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
    public static ObjectOutputStream oos;
    public void write(AppData.MessageData req) {
        if (oos == null) {
            boolean first_notify = true;
            while (true) {
                try {
                    oos = new ObjectOutputStream(ClientConnectionModule.os);
                    break;
                } catch (IOException e) {
                    if (first_notify) {
                        logger.error("Connection problem: writing");
                        logger.error(e.getMessage());
                        first_notify = false;
                    }
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ex)
                    {
                        logger.error("Interrupt:" + ex.getMessage());
                    }
                }
            }
        }

        Runnable run = () -> {
            inner_write(req);
        };
        Thread t = new Thread(run);
        t.setDaemon(true);
        t.start();
    }

    public synchronized void inner_write(AppData.MessageData req) {
        boolean first_notify = true;
        while (true) {
            try {
                synchronized (oos) {
                    oos.writeObject(req);
                }
                break;
            } catch (IOException e) {
                while (true) {
                    try {
                        oos = new ObjectOutputStream(ClientConnectionModule.os);
                        break;
                    } catch (IOException e2) {
                        if (first_notify) {
                            logger.error("Connection problem: writing");
                            logger.error(e.getMessage());
                            first_notify = false;
                        }
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException ex)
                        {
                            logger.error("Interrupt:" + ex.getMessage());
                        }
                    }
                }
            }
        }
    }
}
