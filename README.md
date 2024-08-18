# Java web server

Web server usando Java puro. Para estudos apenas.

## Local tests

Printar retorno usando gzip
```bash
curl --compressed -v -H "Accept-Encoding: gzip" http://localhost:4221/echo/abc
```

Http post para criar arquivos
```bash
curl -v --data "12345" -H "Content-Type: application/octet-stream" http://localhost:4221/files/file_123
```

Http get obter arquivo criado
````bash
curl -v --header "User-Agent: cuica" http://localhost:4221/files/file_123
````


## TODO

- Sistema de logs
- Performance
- Implementar Servlets
- Container

## Créditos

- https://app.codecrafters.io/courses/http-server/overview
