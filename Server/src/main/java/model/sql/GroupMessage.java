package model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class GroupMessage
{
    @Getter
    @Setter
    private Integer groupMessageId;
    @Getter
    @Setter
    private Integer senderId;
    @Getter
    @Setter
    private Integer groupId;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private Integer messageTypeId;
    @Getter
    @Setter
    private Date sendTime;

}
