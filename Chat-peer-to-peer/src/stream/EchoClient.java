/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;



public class EchoClient {

 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;
        String pseudo = "";

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }

        try {
      	    // creation socket ==> connexion
      	    echoSocket = new Socket(args[0],Integer.parseInt(args[1]));
	    socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	    socOut= new PrintStream(echoSocket.getOutputStream());
	    stdIn = new BufferedReader(new InputStreamReader(System.in));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        System.out.print("Please enter your pseudo: ");
        line = stdIn.readLine();
        pseudo=line;
        socOut.println(line);

        System.out.print("Please enter the pseudo of you target: ");
        line = stdIn.readLine();
        socOut.println(line);

        // If the user don't wanna get connected with a user we can show his history
        if(line.equals("")){
            System.out.print("Do you want to check your historic? (yes/no) ");
            line=stdIn.readLine();
            if(line.equals("yes")) {
                readHistoric(pseudo);
            }
        }

        while (true) {
        	line=stdIn.readLine();
            if (line.equals(".")) {
                break;
            }
            writeToHistoric(pseudo+": "+line+"\r\n");
        	socOut.println(line);
        	System.out.println("received: " + socIn.readLine());
        }
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }

    public static void writeToHistoric (String message){
        try {
            FileWriter writer = new FileWriter("historique.log", true);
            writer.write(message);
            writer.close();
        }
        catch(Exception e){
            System.out.println("Failed write to historic");
        }
    }

    public static void readHistoric (String pseudo){
        try {
            System.out.println("Showing historic...");
            BufferedReader reader = new BufferedReader(new FileReader("historique.log"));
            String currentLine = reader.readLine();
            boolean toPrint = currentLine.startsWith("Client") && currentLine.contains(pseudo);
            while(currentLine!=null && !currentLine.isEmpty()){

                if(currentLine.startsWith("Client")){
                    toPrint = currentLine.contains(pseudo);
                }
                if(toPrint){
                    System.out.println(currentLine);
                }
                currentLine = reader.readLine();
            }

            reader.close();
        }
        catch(Exception e){
            System.out.println("No historic to be shown");
        }
    }
}


