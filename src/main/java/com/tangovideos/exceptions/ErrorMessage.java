package com.tangovideos.exceptions;


import javax.ws.rs.NotFoundException;


public class ErrorMessage {
    int status = 401;
    int code;
    String message;
    private String name;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ErrorMessage(Exception ex) {
        this.setName(ex.getClass().getName());
        this.setMessage(ex.getMessage());
    }




    public ErrorMessage(NotFoundException ex){
        this.status = 404;
        this.message = "Page not found";
    }

    public ErrorMessage() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
