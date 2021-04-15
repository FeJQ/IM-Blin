package model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class FriendRelationship
{
    @Getter
    @Setter
    private Integer friendRelationshipId;
    @Getter
    @Setter
    private Integer userId;
    @Getter
    @Setter
    private Integer friendId;
    @Getter
    @Setter
    private Date genneratedTime;
}
