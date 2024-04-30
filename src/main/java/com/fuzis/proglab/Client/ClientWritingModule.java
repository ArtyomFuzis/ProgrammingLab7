package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Server.ServerConnectionModule;
import com.fuzis.proglab.Server.ServerWritingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientWritingModule {
    ClientLogger logger = ClientLogger.getInstance();
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static ObjectOutputStream oos;
    public void write(AppData.MessageData req)
    {
        if (oos == null) {
            try {
                oos = new ObjectOutputStream(ClientConnectionModule.os);
            } catch (IOException e) {
                logger.error("Can't be written");
                logger.error(e.getMessage());
            }
        }
        Runnable run = ()->{inner_write(req);};
        executorService.submit(run);
    }
    public synchronized void inner_write(AppData.MessageData req) {
        try {
            synchronized (oos) {
                oos.writeObject(req);
            }
        } catch (Exception e) {
            logger.error("Can't be written");
            logger.error(e.getMessage());
        }
    }
}
