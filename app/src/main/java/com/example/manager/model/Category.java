package com.example.manager.model;

import java.util.List;

public class Category {
    boolean succes;
    String message;

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

    public List<CategoryModel> getResult() {
        return result;
    }

    public void setResult(List<CategoryModel> result) {
        this.result = result;
    }

    List<CategoryModel> result;

}
