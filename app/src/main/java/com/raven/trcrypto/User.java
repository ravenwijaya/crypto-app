package com.raven.trcrypto;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class User {
    private String fullname;
    private String email;
    private String password;
    private String walletid;

    public User() {

    }
    public User(String fullname, String email, String password, String walletid) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.walletid = walletid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }
}