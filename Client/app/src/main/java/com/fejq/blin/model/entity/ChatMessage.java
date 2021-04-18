package com.fejq.blin.model.entity;

import android.provider.ContactsContract;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableLong;

import java.util.Date;

public class ChatMessage
{
    public enum ChatMessageStatus
    {
        SENDING,
        SEND_FAILED,
        SEND_SUCCESSFUL
    }
    public static final int SENDER=0;
    public static final int RECEIVER=1;

    public ObservableInt senderId=new ObservableInt();
    public ObservableField<String> senderName =new ObservableField();
    public ObservableField<String> content=new ObservableField();
    public ObservableField<String> time=new ObservableField();
    public ObservableField<Date> sendTime=new ObservableField();
    public ObservableInt type=new ObservableInt();
    public ObservableField<ChatMessageStatus> status=new ObservableField(ChatMessageStatus.SEND_SUCCESSFUL);
}
