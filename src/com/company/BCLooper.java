package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Hemanth on 10/22/2016.
 */
public class BCLooper implements Runnable {

    public static boolean adminControlSwitch = true;

    public void run(){
        System.out.println("In BC Looper thread,waiting at port 2570");

        int q_len = 6;
        int port = 2570;
        Socket sock;

        try{
            ServerSocket servsock = new ServerSocket(port,q_len);
            while (adminControlSwitch){
                sock = servsock.accept();
                new BCWorker(sock).start();
            }

        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }

}
