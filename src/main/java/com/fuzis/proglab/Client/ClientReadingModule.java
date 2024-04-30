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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ClientReadingModule {
    public static ClientLogger logger = ClientLogger.getInstance();
    public static ObjectInputStream ois;
    public static ForkJoinPool executorService = new ForkJoinPool();
    public AppData.MessageData read() {
        if (ois == null) {
            try {
                ois = new ObjectInputStream(ClientConnectionModule.is);
            } catch (IOException e) {
                logger.error("Can't be read");
                logger.error(e.getMessage());
                return null;
            }
        }
        RecursiveTask<AppData.MessageData> act = new RecursiveTask<AppData.MessageData>() {
            @Override
            protected AppData.MessageData compute() {
                return inner_read();
            }
        };
        executorService.submit(act);
        return act.join();
    }

    public AppData.MessageData inner_read() {
        try {
            return (AppData.MessageData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Can't be read");
            logger.error(e.getMessage());
        }
        return null;
    }
}
