package com.server;

import com.server.config.Configuration;
import com.server.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // TODO we would read

            String html = "<html><head><title>Simple Java HTTP Sever</title></head><body><h1> This Page was server using my Simple Java HTTP Server</body></html>";

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
            serverSocket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}