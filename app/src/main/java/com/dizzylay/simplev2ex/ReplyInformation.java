package com.dizzylay.simplev2ex;

/**
 * Created by dizzylay on 2018/4/12.
 */
public class ReplyInformation {

    private String replyDetails;
    private String replyContent;
    private String url;

    public ReplyInformation(String replyDetails, String replyContent, String url) {
        this.replyDetails = replyDetails;
        this.replyContent = replyContent;
        this.url = url;
    }

    public String getReplyDetails() {
        return replyDetails;
    }

    public void setReplyDetails(String replyDetails) {
        this.replyDetails = replyDetails;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
