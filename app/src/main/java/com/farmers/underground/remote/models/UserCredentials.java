package com.farmers.underground.remote.models;

import android.support.annotation.NonNull;

/**
 * Created by tZpace
 * on 29-Sep-15.
 */
public class UserCredentials {

    private String email;

    private String pass;

    public UserCredentials(@NonNull String email,@NonNull String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
