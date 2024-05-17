package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

public class StateMachine
{
    public static final Logger logger = LoggerFactory.getLogger(StateMachine.class);
    public static class ConState
    {
        private final int id;
        private final ServerConnectionModule con;
        private AppData.AuthData auth;
        private final ObjectOutputStream oos;
        private final ObjectInputStream ois;
        public ConState (int _id,ServerConnectionModule _con, AppData.AuthData _auth)
        {
            id = _id;
            con = _con;
            auth = _auth;
            if(con==null)
            {
                oos = null;
                ois = null;
                return;
            }
            try {
                oos = new ObjectOutputStream(con.os);
                ois = new ObjectInputStream(con.is);
            }
            catch(IOException e)
            {
                logger.error("id: {} Connection problem: {}",id,e.getMessage());
                throw new RuntimeException(e);
            }
        }
        public AppData.AuthData getAuth ()
        {
            return auth;
        }
        public void setAuth(AppData.AuthData _auth)
        {
            auth = _auth;
        }
        public void write(AppData.MessageData msg)
        {
            ServerWritingModule.write(msg,oos,id);
        }
        public void read()
        {
            ServerReadingModule.read(ois,ServerExecutionModule::requestHandle,id);
        }
    }
    static private StateMachine _instance;
    public static StateMachine get_instance()
    {
        if(_instance == null)
        {
            _instance = new StateMachine();
        }
        return _instance;
    }
    private StateMachine()
    {
        storage = new HashMap<>();
    }
    private final HashMap<Integer,ConState> storage;
    public synchronized ConState get(int i)
    {
        return storage.get(i);
    }
    public synchronized Collection<ConState> get()
    {
        return storage.values();
    }
    int id_cnt = 1;
    public synchronized ConState add(ServerConnectionModule con, AppData.AuthData auth)
    {
        if(id_cnt == ServerData.admin_id) id_cnt++;
        try {
            var obj_add = new ConState(id_cnt, con, auth);
            storage.put(id_cnt++,obj_add);
            return obj_add;
        }
        catch(Exception e)
        {
            return null;
        }

    }
    public ConState addAdmin()
    {
        var obj_add = new ConState(ServerData.admin_id,null,new AppData.AuthData(1,"admin",""));
        storage.put(ServerData.admin_id,obj_add);
        return obj_add;
    }
}

