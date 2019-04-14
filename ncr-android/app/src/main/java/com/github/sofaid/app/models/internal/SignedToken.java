package com.github.sofaid.app.models.internal;

/**
 * Created by robik on 6/16/17.
 */

public class SignedToken {
    private String token;
    private long timestamp;
    private byte v;
    private String r;
    private String s;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getV() {
        return v;
    }

    public void setV(byte v) {
        this.v = v;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "SignedToken{" +
                "token='" + token + '\'' +
                ", timestamp=" + timestamp +
                ", v=" + v +
                ", r='" + r + '\'' +
                ", s='" + s + '\'' +
                '}';
    }
}
