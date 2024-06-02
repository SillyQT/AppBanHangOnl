package com.example.manager.model;

import java.util.List;

public class User {
    boolean succes;
    String message;
    List<UserModel> result;

    public List<UserModel> getResult() {
        return result;
    }

    public void setResult(List<UserModel> result) {
        this.result = result;
    }

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
