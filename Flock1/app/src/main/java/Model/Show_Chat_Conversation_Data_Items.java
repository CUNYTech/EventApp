package Model;

import java.util.Date;



public class Show_Chat_Conversation_Data_Items {
    private String message;
    private String sender;
    private long messageTime;
    private String userImage;
    private String email;

    public Show_Chat_Conversation_Data_Items()
    {
    }

    public Show_Chat_Conversation_Data_Items(String message, String sender) {
        this.message = message;
        this.sender = sender;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
