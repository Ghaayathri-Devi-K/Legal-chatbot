package com.lucario.lawgpt;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private int SENT_BY;

    public Message(String message, int SENT_BY){
        this.message = message;
        this.SENT_BY = SENT_BY;
    }

    public String getMessage() {
        return message;
    }

    public int sentBy(){
        return SENT_BY;
    }
}
