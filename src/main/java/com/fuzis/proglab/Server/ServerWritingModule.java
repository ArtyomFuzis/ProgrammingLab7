package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Client.ClientConnectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerWritingModule {
    public static final Logger logger = LoggerFactory.getLogger(ServerWritingModule.class);
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static void write(AppData.MessageData req,ObjectOutputStream oos,int id)
    {
        Runnable run = ()->{inner_write(req,oos,id);};
        executorService.submit(run);
    }
    public static void inner_write(AppData.MessageData req,ObjectOutputStream oos,int id) {
        try {
            synchronized (oos) {
                oos.writeObject(req);
            }
        } catch (IOException e) {
            logger.error("id: {} Can't be written: {}", id, e.getMessage());
        }
    }
}
