package com.github.sofaid.app.models.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

import jnr.ffi.annotations.In;

@Entity
public class Share {
    @Id
    private Long id;
    @NotNull
    private String share;
    @NotNull
    private Integer shareId;
    @NotNull
    private String userName;
    @Generated(hash = 381732129)
    public Share(Long id, @NotNull String share, @NotNull Integer shareId,
            @NotNull String userName) {
        this.id = id;
        this.share = share;
        this.shareId = shareId;
        this.userName = userName;
    }
    @Generated(hash = 1069092765)
    public Share() {
    }
    public String getShare() {
        return this.share;
    }
    public void setShare(String share) {
        this.share = share;
    }
    public int getShareId() {
        return this.shareId;
    }
    public void setShareId(int shareId) {
        this.shareId = shareId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setShareId(Integer shareId) {
        this.shareId = shareId;
    }
}
