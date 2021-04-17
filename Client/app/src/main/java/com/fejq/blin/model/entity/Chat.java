package com.fejq.blin.model.entity;

import android.media.Image;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.util.Date;

public class Chat
{
    public ObservableInt chatId=new ObservableInt();
    public ObservableField<Image> photo = new ObservableField<>();
    public ObservableField<String> chatType = new ObservableField<>();
    public ObservableField<String> chatName = new ObservableField<>();
    public ObservableField<String> lastMessage = new ObservableField<>();
    public ObservableField<Date> lastMessageTime = new ObservableField<>();
    public ObservableInt nonReadCount=new ObservableInt();
}
