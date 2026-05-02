package com.server;

import com.server.config.Configuration;
import com.server.config.ConfigurationManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Driver class for a Http Server
 */

public class HttpServer {
    public static void main(String[] args) {
        System.out.println("Starting HTTP Sever...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf =  ConfigurationManager.getInstance().getCurrentConfiguration();

        System.out.println("Using port:" + conf.getPort());
        System.out.println("Using port:" + conf.getWebroot());

        try{
            ServerSocket serverSocket = new ServerSocket(conf.getPort());
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                // TODO we would read
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String requestLine = reader.readLine();

                String word = "Guest";
                if(requestLine != null){
                    String[] parts = requestLine.split(" ");
                    if(parts.length >= 2){
                        String path = parts[1];
                        int queryStart = path.indexOf('?');
                        if (queryStart >=0) path = path.substring(0,queryStart);
                        if(path.startsWith("/")) path = path.substring(1);
                        if(!path.isEmpty()) {
                            word = escapeHtml(URLDecoder.decode(path, StandardCharsets.UTF_8));
                        }
                    }
                }

                String html = "<html><head><title>Simple Java HTTP Sever</title></head><body><h1> Welcome, " + word + " to my Simple Java HTTP Server</body></html>";

                // TODO we would writing

                String  CRLF = "\n\r";

                String response =
                        "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP VERSION RESPONSE_CODE RESPONSE_MESSAGE
                        "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                        CRLF +
                        html +
                                CRLF + CRLF;

                outputStream.write(response.getBytes());

                inputStream.close();
                outputStream.close();
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }


private static String escapeHtml(String s) {
    return s.replace("&", "&amp;")
           .replace("<", "&lt;")
            .replace(">", "&gt;")
             .replace("\"", "&quot;");
   }
}