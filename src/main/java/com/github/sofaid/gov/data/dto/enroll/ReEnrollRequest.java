package com.github.sofaid.gov.data.dto.enroll;

import lombok.Data;

@Data
public class ReEnrollRequest {

    private String bsn;
    private String masterPublicKey;
    private String signature;

}
