Simple Java HTTP Server


 A minimal HTTP server built from scratch in Java 21, using only java.net.ServerSocket — no Spring, no Netty, no embedded
  Tomcat. The goal of this project is to learn how the HTTP protocol actually works at the byte level: parsing the request
  line, constructing the response with proper status line, headers, and CRLF separators, and writing it back over a raw socket.


  What it does

  - Opens a ServerSocket on a port read from a JSON config file.
  - Loops on accept(), handling one client connection at a time on the main thread.
  - Reads the HTTP request line (e.g. GET /Sergiu HTTP/1.1) with a BufferedReader.
  - Extracts the URL path, strips any query string, URL-decodes it, and HTML-escapes it to prevent injection.
  - Renders an HTML page whose <h1> welcomes the visitor by name:
    - localhost:8080/Sergiu → Welcome, Sergiu
    - localhost:8080/ → Welcome, Guest
  - Writes a hand-built HTTP/1.1 response (status line + Content-Length header + body) and closes the connection.


Architecture

  - HttpServer — entry point; owns the socket loop and the request/response handling.
  - ConfigurationManager — singleton that loads server config from disk.
  - Configuration — POJO mapped from http.json (port, webroot).
  - HttpConfigurationException — custom unchecked exception thrown when config can't be loaded or parsed.
  - Json — small Jackson wrapper used by the configuration layer.
  - src/main/resources/http.json — runtime config; change the port without recompiling


 Tech stack

  - Java 21, Maven
  - Jackson (jackson-core + jackson-databind) for JSON config parsing
  - Multi-stage Docker build: maven:3.9-eclipse-temurin-21 for compilation, eclipse-temurin:21-jre for runtime


Running

  Locally:
  mvn compile exec:java -Dexec.mainClass=com.server.HttpServer

  In Docker:
  docker build -t java-http-server .
  docker run --rm -p 8080:8080 java-http-server

  Then visit http://localhost:8080/<any-word>.
