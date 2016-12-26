// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	public static String passwordDB;
	static String table= "worker";
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  

  
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
	  System.out.println("Message received: " + msg + " from " + client);
  
	 
	  mysqlConnection mysqlcon = new mysqlConnection(passwordDB);
	  ArrayList<String> parameters = (ArrayList<String>)msg;
	  if(((String)parameters.get(0)).equals("1")){ //show all deatils
		  try 
	      { 
			
			  ArrayList<String> result = mysqlConnection.select(table, "", mysqlcon.conn);
			  if(Integer.parseInt(result.get(0)) > 0){
				  client.sendToClient("Here is the details of all the workers:\n");
				  
				  int j=1; 
				  for(int i=0; i<Integer.parseInt(result.get(0));i++){
					if(j>1)
						j=j+2;
					client.sendToClient(result.get(j)+" "+result.get(j+1)+" "+result.get(j+2)+"\n");
					j++;
				  }
				 } 
			  else
			  client.sendToClient("There is no workers at the company\n");
			  client.sendToClient(mysqlConnection.PrintMenu());

	      }   
		  catch (Exception localException2) {}
	  } 
	  else if(((String)parameters.get(0)).equals("2")){
		  try 
	      {    
			  ArrayList<String> result = mysqlConnection.select(table, (String)parameters.get(1), mysqlcon.conn);
			 if(Integer.parseInt(result.get(0)) > 0){
				client.sendToClient(result.get(2)+"'s department before the update is: "+result.get(3));
				mysqlConnection.update("worker", (String)parameters.get(1), (String)parameters.get(2), mysqlcon.conn);
				
				ArrayList<String> result_afterUpdate = mysqlConnection.select(table, (String)parameters.get(1), mysqlcon.conn);
				client.sendToClient(result_afterUpdate.get(2)+"'s department after the update is: "+result_afterUpdate.get(3)+"\n");

			 }
			 else
				 client.sendToClient("There is no worker with ID: "+(String)parameters.get(1)+"\nPlease try again.\n");
			 client.sendToClient(mysqlConnection.PrintMenu());

			  }
				  
	       
		  catch (Exception localException2) {}	  }
	 
	  }

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	 
	 	
	
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on
    Scanner in = new Scanner(System.in);
    System.out.println("Please Enter DB Password:");
    passwordDB = in.nextLine();
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
     
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

}
//End of EchoServer class
