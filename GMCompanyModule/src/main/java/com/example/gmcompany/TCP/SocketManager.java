package com.example.gmcompany.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketManager {
    private String host_local = "192.168.1.2";
    private String host_net = "111.175.77.137";
    private int port = 8107;
    private Socket socket;
    private BufferedReader reader = null;
    private OutputStream stream = null;
    public boolean isReceived = false;
    public SocketManager(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host_local,port),3000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stream = socket.getOutputStream();
            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message){
        try {
            stream.write(message.getBytes("utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessage(){
        try {
            isReceived = false;
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private class ReceiveThread extends Thread{
        @Override
        public void run() {
            try {
                String content;
                while ((content = reader.readLine()) != null){
                    //isReceived = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
