package com.github.sofaid.app.models.remote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String blockchainId;
    private String bsn;
    private String masterPublicKey;
    private String signature;
    private Long timestamp;

}