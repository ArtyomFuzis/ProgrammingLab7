package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class ClientConnectionModule {
    public static InputStream is;
    public static OutputStream os;
    public static ClientLogger log = ClientLogger.getInstance();
    public static boolean tryconnect() {
        try {
            InetSocketAddress addr;
            try {
                 addr = new InetSocketAddress(AppData.ADDRESS, AppData.PORT);
            }
            catch (IllegalArgumentException e)
            {
                log.error("Invalid port, set to 4352");
                addr = new InetSocketAddress(AppData.ADDRESS, 4352);
            }
            Socket socket = new Socket();
            socket.connect(addr);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            return true;
        } catch (IOException ex) {
            log.error("Connection error: " + ex.getLocalizedMessage());
            return false;
        }
    }
    public static void connect()
    {
        try {
            while (!ClientConnectionModule.tryconnect()) {
                System.out.println("Server not available, reconnecting");
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException ex)
        {
           log.error("Unexpected interrupt");
           log.error(ex.getLocalizedMessage());
           System.exit(1);
        }
    }
}
