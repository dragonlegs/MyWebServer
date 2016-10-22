package com.company;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Hemanth on 10/5/16.
 */
public class FileServer implements Runnable {
    Socket sock;
    FileServer(Socket s){sock=s;}
    private BufferedReader in =null;
    private OutputStream out = null;
    FileList hello = new FileList();


    @Override
    public void run(){
        try {
            System.out.println("File Server");
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = (sock.getOutputStream()) ;
            //out.write("Hello".getBytes());
//            System.out.println(in.readLine());
//                if (in.readLine().isEmpty()){
//                    errorView(out);
//                    return;
//                }
            String enteredString="";
            String reading;
            //Read all of the input message until null or ""
            while(!(reading= in.readLine()).isEmpty()) {
                //System.out.println(reading);
                enteredString = enteredString + reading;
            }
            System.out.println("Entered String: " +enteredString);
            if (!enteredString.equals(null)) {
                String[] identifyInput = enteredString.split(" ");
                //System.out.println(Arrays.toString(identifyInput));
                if (identifyInput[0].isEmpty()){
                    chooser("error");
                }else {
                    chooser(identifyInput[1]);
                }
                //basicView(out);
                out.flush();
                out.close();
            }
        }catch (IOException e){
            errorView(out);
            System.out.println(e.getMessage());

        }catch (NullPointerException e){
            errorView(out);
            System.out.println(e.getMessage());
        }
        try {
            sock.close();
        }catch (IOException e){System.out.println(e.getMessage());}
    }

//Displays root directory
    private void basicView(OutputStream out){
        String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr>                            -   \n";
        String sendHtml = hello.getHtmlCode();
        String formatHeader = hello.formatHtmlHeader();
        System.out.println("Current Dir: "+hello.getCurrDir());
        int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9;
        try {

            out.write("HTTP/1.1 200 OK\n".getBytes());
            out.write("Content-Type: text/html\n".getBytes());
            out.write(("Content-Length: " + length+"\n").getBytes());
            System.out.println("Content-Length: " + length);
            out.write("\r\n".getBytes());
            out.write(formatHeader.getBytes());
            out.write(listingHeader.getBytes());
            out.write(sendHtml.getBytes());
            out.write("</pre>".getBytes());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    //Display subfolder files
    private void folderView(OutputStream out,String folder){
        System.out.println("Get Folder: " + folder);
        hello.setCurrDir(folder);
        String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr><img src=\"\" alt=\"[DIR]\"> <a href=\""+hello.getParentDir()+"\">Parent Directory</a>                             -   \n";
        String sendHtml = hello.getHtmlCode();
        String formatHeader = hello.formatHtmlHeader();
        int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9;
        System.out.println("Current Dir: "+hello.getCurrDir());
        try {
            out.write("HTTP/1.1 200 OK\n".getBytes());
            out.write("Content-Type: text/html\n".getBytes());
            out.write(("Content-Length: " + length+"\n").getBytes());
            System.out.println("Content-Length: " + length);
            out.write("\r\n".getBytes());
            out.write(formatHeader.getBytes());
            out.write(listingHeader.getBytes());
            out.write(sendHtml.getBytes());
            out.write("</pre>".getBytes());
        }catch (IOException e){
            System.out.println(e.getMessage().toString());
        }
    }
    //Send File
    private void getfile(OutputStream out,String fileName){
        System.out.println("Get File: " + fileName);
        hello.setCurrDir(fileName);
//        String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr><img src=\"\" alt=\"[DIR]\"> <a href=\""+hello.getCurrDir()+"\">Parent Directory</a>                             -   \n";
//        String sendHtml = hello.getHtmlCode();
//        String formatHeader = hello.formatHtmlHeader();
//        int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9;
        byte[] data = hello.getFile(fileName);
        System.out.println(hello.getMIMEtype(fileName.substring(fileName.lastIndexOf("/") + 1)));
        try {

            out.write("HTTP/1.1 200 OK\n".getBytes());
            out.write(("Content-Type: " + hello.getMIMEtype(fileName.substring(fileName.lastIndexOf("/") + 1))+"\n").getBytes());
            out.write(("Content-Length: " + data.length+"\n").getBytes());
            System.out.println("Content-Length: " + data.length);
            out.write("\r\n".getBytes());
            //System.out.println("Data"+data);
            out.write(data);
            //out.println(formatHeader);
            //out.println(listingHeader);
            //out.println(sendHtml);
            //out.println("</pre>");
        }catch (IOException e){
            System.out.println(e.getMessage());
            errorView(out);
        }

    }
//Read Get statement and direct them towards current destination
    private void chooser(String input){
        System.out.println(input);
        //If its root
       if (input.equals("/"))
           basicView(out);
       //if it contains folder and . which is for file with ext
       else if (input.contains("/") && input.contains(".")){
           getfile(out,input);
       }
       //Redirect for birthday what happens in script
       else if (input.contains("/cgi/birthday")){
           System.out.println("Do calculations");
           Birthday guess = new Birthday(input,out);
       }
       //If it contains folder only
       else if (input.contains("/")){
           folderView(out,input);
       }
        //If all else fails 500 error
        else{
           errorView(out);
       }



    }
//Default 500 server error if anything goes wrong
    public void errorView(OutputStream out){
        String error = "<h1>500 Error</h1>";
        try {
            out.write("HTTP/1.1 500\n".getBytes());
            out.write("Content-Type: text/html\n".getBytes());
            out.write((("Content-Length: " + error.length()+"\n").getBytes()));
            out.write("\r\n".getBytes());
            out.write(error.getBytes());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
