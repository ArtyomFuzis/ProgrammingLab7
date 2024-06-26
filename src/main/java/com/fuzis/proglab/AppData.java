package com.fuzis.proglab;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

public class AppData {
    public static final String salt = "ロシア語？`ёавыаэб.hu9u0gjbhteo;iboh;itesr455453";

    public static int PORT;
    public static InetAddress ADDRESS;

    public enum MsgStatus {
        Successful,
        Error
    }
    public enum UpdateType {
        Remove,
        Add,
        Clear,
        Update
    }

    public enum MsgPurpose {
        Cmd,
        Response,
        Update
    }

    public record Command(CmdType type, Object[] args) implements Serializable {
        @Serial
        private static final long serialVersionUID = 522L;
    }

    public enum CmdType {
        add,
        auth,
        getCollectionInfo,
        getAll,
        clear,
        remove,
        update
    }

    public record MessageData(MsgStatus status, Integer id, Object body, MsgPurpose purpose) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    public record AuthData(int id, String name, String password) implements Serializable {
        @Serial
        private static final long serialVersionUID = 123L;
    }
}
