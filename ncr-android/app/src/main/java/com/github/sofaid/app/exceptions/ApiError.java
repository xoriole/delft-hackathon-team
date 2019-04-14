package com.github.sofaid.app.exceptions;

import java.util.List;

public class ApiError {

    private String status;
    private String code;
    private List<String> messages;
    private Boolean userReadableMessage;

    public ApiError() {
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getUserReadableMessage() {
        return userReadableMessage;
    }

    public void setUserReadableMessage(Boolean userReadableMessage) {
        this.userReadableMessage = userReadableMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}