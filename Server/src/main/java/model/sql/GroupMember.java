package model.sql;

import lombok.Getter;
import lombok.Setter;

public class GroupMember
{
    @Getter
    @Setter
    private Integer groupMemberId;
    @Getter
    @Setter
    private Integer groupId;
    @Getter
    @Setter
    private Integer memberId;
    @Getter
    @Setter
    private Integer groupIdentityId;

}
