package com.github.sofaid.gov.data.dto.enroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String loginToken;
    private String loggerId;
    private String delegatorId;
    private int delegatorKeyIndex;
    private String loggerSignature;
    private String delegatorSignature;
    private Long timestamp;

}