package model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class UserInfo
{
    @Getter
    @Setter
    private Integer userId;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private Date tokenFailureTime;
    @Getter
    @Setter
    private Date registerTime;
    @Getter
    @Setter
    private Date lastLoginTime;
    @Getter
    @Setter
    private String lastLoginIP;
    @Getter
    @Setter
    private Integer lastLoginPort;
    @Getter
    @Setter
    private Boolean isOnline;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    private String email;
}
