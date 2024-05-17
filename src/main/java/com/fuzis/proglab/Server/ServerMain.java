/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */
package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.Server.Collection.CharacterCollectionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    public static Runnable task;

    public static void main(String[] args) {
        try {
            if (args.length >= 1) AppData.PORT = Integer.parseInt(args[0]);
            else AppData.PORT = 4352;
        } catch (NumberFormatException ex) {
            logger.error("Port not found, redirect to 4352");
            AppData.PORT = 4352;
        }

        if (!ServerConnectionModule.init()) {
            logger.error("Unable to open server socket");
            System.exit(1);
        }
        var state_machine = StateMachine.get_instance();
        CharacterCollectionSQL.getInstance().load();
        if(state_machine.get(ServerData.admin_id) != null)
        {
            logger.error("Unable to register the administrator");
            System.exit(1);
        }
        state_machine.addAdmin();
        ServerConsole.start_console();
        while (true) {
            ServerConnectionModule con = new ServerConnectionModule();
            if (!con.tryconnect()) {
                logger.error("Unable to open the socket");
                System.exit(1);
            }
            var state = state_machine.add(con, null);
            if(state==null)
            {
                logger.error("Unable to make a connection");
            }
            else state.read();
        }

    }
}