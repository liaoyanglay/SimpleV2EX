package com.dizzylay.simplev2ex;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/3.
 */
public class ListItem {
    private String avatar;
    private String title;
    private String nodeTitle;
    private String username;
    private String replies;
    private String url;

    public ListItem(String avatar, String title, String nodeTitle, String username, String
            replies, String url) {
        this.avatar = avatar;
        this.title = title;
        this.nodeTitle = nodeTitle;
        this.username = username;
        this.replies = replies;
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTitle() {
        return title;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getReplies() {
        return replies;
    }

    public String getUrl() {
        return url;
    }
}
