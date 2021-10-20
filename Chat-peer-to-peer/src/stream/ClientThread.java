/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	String pseudo;
	BufferedReader socIn;
	PrintStream socOut;
	static List<ClientThread> connectedClients = new ArrayList<>();
	FileWriter writer;
	
	ClientThread(Socket s) {
		this.clientSocket = s;
		socIn=null;
		try {
			writer = new FileWriter("historique.log",true);
		}
		catch(Exception e){
			System.out.println("Failed opening historic file");
		}
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	**/
	public void run() {
    	  try {
			  System.out.println("Current static clients: "+connectedClients.size());
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		socOut = new PrintStream(clientSocket.getOutputStream());
			String target="";
			for(int i=0; i<2;i++){
				String line = socIn.readLine();
				if(i==0){
					pseudo =line;
				}
				else{
					target = line;
				}
			}
			if(!target.isEmpty()) {
				System.out.println("Client " + pseudo + " wants to communicate with " + target);
				writer.write("Client " + pseudo + " wants to communicate with " + target);
				writer.write("\r\n");
				writer.close();
				connectToFriend(target);
			}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }

	   public void connectToFriend(String targetPseudo){
		// Find the client thread if it exists
		   ClientThread ct=null;
		   for(ClientThread c: connectedClients){
			   if(c.pseudo.equals(targetPseudo)){
				   ct=c;
				   break;
			   }
		   }
		   if(ct!=null){
			   try {
				   while (true) {
					   String line = socIn.readLine();
					   ct.socOut.println(line);
					   line = ct.socIn.readLine();
					   socOut.println(line);

				   }
			   }
			   catch(Exception e){
				   System.err.println("Error in EchoServer:" + e);
			   }
		   }
		   else{
			   System.out.println("Cannot connect to "+targetPseudo+" since it does not exist");
		   }
	   }

	   static public void addClient(ClientThread c){
		connectedClients.add(c);
	   }
  
  }

  
