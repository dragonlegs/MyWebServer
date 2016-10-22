/* file is: BCClient.java   5-5-07  1.0

For use with webserver back channel. Written for Windows.

This program may contain bugs. Note: version 1.0.

To compile: 

rem jcxclient.bat
rem java compile BCClient.java with xml libraries...
rem Here are two possible ways to compile. Uncomment one of them:
rem set classpath=%classpath%C:\dp\435\java\mime-xml\;c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar;
rem javac -cp "c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar" BCClient.java

Note that both classpath mechanisms are included. One should work for you.

Requires the Xstream libraries contained in .jar files to compile, AND to run.
See: http://xstream.codehaus.org/tutorial.html


To run:

rem rxclient.bat
rem java run BCClient.java with xml libraries:
set classpath=%classpath%C:\dp\435\java\mime-xml\;c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar;
java BCClient

This is a standalone program to connect with MyWebServer.java through a
back channel maintaining a server socket at port 2570.

----------------------------------------------------------------------*/

import java.io.*;  // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.*;


class myDataArray {
  int num_lines = 0;
  String[] lines = new String[8];
}

public class BCHandler{
    //Creates Output to file of xml
  private static String XMLfileName = "C:\\temp\\mimer.output";
  private static PrintWriter      toXmlOutputFile;
  private static File             xmlFile;
  private static BufferedReader   fromMimeDataFile;

  
  public static void main (String args[]) {
    String serverName;
      //Default filePath if cannot get env value
    String argOne = "WillBeFileName";
    //Initialze to get env variables
    Properties p = new Properties(System.getProperties());
    if (args.length < 1) serverName = "localhost";
    else serverName = args[0];
      //Shim.bat passes firstarg="" for location of file
	argOne = p.getProperty("firstarg");
      System.out.println("ArgONE" + argOne);

    //Initalize xStream
      XStream xstream = new XStream();
    String[] testLines = new String[4];  int i;
    myDataArray da = new myDataArray();
    myDataArray daTest = new myDataArray();

    System.out.println("Hemanth Ande's back channel Client.\n");
    System.out.println("Using server: " + serverName + ", Port: 2540 / 2570");
    //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      try {
          //Open File and Read it
          fromMimeDataFile = new BufferedReader(new FileReader(argOne));
      }catch (FileNotFoundException ioe){
          System.out.println(ioe.toString());
      }
    try {
//      String userData;
//
//	System.out.print
//	  ("Enter a string to send to back channel of webserver, (quit) to end: ");
//	System.out.flush ();
	//userData = in.readLine ();
	//da.lines[0] = "You "; da.lines[1] = "typed "; da.lines[2] = userData;
	//da.num_lines = 3;
        i =0;
        //Read line by line up to 8 lines
	 while(((da.lines[i++] = fromMimeDataFile.readLine())!= null) && i < 8){
	System.out.println("Data is: " + da.lines[i-1]);
      }
      //Set the number of lines read max 8
	da.num_lines = i -1;
	System.out.println("i is " + i);
        //Convert da to string XML
	String xml = xstream.toXML(da);
        //Send it back to webServer
	  sendToBC(xml, serverName);

	  System.out.println("\n\nHere is the XML version:");
	  System.out.print(xml);

	  daTest = (myDataArray) xstream.fromXML(xml); // deserialize data
	  System.out.println("\n\nHere is the deserialized data: ");
	  for(i=0; i < daTest.num_lines; i++){System.out.println(daTest.lines[i]);}
	  System.out.println("\n");

	  xmlFile = new File(XMLfileName);
	  if (xmlFile.exists() == true && xmlFile.delete() == false){
	    throw (IOException) new IOException("XML file delete failed.");
	  }
	  xmlFile = new File(XMLfileName);
	  if (xmlFile.createNewFile() == false){
	    throw (IOException) new IOException("XML file creation failed.");
	  }
	  else{
	    toXmlOutputFile =
	      new PrintWriter(new BufferedWriter(new FileWriter(XMLfileName)));
	    toXmlOutputFile.println("First arg to Handler is: " + argOne + "\n");
	    toXmlOutputFile.println(xml);
	    toXmlOutputFile.close();
	  }




    } catch (IOException x) {x.printStackTrace ();}
  }


  
  static void sendToBC (String sendData, String serverName){
    Socket sock;
    BufferedReader fromServer;
    PrintStream toServer;
    String textFromServer;
    try{
      // Setting up connection
      sock = new Socket(serverName, 2570);
      toServer   = new PrintStream(sock.getOutputStream());

      fromServer = 
	new  BufferedReader(new InputStreamReader(sock.getInputStream()));
      
      toServer.println(sendData);
        //Tells the webserver no more data after this line.
      toServer.println("end_of_xml");
      toServer.flush(); 
      // Read two or three lines of response from the server,
      // and block while synchronously waiting:
      System.out.println("Blocking on acknowledgment from Server... ");
        // Waiting for response from server will wait forever
      textFromServer = fromServer.readLine();
        //Conintue to print until server stops sending infomration
      if (textFromServer != null){System.out.println(textFromServer);}
      sock.close();
    } catch (IOException x) {
      System.out.println ("Socket error.");
      x.printStackTrace ();
    }
  }
}
