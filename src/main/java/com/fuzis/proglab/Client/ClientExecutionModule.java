package com.fuzis.proglab.Client;

import com.fuzis.proglab.AppData;

public class ClientExecutionModule {
    public static ClientReadingModule read_module;
    public static ClientWritingModule write_module;
    public static void start()
    {
        read_module = new ClientReadingModule();
        write_module = new ClientWritingModule();
        write_module.write(new AppData.MessageData(AppData.MsgStatus.Successful,"Hello", AppData.MsgPurpose.Response));
        write_module.write(new AppData.MessageData(AppData.MsgStatus.Successful,"Hello2", AppData.MsgPurpose.Response));
        write_module.write(new AppData.MessageData(AppData.MsgStatus.Successful,"Hello3", AppData.MsgPurpose.Response));
    }
}
