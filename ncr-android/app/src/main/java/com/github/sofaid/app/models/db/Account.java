package com.github.sofaid.app.models.db;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by robik on 6/14/17.
 */

@Entity
public class Account {

    @Id
    private String ncrAddress;
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Integer category;
    @NotNull
    private Date dateOfBirth;
    private Integer gender;
    @NotNull
    private Long timestamp;
    @NotNull
    private String mobileNumber;
    @NotNull
    private String emailAddress;
    @NotNull
    @Expose(deserialize = false, serialize = false)
    private Boolean verified;

    @Generated(hash = 2115690879)
    public Account(String ncrAddress, @NotNull String username, @NotNull String firstName, @NotNull String lastName,
            @NotNull Integer category, @NotNull Date dateOfBirth, Integer gender, @NotNull Long timestamp,
            @NotNull String mobileNumber, @NotNull String emailAddress, @NotNull Boolean verified) {
        this.ncrAddress = ncrAddress;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.timestamp = timestamp;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.verified = verified;
    }

    @Generated(hash = 882125521)
    public Account() {
    }

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return getNcrAddress() != null ? getNcrAddress().equals(account.getNcrAddress()) : account.getNcrAddress() == null;

    }

    @Override
    public int hashCode() {
        return getNcrAddress() != null ? getNcrAddress().hashCode() : 0;
    }
}