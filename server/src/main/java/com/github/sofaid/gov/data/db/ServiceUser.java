package com.github.sofaid.gov.data.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "service_users")
public class ServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String blockchainId;
    private String bsn;
    private String masterPublicKey;
    private String signature;

    private Long registerTs;
    private boolean active;

    public ServiceUser(){}

    public ServiceUser(String blockchainId, String bsn, String masterPublicKey, String signature, Long registerTs) {
        this.blockchainId = blockchainId;
        this.bsn = bsn;
        this.masterPublicKey = masterPublicKey;
        this.signature = signature;
        this.registerTs = registerTs;
        this.active = true;
    }
}

