package com.dizzylay.simplev2ex.javabean;

import android.graphics.Bitmap;
import android.text.Spanned;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/6.
 */
public class RepliesItem {
    private Bitmap avatar;
    private String username;
    private String replyTime;
    private Spanned replyContent;

    public RepliesItem(Bitmap avatar, String username, String replyTime, Spanned replyContent) {
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

    public Spanned getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(Spanned replyContent) {
        this.replyContent = replyContent;
    }
}
