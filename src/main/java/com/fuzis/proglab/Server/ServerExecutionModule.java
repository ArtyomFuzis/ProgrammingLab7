package com.fuzis.proglab.Server;


import com.fuzis.proglab.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerExecutionModule {
    public static final Logger logger = LoggerFactory.getLogger(ServerExecutionModule.class);


    public AppData.AuthData auth_data;
    /*public void auth(String user,String pass )
    {
        var con  = CharacterCollectionSQL.con;
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM auth WHERE username = ? and password = ?");
            System.out.println(user);
            System.out.println(pass);
            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            if (rs.next())
            {
                auth_data = new CharacterCollection.AuthData(user,pass,rs.getInt("id"));
                write_module.write(new AppData.Da(AppData.TransferPurpose.Auth,null,2,null));
            }
            else write_module.write(new AppData.TransferData(AppData.TransferPurpose.Auth,null,3,null));
        }
        catch (SQLException e)
        {
            error(e.getMessage());
            e.printStackTrace();
        }
    }*/

    public static void request_handle(AppData.MessageData msg, int id)
    {
        logger.info(msg.toString());
        var State = StateMachine.get_instance().get(id);
        State.read();
    }
}
