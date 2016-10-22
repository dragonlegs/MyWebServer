package com.company;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Hemanth on 10/22/2016.
 */
public class BCWorker extends Thread {
    private Socket sock;
    private int i;
    BCWorker (Socket s){sock = s;}
    PrintStream out = null;BufferedReader in = null;

    String[] xmlLines = new String[15];
    String[] testLines = new String[10];
    String xml;
    String temp;
    XStream xstream = new XStream();
    final String newLine = System.getProperty("line.separator");
    myDataArray da = new myDataArray();

    public void run(){
        System.out.println("Called BC worker.");
        try{
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream()); // to send ack back to client
            i = 0; xml = "";
            while(true){
                temp = in.readLine();
                if (temp.indexOf("end_of_xml") > -1) break;
                else xml = xml + temp + newLine; // Should use StringBuilder in 1.5
            }
            System.out.println("The XML marshaled data:");
            //Raw XML Strings
            System.out.println(xml);
            out.println("Acknowledging Back Channel Data Receipt"); // send the ack
            out.flush(); sock.close();
            XStream xstream = new XStream();
            //Creating alias solves ClassNotresolved Exeception
            xstream.alias("myDataArray",com.company.myDataArray.class);
            //Cast data type for xstream which returns object that can mapped to the structure
            da = (myDataArray)xstream.fromXML(xml); // deserialize / unmarshal data
            System.out.println("Here is the restored data: ");
            for(i = 0; i < da.num_lines; i++){
                System.out.println(da.lines[i]);
            }
        }catch (IOException ioe){

            System.out.println(ioe.toString());
        }catch( CannotResolveClassException e){
            System.out.println(e);// end run
        }
    }
    }


