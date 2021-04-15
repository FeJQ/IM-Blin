package com.fejq.blin.model.entity;

import android.media.Image;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.util.Date;

public class Chat
{
    public ObservableField<Image> photo = new ObservableField<>();
    public ObservableField<String> entryName = new ObservableField<>();
    public ObservableField<String> lastSenderName = new ObservableField<>();
    public ObservableField<String> lastMessage = new ObservableField<>();
    public ObservableField<Date> lastMessageTime = new ObservableField<>();
    public ObservableInt nonReadCount=new ObservableInt();
}
