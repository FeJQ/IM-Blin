package model.sql;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public class FriendMessage
{
    @Getter
    @Setter
    private Integer friendMessageId;
    @Getter
    @Setter
    private Integer senderId;
    @Getter
    @Setter
    private Integer receiverId;
    @Getter
    @Setter
    private Integer messageTypeId;
    @Getter
    @Setter
    private Date sendTime;
    @Getter
    @Setter
    private Boolean haveRead;
    @Getter
    @Setter
    private String content;
    //---------------------
    @Getter
    @Setter
    private String senderName;

}
