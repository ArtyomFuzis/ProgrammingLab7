package com.fuzis.proglab.Exception;

public class DataBaseConnectionFailedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Unable to connect to database: " + super.getMessage();
    }
    public DataBaseConnectionFailedException(String msg) {super(msg);}
}
