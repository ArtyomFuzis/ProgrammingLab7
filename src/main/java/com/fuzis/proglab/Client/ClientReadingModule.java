package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;

public class ClientReadingModule {
    public static ClientLogger logger = ClientLogger.getInstance();
    public static ObjectInputStream ois;
    public static void start_reading(Consumer<AppData.MessageData> callback) {
        if (ois == null) {
            while (true) {
                try {
                    ois = new ObjectInputStream(ClientConnectionModule.is);
                    break;
                } catch (IOException e) {
                    logger.error("Connection problem");
                    logger.error(e.getMessage());
                    ClientConnectionModule.connect();
                }
            }
        }
        Thread thr = new Thread(() -> {inner_read(callback);});
        thr.setDaemon(true);
        thr.start();
    }
    public static void inner_read(Consumer<AppData.MessageData> callback)
    {
        while(true) {
            while (true) {
                try {
                    callback.accept((AppData.MessageData) ois.readObject());
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Connection problem");
                    logger.error(e.getMessage());
                    ClientConnectionModule.connect();
                    while (true) {
                        try {
                            ois = new ObjectInputStream(ClientConnectionModule.is);
                            break;
                        } catch (IOException e2) {
                            logger.error("Connection problem");
                            logger.error(e.getMessage());
                            ClientConnectionModule.connect();
                        }
                    }
                }
            }
        }
    }
}
