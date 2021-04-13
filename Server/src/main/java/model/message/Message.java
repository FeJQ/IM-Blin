package model.message;

import service.Status;

public abstract class Message
{
    private String uuid;

    public Message(String uuid)
    {
        this.uuid=uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public abstract String handle();
}
