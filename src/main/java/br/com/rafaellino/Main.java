package br.com.rafaellino;

import br.com.rafaellino.resources.EchoGet;
import br.com.rafaellino.resources.FileResource;
import br.com.rafaellino.resources.UserAgentResource;
import br.com.rafaellino.utils.FileHandlerIoImpl;
import br.com.rafaellino.utils.GzipHttpEncondingImpl;
import br.com.rafaellino.utils.HttpEnconding;
import br.com.rafaellino.utils.HttpEncondingChain;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  public static final HttpEncondingChain httpEncondingChain = new HttpEncondingChain();

  private static Map<String, String> getArgs(String... args) {
    Map<String, String> registeredArgs = new HashMap<>();
    registeredArgs.put(Constants.ARG_DIRECTORY, "/tmp");
    registeredArgs.put(Constants.ARG_PORT, "4221");
    registeredArgs.put(Constants.ARG_THEAD_POOL, "10");
    if (args.length == 0) {
      return registeredArgs;
    }
    for (int i = 0; i < args.length; i ++) {
      if (i % 2 == 0) {
        String currentArg = args[0];
        if (Objects.isNull(registeredArgs.get(currentArg))) {
          System.out.println("arg " + currentArg + " isnt registered, therefore will be ignored");
          i++;
          continue;
        }
        registeredArgs.put(args[i], args[i+1]);
      }
    }
    return registeredArgs;
  }

  public static void main(String[] args) {

    Map<String, String> argsParsed = getArgs(args);
    final int port = Integer.parseInt(argsParsed.get(Constants.ARG_PORT));
    final String defaultFolder = argsParsed.get(Constants.ARG_DIRECTORY);
    final int threadPool = Integer.parseInt(argsParsed.get(Constants.ARG_THEAD_POOL));

    Map<String, RequestHandler> mappedPaths = new HashMap<>();
    mappedPaths.put("/echo", new EchoGet());
    mappedPaths.put("/user-agent", new UserAgentResource());
    mappedPaths.put("/files", new FileResource(new FileHandlerIoImpl(defaultFolder)));

    httpEncondingChain.addEnconde(new GzipHttpEncondingImpl());

    ExecutorService executor = Executors.newFixedThreadPool(threadPool);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      // CRLF = CR carriage return \r, LF line feed, \n
      // CR = volta para o começo da linha sem abrir uma nova linha
      // LR = abre uma nova linha
      // http response 3 parts: status, header, body optional
      serverSocket.setReuseAddress(true);
      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("accepted new connection");
        System.out.println("connect to " + socket.getInetAddress().getHostAddress());
        System.out.println("address = " + socket.getInetAddress().toString());
        executor.execute(new ClientHandler(socket, mappedPaths));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}

class ClientHandler extends Thread {
  private final Socket socket;
  private final Map<String, RequestHandler> mappedPaths;

  public ClientHandler(Socket socket, Map<String, RequestHandler> mappedPaths) {
    this.socket = socket;
    this.mappedPaths = mappedPaths;
  }

  private static byte[] readDataFromSocket(InputStream inputStream) throws IOException {
    byte[] resultArray = new byte[0];
    while (true) {
      byte[] buffer = new byte[2048];
      int bytesRead = inputStream.read(buffer, 0, buffer.length);
      if (bytesRead == -1) {
        break;
      }

      byte[] temp = new byte[resultArray.length + bytesRead];
      System.arraycopy(resultArray, 0, temp, 0, resultArray.length);
      System.arraycopy(buffer, 0, temp, resultArray.length, bytesRead);
      resultArray = temp;
      if (bytesRead < buffer.length) {
        break;
      }
    }
    return resultArray;
  }

  public void run() {
    try {
      HttpRequest httpRequest = HttpRequest
              .makeFromString(new String(readDataFromSocket(socket.getInputStream()), StandardCharsets.UTF_8));
      if (Constants.httpVersionsSupported.stream().noneMatch(hv -> httpRequest.getHttpVersion().equals(hv))) {
        throw new RuntimeException("Http version not supported");
      }
      if (httpRequest.getPath().isEmpty() || httpRequest.getPath().equals("/")) {
        socket.getOutputStream().write(new HttpResponse(Constants.STATUS_OK).getBytes());
      } else {
        var func = mappedPaths.get(httpRequest.getRootPath());
        if (func == null) {
          socket.getOutputStream().write(new HttpResponse(Constants.STATUS_NOT_FOUND).getBytes());
        } else {
          try {
            HttpResponse response = func.handle(httpRequest);
            response = Main.httpEncondingChain.process(response, httpRequest.getHeaders());
            socket.getOutputStream().write(response.getBytes());
          } catch (final Exception e) {
            System.out.println("error: " + e.getMessage());
            e.printStackTrace();
            socket.getOutputStream().write(new HttpResponse(Constants.STATUS_INTERNAL_SERVER_ERROR).getBytes());
          }
        }
      }
      this.socket.close();
    } catch (final IOException e) {
      System.out.println("io exception " + e.getMessage());
      e.printStackTrace();
    }
  }
}
