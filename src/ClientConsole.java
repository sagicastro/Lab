// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;
      ArrayList<String> input = new ArrayList();
      while (true) 
      {
        message = fromConsole.readLine();
        input.add(message);
        if(message.equals("2")){
	        System.out.println("Enter worker ID:");
	        input.add(fromConsole.readLine()); //@@@@@@@@@@ CHANGE THE ID IN THE DB TO STRING
	        System.out.println("Enter new department:");
	        input.add(fromConsole.readLine()); //@@@@@@@@@@ CHANGE THE ID IN THE DB TO STRING
	        client.handleMessageFromClientUI(input);

        }
        else if(message.equals("1"))
	        client.handleMessageFromClientUI(input);
        else if(!message.equals("1") && !message.equals("2"))
        {
           	System.out.println("*** Error inputs ****\nPlease one choose options:\nPress 1 to show all the workders details\nPress 2 to set a new detpartment for workder\n");
        }
        

        input.clear();
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
	  message = message.replaceAll(",", "\n");
	  message = message.replace("[", "");
	  message = message.replace("]", "");
    System.out.println("" + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    int port = 0;  //The port number

    Scanner in = new Scanner(System.in);
    System.out.println("Please enter Server IP: \n");
    String host = in.nextLine();
    ClientConsole chat= new ClientConsole(host, DEFAULT_PORT);

    System.out.print("Welcome to HR system\n");
    System.out.print("Please one hoose options:\n");
    System.out.print("Press 1 to show all the workders details\n");
    System.out.print("Press 2 to set a new detpartment for workder\n");
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
