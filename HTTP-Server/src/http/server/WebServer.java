///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String request = in.readLine();
        if (request == null || request.equals("")) {
          throw new Exception("Unknown request");
        }
        System.out.println("request: " + request);
        String[] params = request.split(" ");
        String httpMethod = params[0];
        String path = params[1];

        switch (httpMethod) {
        case "GET":
          switch (path) {
          case "/":
            // Send the response
            // Send the headers
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            // this blank line signals the end of the headers
            out.println("");
            // Send the HTML page
            out.println("<H1>Welcome to the Home Page</H1>");
            out.println("<a href='/page1.html'>Go to page1</a>");
            out.println("<br/> <br/>");
            out.println("<a href='/nature.jpeg'>Go to jpeg image</a>");
            out.println("<br/> <br/>");
            out.println("<a href='/mario.png'>Go to png image</a>");
            out.println("<br/> <br/>");
            out.println("<a href='/jojo.mp4'>Go to mp4 video</a>");
            out.println("<br/> <br/>");
            out.println("<a href='/form.html'>Go to dynamic form/>");
            break;

          default:
            String[] splitted = path.split("\\.");
            if (splitted.length != 2) {
              out.println("HTTP/1.0 404 Not Found");
              out.println("Content-Type: text/html");
              // this blank line signals the end of the headers
              out.println("");
              out.println("<h1>ERROR 404: Page/Resource Not Found</h1>");
            } else {
              String extension = splitted[1];

              if (extension.equals("html") || extension.equals("txt")) {

                try {
                  String directoryPath = extension.equals("html") ? "html" : "files";
                  File page = new File(directoryPath + path);
                  Scanner reader = new Scanner(page);
                  out.println("HTTP/1.0 200 OK");
                  out.println("Content-Type: text/html");
                  // this blank line signals the end of the headers
                  out.println("");
                  while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    out.println(line);
                  }
                  reader.close();
                } catch (Exception e) {
                  // HTML/text file not found
                  printNotFound(out);
                }

              } else if (extension.equals("jpeg") || extension.equals("png") || extension.equals("mp4")) {
                try {
                  String type = extension.equals("mp4") ? "video" : "image";
                  FileInputStream inp = new FileInputStream(type + "s" + path);
                  remote.getOutputStream().write("HTTP/1.0 200 OK".getBytes());
                  remote.getOutputStream().write(("Content-Type: " + type + "/" + extension).getBytes());
                  remote.getOutputStream().write("\r\n".getBytes());
                  remote.getOutputStream().write("\r\n".getBytes());

                  // Writing the image
                  byte[] bytes = inp.readAllBytes();
                  remote.getOutputStream().write(bytes);
                  remote.getOutputStream().flush();
                  inp.close();
                } catch (Exception e) {
                  // Image file not found
                  printNotFound(out);
                }

              }

              else {
                printNotFound(out);
              }
            }
            break;
          }
          break;

        case "POST":
          // Getting headers
          String info = ".";
          while (info != null && !info.isEmpty()) {
            info = in.readLine();
            System.out.println(info);
          }

          // Getting postData
          String line = ".";
          List<String> postData = new ArrayList<>();
          while (line != null && !line.isEmpty()) {
            line = in.readLine();
            System.out.println("postData " + line);
            postData.add(line);
          }
          // We put out the empty line
          postData.remove(postData.size() - 1);
          System.out.println(postData);

          String fileName = postData.get(0);
          // Creates file if it doesn't exist
          String filePath = "files/" + fileName + ".txt";
          File file = new File(filePath);
          file.createNewFile();

          // Writes post data to file
          FileWriter writer = new FileWriter(filePath);
          for (int i = 1; i < postData.size(); i++) {
            writer.write(postData.get(i));
            if (i != postData.size() - 1) {
              writer.write(System.lineSeparator());
            }
          }
          writer.close();

          out.println("HTTP/1.0 201 Created");
          out.println("Content-Type: text/html");
          // this blank line signals the end of the headers
          out.println("");
          out.println("");
          out.println("File Created with name: " + fileName + ".txt");
          break;

        case "DELETE":
          if (path.equals("/")) {
            out.println("HTTP/1.0 400 Bad Request");
            out.println("Content-Type: text/html");
            // this blank line signals the end of the headers
            out.println("");
            out.println("Please specify a file");
          }

          else {
            String pathFileToDelete = "files" + path;
            File fileToDelete = new File(pathFileToDelete);

            if (fileToDelete.delete()) {
              // Success deleting
              System.out.println("Success deleting file");
              out.println("HTTP/1.0 200 OK");
              out.println("Content-Type: text/html");
              // this blank line signals the end of the headers
              out.println("");
              out.println("Success deleting file with name: " + path.substring(1));
            }

            else {
              // Failed deleting
              System.out.println("Failed deleting file");
              out.println("HTTP/1.0 404 Not Found");
              out.println("Content-Type: text/html");
              // this blank line signals the end of the headers
              out.println("");
              out.println("Failed deleting file, file doesn't exist");
            }
          }
          break;
        }

        out.flush();
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

  public static void printNotFound(PrintWriter out) {
    out.println("HTTP/1.0 404 Not Found");
    out.println("Content-Type: text/html");
    // this blank line signals the end of the headers
    out.println("");
    out.println("<h1>ERROR 404: Page/Resource Not Found</h1>");
  }

  /**
   * Start the application.
   * 
   * @param args Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
