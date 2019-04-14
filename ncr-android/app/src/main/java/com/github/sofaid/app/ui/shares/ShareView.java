package com.github.sofaid.app.ui.shares;

import com.github.sofaid.app.models.db.Share;

import java.util.ArrayList;
import java.util.List;

public class ShareView {

    private String emoji;
    private Integer shareId;
    private boolean active;
    private String share;

    public ShareView(String share, String emoji, Integer shareId, boolean active) {
        setShare(share);
        setActive(active);
        setEmoji(emoji);
        setShareId(shareId);
    }

    public ShareView() {
    }

    public static List<ShareView> convertFromShareList(List<Share> shareList){
        ArrayList<ShareView> list = new ArrayList<>(shareList.size());
        for (Share share :
                shareList) {
            list.add(new ShareView(share.getShare(),share.getUserName(), share.getShareId(),
                    true));

        }
        return list;
    }

    public String getEmoji() {
        return emoji;
    }

    public Integer getShareId() {
        return shareId;
    }

    public boolean isActive() {
        return active;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setShareId(Integer shareId) {
        this.shareId = shareId;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ShareView{" +
                "emoji='" + emoji + '\'' +
                ", shareId='" + shareId + '\'' +
                ", active=" + active +
                '}';
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
