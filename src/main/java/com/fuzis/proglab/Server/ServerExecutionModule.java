package com.fuzis.proglab.Server;


import com.fuzis.proglab.AppData;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Exception.NoRootsException;
import com.fuzis.proglab.Server.Collection.CharacterCollectionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutionModule {
    public static final Logger logger = LoggerFactory.getLogger(ServerExecutionModule.class);
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    public static void auth(Integer msg_id, String user,String pass,int id )
    {
        System.out.println(user+" "+ pass);
        var state = StateMachine.get_instance().get(id);
        var con  = CharacterCollectionSQL.con;
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM auth WHERE username = ? and password = ?");
            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            if (rs.next())
            {
                state.setAuth(new AppData.AuthData(rs.getInt("id"),user,pass));
                logger.info("id: {} Successfully logged in: {} {}",id,user,pass);
                state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg_id,null, AppData.MsgPurpose.Response) );

            }
            else state.write(new AppData.MessageData(AppData.MsgStatus.Error,msg_id,null, AppData.MsgPurpose.Response) );

        }
        catch (SQLException e)
        {
            logger.error(e.getMessage());
        }
    }
    public static void commandHandle(AppData.MessageData msg, int id)
    {

        var state = StateMachine.get_instance().get(id);
        var collection = CharacterCollectionSQL.getInstance();
        try {
            var cmd = (AppData.Command) msg.body();
            var cmd_type = cmd.type();
            var cmd_args = cmd.args();
            switch(cmd_type)
            {
                case add:
                    collection.add((String) cmd_args[0], (DefaultCartoonPersonCharacter) cmd_args[1], id);
                    state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg.id(),null, AppData.MsgPurpose.Response) );
                    break;
                case remove:
                    collection.deleteCharacter((String) cmd_args[0],id);
                    state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg.id(),null, AppData.MsgPurpose.Response) );
                    break;
                case clear:
                    collection.clear(id);
                    state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg.id(),null, AppData.MsgPurpose.Response) );
                    break;
                case getCollectionInfo:
                    state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg.id(),collection.getInfo(), AppData.MsgPurpose.Response) );
                    break;
                case getAll:
                    state.write(new AppData.MessageData(AppData.MsgStatus.Successful,msg.id(),collection.getCharacters(), AppData.MsgPurpose.Response) );
                    break;
                case auth:
                    auth(msg.id(),(String)cmd_args[0],(String)cmd_args[1],id);
                    break;
            }

        }
        catch (ClassCastException e)
        {
            logger.error("id: {} Command message body is corrupted: {}", id, e.toString());
            state.write(new AppData.MessageData(AppData.MsgStatus.Error,msg.id(),e, AppData.MsgPurpose.Response));
        }
        catch (NoRootsException e)
        {
            state.write(new AppData.MessageData(AppData.MsgStatus.Error,msg.id(),e, AppData.MsgPurpose.Response));
        }
    }
    public static void handle(AppData.MessageData msg, int id)
    {
        try {
            switch (msg.purpose()) {
                case Cmd:
                    commandHandle(msg, id);
                    break;
                case Update:
                case Response:
                    logger.warn("Unexpected message: {}", msg);
            }
        }
        catch (Exception e) {
            logger.error("id: {} Unexpected error: {}",id, e.toString());
        }
    }
    public static void requestHandle(AppData.MessageData msg, int id)
    {
        var state = StateMachine.get_instance().get(id);
        logger.info(id + ": " + msg.toString());
        executorService.submit(()->handle(msg, id));
        state.read();
    }
}
