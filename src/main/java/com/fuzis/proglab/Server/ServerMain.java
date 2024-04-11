/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */
package com.fuzis.proglab.Server;

import com.fuzis.proglab.AppData;
import com.fuzis.proglab.InteractiveInput;
import com.fuzis.proglab.Server.Collection.CharacterCollection;
import com.fuzis.proglab.Server.Collection.CharacterCollectionFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    public static void warn(String info) {
        logger.warn(info);
    }

    public static void feedback(String info) {
        logger.info(info);
    }

    public static void error(String info) {
        logger.error(info);
    }
    public static Runnable task;

    public static void main(String[] args) {
        if (args.length >= 1) CharacterCollectionFile.fileName = args[0];
        try {
            if (args.length >= 2) AppData.PORT = Integer.parseInt(args[1]);
            else AppData.PORT = 4352;
        }
        catch (NumberFormatException ex)
        {
            error("Port not found, redirect to 4352");
            AppData.PORT = 4352;
        }
        //Runtime.getRuntime().addShutdownHook(new Thread(() -> CharacterCollectionFile.getInstance().save(new CharacterCollection.AuthData("admin","",1)), "Shutdown"));

        boolean listening_exit = false;

        if(!ServerConnectionModule.init())
        {
            error("Unable to open server socket");
            System.exit(1);
        }
        task = ()->
        {
            ServerConnectionModule con = new ServerConnectionModule();
            if (!con.tryconnect())
            {
                error("Unable to open the socket");
                System.exit(1);
            }
            ServerExecutionModule exec = new ServerExecutionModule(con);
            var in_scan = new InteractiveInput.FakeScanner((a) -> {
                var res = exec.getNextInteractive();
                if (res == null) a.close();
                a.add(res);
            });
            Thread thr = new Thread(task);
            thr.start();
            exec.start_server(in_scan);
        };
        Thread thr = new Thread(task);
        thr.start();
        ServerExecutionModule main_exec = new ServerExecutionModule(null);
        main_exec.start_console();
    }
}