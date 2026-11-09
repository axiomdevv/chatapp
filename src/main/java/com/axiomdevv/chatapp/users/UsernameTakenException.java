package com.axiomdevv.chatapp.users;

import com.axiomdevv.chatapp.common.error.ConflictException;

public class UsernameTakenException extends ConflictException {

    public UsernameTakenException(String username) {
        super("Username " + username +" is already taken");
    }
}
