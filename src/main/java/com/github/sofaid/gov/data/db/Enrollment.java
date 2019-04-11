package com.github.sofaid.gov.data.db;

import com.github.sofaid.gov.data.dto.enroll.EnrollRequest;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bsn;
    private String documentType;
    private String documentId;
    private String govMasterPublicKey;
    private String userMasterPublicKey;
    private String signature;
    private boolean reEnroll;
    private long createdTs;


    public static Enrollment fromRequest(EnrollRequest request){
        Enrollment enrollment = new Enrollment();
        return  null;
    }

}

