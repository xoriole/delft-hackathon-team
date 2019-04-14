package com.github.sofaid.app.models.remote;


import lombok.Data;

@Data
public class RegisterResponse {
    private String status;
    private String message;
    private String challenge;

    public RegisterResponse() {
    }

    public RegisterResponse(String status, String message, String challenge) {
        this.status = status;
        this.message = message;
        this.challenge = challenge;
    }

    public static RegisterResponse invalidParameters(String message){
        RegisterResponse response = new RegisterResponse();
        response.setStatus("FAILED");
        response.setMessage(message);
        response.setChallenge("");
        return response;
    }
}
