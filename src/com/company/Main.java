package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Main {
    private static int counter =0;

    public static void main(String[] args) {
	// write your code here
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
