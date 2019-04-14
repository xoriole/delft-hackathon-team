package com.github.sofaid.app.models.remote;

/**
 * Created by robik on 6/17/17.
 */
public class AccountPublicInfo {
    private String ncrAddress;
    private String username;

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
}
