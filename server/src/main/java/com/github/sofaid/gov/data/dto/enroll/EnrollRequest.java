package com.github.sofaid.gov.data.dto.enroll;

import lombok.Data;

@Data
public class EnrollRequest {

    private String documentType;
    private String documentId;
    private String govMasterPublicKey;
    private String userMasterPublicKey;
    private String signature;
    private String publicKeyHash;
    public Long timestamp;
    private boolean reEnroll;

}
