package com.github.sofaid.app.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by robik on 6/14/17.
 */

@Entity
public class Transaction {

    @Id
    private String id;
    @NotNull
    private String fromAddress;
    @NotNull
    private String toAddress;
    @NotNull
    private Integer toIdType;
    @NotNull
    private Integer type;
    @NotNull
    private Double amount;
    @NotNull
    private Double networkFeePercent;
    @NotNull
    private Double networkFeeAmount;
    @NotNull
    private Double serviceFeePercent;
    @NotNull
    private Double serviceFeeAmount;
    @NotNull
    private Double totalAmount;
    private String remarks;
    @NotNull
    private Date createdOn;
    private Date updatedOn;
    @NotNull
    private Integer status;
    private String transactionHash;


    @Generated(hash = 680176734)
    public Transaction(String id, @NotNull String fromAddress,
            @NotNull String toAddress, @NotNull Integer toIdType,
            @NotNull Integer type, @NotNull Double amount,
            @NotNull Double networkFeePercent, @NotNull Double networkFeeAmount,
            @NotNull Double serviceFeePercent, @NotNull Double serviceFeeAmount,
            @NotNull Double totalAmount, String remarks, @NotNull Date createdOn,
            Date updatedOn, @NotNull Integer status, String transactionHash) {
        this.id = id;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.toIdType = toIdType;
        this.type = type;
        this.amount = amount;
        this.networkFeePercent = networkFeePercent;
        this.networkFeeAmount = networkFeeAmount;
        this.serviceFeePercent = serviceFeePercent;
        this.serviceFeeAmount = serviceFeeAmount;
        this.totalAmount = totalAmount;
        this.remarks = remarks;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.status = status;
        this.transactionHash = transactionHash;
    }

    @Generated(hash = 750986268)
    public Transaction() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Integer getToIdType() {
        return toIdType;
    }

    public void setToIdType(Integer toIdType) {
        this.toIdType = toIdType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getNetworkFeePercent() {
        return networkFeePercent;
    }

    public void setNetworkFeePercent(Double networkFeePercent) {
        this.networkFeePercent = networkFeePercent;
    }

    public Double getServiceFeePercent() {
        return serviceFeePercent;
    }

    public void setServiceFeePercent(Double serviceFeePercent) {
        this.serviceFeePercent = serviceFeePercent;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Double getNetworkFeeAmount() {
        return networkFeeAmount;
    }

    public void setNetworkFeeAmount(Double networkFeeAmount) {
        this.networkFeeAmount = networkFeeAmount;
    }

    public Double getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public void setServiceFeeAmount(Double serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}