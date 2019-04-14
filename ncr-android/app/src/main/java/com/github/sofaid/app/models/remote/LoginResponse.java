package com.github.sofaid.app.models.remote;

import lombok.Data;

@Data
public class LoginResponse {
    private String status;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static LoginResponse invalidParameters(String message){
        LoginResponse response = new LoginResponse();
        response.setStatus("FAILED");
        response.setMessage(message);
        return response;
    }
}
