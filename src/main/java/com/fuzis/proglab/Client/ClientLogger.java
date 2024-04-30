package com.fuzis.proglab.Client;

public class ClientLogger {
    private static ClientLogger _instance;
    private ClientLogger()
    {

    }
    public static ClientLogger getInstance()
    {
        if(_instance == null)_instance = new ClientLogger();
        return _instance;
    }
    public void error(String msg)
    {
        System.out.println("[error] " + msg);
    }
    public void info(String msg)
    {
        System.out.println("[info] " + msg);
    }
    public void warning(String msg)
    {
        System.out.println("[warning] " + msg);
    }
}
