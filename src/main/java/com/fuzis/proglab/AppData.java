package com.fuzis.proglab;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

public class AppData {
    public static int PORT;
    public static InetAddress ADDRESS;
    public enum MsgStatus
    {
        Successful,
        Error
    }
    public enum MsgPurpose
    {
        Cmd,
        Response,
        Update
    }

    public record MessageData(MsgStatus status, Object body,MsgPurpose purpose) implements Serializable
    {
        @Serial
        private static final long serialVersionUID = 1L;
    }
    public record AuthData(int id, String name, String password) implements Serializable
    {
        @Serial
        private static final long serialVersionUID = 123L;
    }
}
