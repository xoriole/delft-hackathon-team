package com.github.sofaid.app.models.remote;

import com.github.sofaid.app.models.internal.SignedToken;

/**
 * Created by robik on 6/15/17.
 */

public class SignupRequest {

    private String ncrAddress;
    private String username;
    private String firstName;
    private String lastName;
    private Integer category;
    private String dateOfBirth;
    private Integer gender;
    private Long timestamp;
    private String mobileNumber;
    private String emailAddress;
    private SignedToken signedToken;

    public String getNcrAddress() {
        return ncrAddress;
    }

    public void setNcrAddress(String ncrAddress) {
        this.ncrAddress = ncrAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public SignedToken getSignedToken() {
        return signedToken;
    }

    public void setSignedToken(SignedToken signedToken) {
        this.signedToken = signedToken;
    }
}
