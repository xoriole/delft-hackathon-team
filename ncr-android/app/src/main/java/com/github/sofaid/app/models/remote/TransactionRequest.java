package com.github.sofaid.app.models.remote;


import com.github.sofaid.app.models.internal.SignedToken;
import com.github.sofaid.app.models.internal.TransactionAmount;

/**
 * Created by robik on 6/10/17.
 */
public class TransactionRequest {

    private String referenceId;
    private String fromAddress;
    private String toAddress;
    private Integer toIdType;
    private Integer type;
    private TransactionAmount transactionAmount;
    private SignedToken signedToken;
    private String remarks;
    private String successRedirectUrl;
    private String failureRedirectUrl;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSuccessRedirectUrl() {
        return successRedirectUrl;
    }

    public void setSuccessRedirectUrl(String successRedirectUrl) {
        this.successRedirectUrl = successRedirectUrl;
    }

    public String getFailureRedirectUrl() {
        return failureRedirectUrl;
    }

    public void setFailureRedirectUrl(String failureRedirectUrl) {
        this.failureRedirectUrl = failureRedirectUrl;
    }

    public Integer getToIdType() {
        return toIdType;
    }

    public void setToIdType(Integer toIdType) {
        this.toIdType = toIdType;
    }

    public SignedToken getSignedToken() {
        return signedToken;
    }

    public void setSignedToken(SignedToken signedToken) {
        this.signedToken = signedToken;
    }

    public TransactionAmount getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(TransactionAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRequest that = (TransactionRequest) o;

        if (!referenceId.equals(that.referenceId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return referenceId.hashCode();
    }
}
