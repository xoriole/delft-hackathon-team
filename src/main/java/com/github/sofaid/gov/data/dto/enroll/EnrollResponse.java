package com.github.sofaid.gov.data.dto.enroll;

import com.github.sofaid.gov.data.enums.EnrollStatus;
import lombok.Data;

@Data
public class EnrollResponse {
    private EnrollStatus status;
    private String message;
    private String challenge;

    public static EnrollResponse invalidParameters(String message){
        EnrollResponse response = new EnrollResponse();
        response.setStatus(EnrollStatus.InvalidRequest);
        response.setMessage(message);
        response.setChallenge("");
        return response;
    }
}
