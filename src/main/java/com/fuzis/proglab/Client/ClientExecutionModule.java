package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

public class ClientExecutionModule {
    public static ClientReadingModule read_module;
    public static ClientWritingModule write_module;
    public static void start()
    {
        read_module = new ClientReadingModule();
        write_module = new ClientWritingModule();
        write_module.write(new AppData.MessageData(AppData.MsgStatus.Successful,new AppData.Command(AppData.CmdType.auth,new Object[]{"admin",""}), AppData.MsgPurpose.Cmd));
        System.out.println(read_module.read());
    }
}
