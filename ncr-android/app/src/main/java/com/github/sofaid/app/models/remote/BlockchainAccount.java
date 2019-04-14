package com.github.sofaid.app.models.remote;

import java.math.BigInteger;

/**
 * Created by sandippandey on 20/06/2017.
 */

public class BlockchainAccount {

    private String address;
    private String id;
    private int category;
    private BigInteger balance;
    private boolean verified;
    private boolean enabled;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "BlockchainAccount{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", category=" + category +
                ", balance=" + balance +
                ", verified=" + verified +
                ", enabled=" + enabled +
                '}';
    }
}
