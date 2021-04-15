package model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class GroupInfo
{
    @Getter
    @Setter
    private Integer groupId;
    @Getter
    @Setter
    private String groupName;
    @Getter
    @Setter
    private Integer creatorId;
    @Getter
    @Setter
    private Date createdTime;
    @Getter
    @Setter
    private String description;

}
