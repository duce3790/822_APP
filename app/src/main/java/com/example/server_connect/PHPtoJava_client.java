package com.example.server_connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PHPtoJava_client extends Thread{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    String disconnectReason;
    boolean disconnected = false;
    public boolean running;

    public static void main(String[] args) {
        PHPtoJava_client client = new PHPtoJava_client();
        client.sendMessage("Hello!");
    }

    public PHPtoJava_client() {
        new Thread(this).start();
        running = true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 81);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (running) {
                String line;
                if((line = br.readLine()) != null) {
                    System.out.println("Server response: " + line);
                }
            }
            System.out.println("Disconnected. Reason: " + disconnectReason);
            disconnected = true;
            running = false;
            br.close();
            out.close();
            socket.close();
            System.out.println("Shutted down!");
            System.exit(0);
        } catch (IOException e) {
            if(e.getMessage().equals("Connection reset")) {
                disconnectReason = "Connection lost with server";
                disconnected = true;
                System.out.println("Disconnected from server. Reason: Connection reset");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if(running) {
            if (!disconnected) {
                if (out != null && socket != null) {
                    out.println(message);
                    out.flush();
                }
            }
        }
    }
}

