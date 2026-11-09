package com.axiomdevv.chatapp.common.error;

public abstract class ConflictException extends RuntimeException{
    protected  ConflictException (String message){
        super(message);
    }
}
