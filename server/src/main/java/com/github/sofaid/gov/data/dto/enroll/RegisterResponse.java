package com.github.sofaid.gov.data.dto.enroll;

import com.github.sofaid.gov.data.enums.EnrollStatus;
import lombok.Data;

@Data
public class RegisterResponse {
    private EnrollStatus status;
    private String message;
    private String challenge;

    public RegisterResponse() {
    }

    public RegisterResponse(EnrollStatus status, String message, String challenge) {
        this.status = status;
        this.message = message;
        this.challenge = challenge;
    }

    public static RegisterResponse invalidParameters(String message){
        RegisterResponse response = new RegisterResponse();
        response.setStatus(EnrollStatus.InvalidRequest);
        response.setMessage(message);
        response.setChallenge("");
        return response;
    }
}
