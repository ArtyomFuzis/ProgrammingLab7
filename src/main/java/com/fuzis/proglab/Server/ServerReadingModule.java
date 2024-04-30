package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ServerReadingModule {
    public static final Logger logger = LoggerFactory.getLogger(ServerReadingModule.class);
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void read(ObjectInputStream ois, BiConsumer<AppData.MessageData,Integer> callback,int id) {
        executorService.submit(()->{inner_read(ois,callback,id);});
    }

    public static void inner_read(ObjectInputStream ois, BiConsumer<AppData.MessageData,Integer> callback, int id) {
        try {
            callback.accept((AppData.MessageData) ois.readObject(),id);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("id: {} Can't be read: {}", id, e.getMessage());
        }
    }
}
