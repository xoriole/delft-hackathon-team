package com.github.sofaid.gov.data.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "login_tokens")
public class LoginToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    private Long timestamp;
    private String userId;

    public LoginToken(){}

}

