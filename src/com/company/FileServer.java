package com.company;

import java.io.*;
import java.net.Socket;

/**
 * Created by Hemanth on 10/5/16.
 */
public class FileServer implements Runnable {
    Socket sock;
    FileServer(Socket s){sock=s;}
    private BufferedReader in =null;
    private PrintWriter out = null;

    @Override
    public void run(){
        try {
            System.out.println("File Server");
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());
            System.out.println(in.readLine());
//            while (in.readLine() != null ){
//                System.out.println(in.readLine());
//                System.out.println("a");
//            }
            FileList hello = new FileList();
            String message = "<p>Hello</p>";
            System.out.println("hello");
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("Content-Length: "+hello.getHtmlCode().length());
            out.print("\r\n");
            System.out.println(hello.getHtmlCode());
            out.println(hello.getHtmlCode());
            out.flush();
            out.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        try {
            sock.close();
        }catch (IOException e){System.out.println(e.getMessage());}
    }

}
