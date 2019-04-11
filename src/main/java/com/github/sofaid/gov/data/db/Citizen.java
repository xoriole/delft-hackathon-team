package com.github.sofaid.gov.data.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "citizens")
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String placeOfBirth;
    private String passportPhoto;
    private String bsn;

    public Citizen(){}

    public Citizen(String firstname, String lastname, Date dateOfBirth, String placeOfBirth, String passportPhoto, String BSN) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.passportPhoto = passportPhoto;
        this.bsn = BSN;
    }
}

