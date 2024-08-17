# Java web server

Web server usando Java puro. Para estudos apenas.

## Local tests

curl --compressed -v -H "Accept-Encoding: gzip" http://localhost:4221/echo/abc
curl -v --data "12345" -H "Content-Type: application/octet-stream" http://localhost:4221/files/file_123
