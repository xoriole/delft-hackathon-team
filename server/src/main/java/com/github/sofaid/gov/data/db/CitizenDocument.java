package com.github.sofaid.gov.data.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "citizen_documents")
public class CitizenDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bsn;
    private String documentType;
    private String documentId;
    private String documentData;
    private boolean active;

    public CitizenDocument(){}

    public CitizenDocument(String bsn, String documentType, String documentId, String documentData, boolean active) {
        this.bsn = bsn;
        this.documentType = documentType;
        this.documentId = documentId;
        this.documentData = documentData;
        this.active = active;
    }
}

