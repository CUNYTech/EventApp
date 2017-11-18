package Model;

import java.util.Date;

/**
 * Created by napti on 11/13/2017.
 */

public class Show_Chat_Conversation_Data_Items {
    private String message;
    private String sender;
    private long messageTime;

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
}
