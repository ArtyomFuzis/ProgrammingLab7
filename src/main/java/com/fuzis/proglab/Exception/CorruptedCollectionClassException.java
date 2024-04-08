package com.fuzis.proglab.Exception;

public class CorruptedCollectionClassException extends RuntimeException {
    public CorruptedCollectionClassException(String msg) {super(msg);}
    public String getMessage() {
        return "Unable to connect to create a collection: " + super.getMessage();
    }
}
