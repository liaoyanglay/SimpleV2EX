package com.dizzylay.simplev2ex;

import android.graphics.Bitmap;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/6.
 */
public class RepliesItem {
    Bitmap avatar;
    String username;
    String replyTime;
    String replyContent;

    public RepliesItem(Bitmap avatar, String username, String replyTime, String replyContent) {
        this.avatar = avatar;
        this.username = username;
        this.replyTime = replyTime;
        this.replyContent = replyContent;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}