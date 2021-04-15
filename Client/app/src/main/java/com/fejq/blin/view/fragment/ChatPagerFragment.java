package com.fejq.blin.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.fejq.blin.R;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.view.adapter.ChatListViewAdapter;
import com.fejq.blin.view.fragment.base.PagerFragment;
import com.fejq.blin.viewModel.ChatPagerViewModel;

import java.util.ArrayList;
import java.util.List;


public class ChatPagerFragment extends PagerFragment
{
    private List<Chat> chatList;
    ChatPagerViewModel viewModel;

    public ChatPagerFragment()
    {
        // Required empty public constructor
        viewModel = new ChatPagerViewModel();

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        chatList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            Chat chat = new Chat();
            chat.entryName.set("这是第"+(i+1)+"个标题");
            chat.lastMessage.set("---!!!+++");
            chat.lastSenderName.set("aaaaaa");
            chatList.add(chat);
        }
        ListView listView=(ListView)getActivity().findViewById(R.id.chat_list);
        Button button=(Button)getActivity().findViewById(R.id.button);
        button.setOnClickListener(v->{
            chatList.get(0).entryName.set("修改后的");
        });
        ChatListViewAdapter chatListViewAdapter = new ChatListViewAdapter(chatList);
        listView.setAdapter(chatListViewAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_chat, container, false);
    }
}