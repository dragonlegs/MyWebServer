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
//            System.out.println(in.readLine());
//                if (in.readLine().isEmpty()){
//                    errorView(out);
//                    return;
//                }
            String enteredString = in.readLine();
            System.out.println(enteredString);
            String[] identifyInput = enteredString.split(" ");
            System.out.println(Arrays.toString(identifyInput));
            chooser(identifyInput[1]);
            //basicView(out);
            out.flush();
            out.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        try {
            sock.close();
        }catch (IOException e){System.out.println(e.getMessage());}
    }


    private void basicView(OutputStream out){
        String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr><img src=\"\" alt=\"[DIR]\"> <a href=\""+hello.getCurrDir()+"\">Parent Directory</a>                             -   \n";
        String sendHtml = hello.getHtmlCode();
        String formatHeader = hello.formatHtmlHeader();
        System.out.println("Current Dir: "+hello.getCurrDir());
        int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9;
        try {
            out.write("HTTP/1.1 200 OK".getBytes());
            out.write("Content-Type: text/html".getBytes());
            out.write(("Content-Length: " + length).getBytes());
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
    private void folderView(OutputStream out,String folder){
        System.out.println("Get Folder: " + folder);
        hello.setCurrDir(folder);
        String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr><img src=\"\" alt=\"[DIR]\"> <a href=\""+hello.getCurrDir()+"\">Parent Directory</a>                             -   \n";
        String sendHtml = hello.getHtmlCode();
        String formatHeader = hello.formatHtmlHeader();
        int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9+9999999;
        System.out.println("Current Dir: "+hello.getCurrDir());
        try {
            out.write("HTTP/1.1 200 OK".getBytes());
            out.write("Content-Type: text/html".getBytes());
            out.write(("Content-Length: " + length).getBytes());
            System.out.println("Content-Length: " + length);
            out.write("\r\n".getBytes());
            out.write(formatHeader.getBytes());
            out.write(listingHeader.getBytes());
//            out.write(sendHtml.getBytes());
            out.write("</pre>".getBytes());
            //out.flush();
        }catch (IOException e){
            System.out.println(e.getMessage().toString());
        }
    }

    private void getfile(OutputStream out,String fileName){
        System.out.println("Get File: " + fileName);
        hello.setCurrDir(fileName);
        //String listingHeader = "<pre><img src=\"\" alt=\"Icon \"> <a href=\"?C=N;O=D\">Name</a>                    <a href=\"?C=M;O=A\">Last modified</a>                       <a href=\"?C=S;O=A\">Size</a>     <a href=\"?C=D;O=A\">Description</a><hr><img src=\"\" alt=\"[DIR]\"> <a href=\""+hello.getCurrDir()+"\">Parent Directory</a>                             -   \n";
        //String sendHtml = hello.getHtmlCode();
        //String formatHeader = hello.formatHtmlHeader();
        //int length = listingHeader.length()+sendHtml.length()+formatHeader.length()+9;
        byte[] data = hello.getFile(fileName);
        System.out.println(hello.getMIMEtype(fileName.substring(fileName.lastIndexOf("/") + 1)));
        try {

            out.write("HTTP/1.1 200 OK".getBytes());
            out.write(("Content-Type: " + hello.getMIMEtype(fileName.substring(fileName.lastIndexOf("/") + 1))).getBytes());
            out.write(("Content-Length: " + data.length).getBytes());
            System.out.println("Content-Length: " + data.length);
            out.write("\r\n".getBytes());
            System.out.println(data);
            out.write(data);
            //out.println(formatHeader);
            //out.println(listingHeader);
            //out.println(sendHtml);
            //out.println("</pre>");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    private void chooser(String input){
        System.out.println(input);
       if (input.equals("/"))
           basicView(out);
       else if (input.contains("/") && input.contains(".")){
           getfile(out,input);
       }
       else if (input.contains("/")){
           folderView(out,input);
       }
        else{
           errorView(out);
       }



    }

    public void errorView(OutputStream out){
        String error = "<h1>500 Error</h1>";
        try {
            out.write("HTTP/1.1 500".getBytes());
            out.write("Content-Type: text/html".getBytes());
            out.write((("Content-Length: " + error.length()).getBytes()));
            out.write("\r\n".getBytes());
            out.write(error.getBytes());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
