package com.fejq.blin.viewModel;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.view.adapter.ChatListViewAdapter;

import java.util.List;

public class ChatPagerViewModel
{
    private Fragment context;
    ChatListViewAdapter chatListViewAdapter;
    List<Chat> chatList;
    public ChatPagerViewModel()
    {

    }

}
