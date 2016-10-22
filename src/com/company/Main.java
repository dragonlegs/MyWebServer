package com.company;

/*--------------------------------------------------------

1. Name / Date: Hemanth Ande Oct 9

2. Java 1.8

3.

> javac *.java


4. Precise examples / instructions to run this program:

Run it in folder you wish to serve

5. List of files needed for running the program.

 a. checklist.html
 b. Birthday.java
 c. FileList.java
 d. Main.java

5. Notes:

e.g.:

Unable to make all files/folders work in chrome

----------------------------------------------------------*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static int counter =0;

    public static void main(String[] args) {
//  Uses port 2540
       int q_len = 6;
        int port = 2540;
        Socket sock;
        try {
            ServerSocket servsock = new ServerSocket(port, q_len);
            System.out.println("My WebServer is Running on port "+port);
            while (true){
                sock = servsock.accept();
                System.out.println(counter);
                new FileServer(sock).run();
                counter++;
            }
        }catch (IOException e){
        System.out.println("Unable to bind to "+port);
            System.out.println(e.getMessage());
        }


    }
}
