package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionModule {
    public static ServerSocket s_socket;
    public InputStream is;
    public OutputStream os;
    public static Logger logger = LoggerFactory.getLogger(ServerExecutionModule.class);
    public int port;

    public static void warn(String info) {
        logger.warn(info);
    }

    public static void feedback(String info) {
        logger.info(info);
    }

    public static void error(String info) {
        logger.error(info);
    }
    public ServerConnectionModule() {
        port = AppData.PORT;
    }
    public static boolean init() {
        try {
            try {
                s_socket = new ServerSocket(AppData.PORT);
            } catch (IllegalArgumentException e) {
                error("Wrong port number, set 4352");
                s_socket = new ServerSocket(4352);
            }
        } catch (IOException ex) {
            error("SocketCreation error: " + ex.getLocalizedMessage());
            return false;
        }
        feedback("Server Socket created");
        return true;
    }

    public boolean tryconnect() {
        try {
            //if(s_socket == null) {if(!init())return false;}
            Socket socket = s_socket.accept();
            os = socket.getOutputStream();
            is = socket.getInputStream();
            feedback("Connected");
            return true;
        } catch (IOException ex) {
            error("Connection error: " + ex.getLocalizedMessage());
            return false;
        }
    }

}
