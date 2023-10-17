package com.youth4work.prepapp.network.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Samar on 9/6/2016.
 */
public class Message {

    public ArrayList<Message> getmArrListMessages() {
        return mArrListMessages;
    }

    @SerializedName("Conv")
    private ArrayList<Message> mArrListMessages;

    @SerializedName("AttachmentFileName")
    private String AttachmentFileName;
    @SerializedName("AttachmentUrl")
    private String AttachmentUrl;
    @SerializedName("Category")
    private String Category;
    @SerializedName("IsRead")
    private boolean IsRead;
    @SerializedName("Message")
    private String MessageBody;
    @SerializedName("MessageID")
    private String MessageID;
    @SerializedName("SenderContact")
    private String SenderContact;
    @SerializedName("SenderName")
    private String SenderName;
    @SerializedName("SenderResume")
    private String SenderResume;
    @SerializedName("SenderUserId")
    private String SenderUserId;
    @SerializedName("SenderUserName")
    private String SenderUserName;
    @SerializedName("SenderUserPic")
    private String SenderUserPic;
    @SerializedName("SenderUserType")
    private String SenderUserType;
    @SerializedName("SentDate")
    private String SentDate;
    @SerializedName("Via")
    private String Via;

    public String getAttachmentFileName() {
        return AttachmentFileName;
    }

    public String getAttachmentUrl() {
        return AttachmentUrl;
    }

    public String getCategory() {
        return Category;
    }

    public boolean isRead() {
        return IsRead;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getSenderContact() {
        return SenderContact;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getSenderResume() {
        return SenderResume;
    }

    public String getSenderUserId() {
        return SenderUserId;
    }

    public String getSenderUserName() {
        return SenderUserName;
    }

    public String getSenderUserPic() {
        return SenderUserPic;
    }

    public String getSenderUserType() {
        return SenderUserType;
    }

    public String getSentDate() {
        return SentDate;
    }

    public String getVia() {
        return Via;
    }

    public void setMessageID(String messageID){
        this.MessageID = messageID;
    }
    public void setMessageBody(String messageBody){
        this.MessageBody = messageBody;
    }
    public void setSenderUserId(String senderUserId){
        this.SenderUserId = senderUserId;
    }
    public void setSentDate(String sentDate){
        this.SentDate = sentDate;
    }
}
