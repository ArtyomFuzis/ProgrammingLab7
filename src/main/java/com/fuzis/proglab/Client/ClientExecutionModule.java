package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ClientExecutionData {
    private static final HashMap<Integer, Thread> handle_table = new HashMap<>();
    private static final HashMap<Integer, AppData.MessageData> order_table = new HashMap<>();

    public synchronized static void putOrder(AppData.MessageData msg) {
        synchronized (order_table) {
            order_table.put(msg.id(), msg);
        }
    }

    public static AppData.MessageData getOrder(int id) {
        synchronized (order_table) {
            var order = order_table.get(id);
            order_table.remove(id);
            return order;
        }
    }

    public static void putHandle(int id, Thread thread) {
        synchronized (handle_table) {
            handle_table.put(id, thread);
        }
    }

    public static Thread getHandle(Integer id) {
        synchronized (handle_table) {
            var handle = handle_table.get(id);
            order_table.remove(id);
            return handle;
        }
    }

    private static Integer last_id = 0;

    public static synchronized Integer getId() {
        return ++last_id;
    }
}

public class ClientExecutionModule {
    public static ClientLogger logger = ClientLogger.getInstance();
    public static ClientWritingModule write_module;
    public static ExecutorService serv = Executors.newCachedThreadPool();

    public static void start() {
        ClientReadingModule.start_reading(ClientExecutionModule::request_handle);
        write_module = new ClientWritingModule();
    }

    public static void request_handle(AppData.MessageData msg) {
        serv.execute(() -> {
            handle_msg(msg);
        });
    }


    public static void handle_msg(AppData.MessageData msg) {
        switch (msg.purpose()) {
            case Cmd:
                logger.warning("Unexpected message: " + msg.toString());
                break;
            case Update:

                break;
            case Response:
                Thread thr_return = ClientExecutionData.getHandle(msg.id());
                if (thr_return == null) {
                    logger.warning("Unexpected response message: " + msg.toString());
                } else {
                    ClientExecutionData.putOrder(msg);
                    synchronized (thr_return) {
                        thr_return.notify();
                    }
                }
                break;
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static boolean request_auth(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            var to_encode = password + AppData.salt;
            byte[] encodedhash = digest.digest(to_encode.getBytes(StandardCharsets.UTF_8));
            Integer id = ClientExecutionData.getId();
            ClientExecutionData.putHandle(id, Thread.currentThread());
            write_module.write(new AppData.MessageData(AppData.MsgStatus.Successful, id, new AppData.Command(AppData.CmdType.auth, new Object[]{username, bytesToHex(encodedhash)}), AppData.MsgPurpose.Cmd));
            try {
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait();
                }
            } catch (InterruptedException e) {
                logger.error("Interrupted");
            }
            var order = ClientExecutionData.getOrder(id);
            return order.status() == AppData.MsgStatus.Successful;
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.error("No hash algorithm");
            System.exit(2);
            return false;
        }
    }
}
