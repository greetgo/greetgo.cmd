package kz.greetgo.cmd.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.*;

public class SimpleServer {
  public static void main(String[] args) throws Exception {
    try (ServerSocket serverSocket = new ServerSocket(58207, 0, InetAddress.getByName(null))) {

      System.nanoTime();

      //noinspection InfiniteLoopStatement
      while (true) {

        System.out.println("Reading...");

        try (Socket socket = serverSocket.accept()) {

          InputStream inputStream = socket.getInputStream();

          InputStreamReader reader = new InputStreamReader(inputStream, UTF_8);

          BufferedReader br = new BufferedReader(reader);

          List<String> lines = new ArrayList<>();

          while (true) {
            String line = br.readLine();
            if (line == null) break;
            lines.add(line);
          }

          System.out.println(lines);

          OutputStream outputStream = socket.getOutputStream();
          for (String line : lines) {
            outputStream.write(("Hello " + line + "\n").getBytes(UTF_8));
          }
          outputStream.close();

          inputStream.close();
        }


      }

    }
  }
}