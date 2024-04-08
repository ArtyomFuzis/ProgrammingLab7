package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ServerReadingModule {
    ServerConnectionModule con;
    public ServerReadingModule(ServerConnectionModule _con)
    {
        con = _con;
    }
    public static ForkJoinPool executorService = new ForkJoinPool();
    public  AppData.TransferData read()
    {
        RecursiveTask<AppData.TransferData> act = new RecursiveTask<AppData.TransferData>() {
            @Override
            protected AppData.TransferData compute() {
                return inner_read();
            }
        };
        executorService.submit(act);
        return act.join();
    }
    public  AppData.TransferData inner_read() {
        try {
            ArrayList<byte[]> arr = new ArrayList<>();
            int whole_length = 0;
            while (true) {
                int code = con.is.read();
                if (code == 1) {
                    int length = (con.is.read() + 256) % 256;
                    var buf = new byte[length];
                    con.is.read(buf);
                    arr.add(buf);
                    whole_length += length;
                } else if (code == 2) break;
            }
            byte[] res_arr = new byte[whole_length];
            int i = 0;
            for (var el : arr) {
                for (var el2 : el) {
                    res_arr[i++] = el2;
                }
            }
            return (AppData.TransferData) (new ObjectInputStream(new ByteArrayInputStream(res_arr))).readObject();
        } catch (IOException ex) {
            ServerConnectionModule.error("Connection reading error: " + ex.getLocalizedMessage());
        } catch (ClassNotFoundException ex) {
            ServerConnectionModule.error("Wrong data error: " + ex.getLocalizedMessage());
        }
        return null;
    }
}
