package com.axiomdevv.chatapp.users;

import com.axiomdevv.chatapp.common.error.ConflictException;

public class EmailTakenException extends ConflictException {

    public EmailTakenException(String email) {
        super("Email "+ email +" is already taken");
    }
}
