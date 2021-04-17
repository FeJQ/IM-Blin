package model.sql;

import lombok.Getter;
import lombok.Setter;

public class ChatEntry
{
    @Getter
    @Setter
    private int chatId;
    @Getter
    @Setter
    private int userId;
    @Getter
    @Setter
    private int entryId;
    @Getter
    @Setter
    private String entryType;

    //***********
    @Getter
    @Setter
    private String entryName;
}
