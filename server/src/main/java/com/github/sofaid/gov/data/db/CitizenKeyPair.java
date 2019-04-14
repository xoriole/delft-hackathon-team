package com.github.sofaid.gov.data.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "citizen_key_pairs")
public class CitizenKeyPair {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dbId;

    // Citizen identifier
    private String bsn;

    // Key pairs for the citizen
    private String userMasterPublicKey;

    // Enrollment index: how many times user enrolled
    private boolean active;

    // Timestamps
    private Long createdTs;
    private Long updatedTs;
}
