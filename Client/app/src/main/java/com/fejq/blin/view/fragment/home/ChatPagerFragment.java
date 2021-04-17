package com.fejq.blin.view.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fejq.blin.R;
import com.fejq.blin.view.activity.ChatActivity;
import com.fejq.blin.view.activity.MainActivity;
import com.fejq.blin.view.adapter.ChatListViewAdapter;
import com.fejq.blin.view.fragment.home.base.PagerFragment;
import com.fejq.blin.viewModel.ChatPagerViewModel;


public class ChatPagerFragment extends PagerFragment
{

    ChatPagerViewModel viewModel;


    public ChatPagerFragment()
    {
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
        viewModel = new ChatPagerViewModel((MainActivity) getActivity());

        // 设置适配器
        ListView listView=getActivity().findViewById(R.id.chat_list);
        listView.setAdapter(viewModel.getAdapter());

        // 点击消息列表,跳转到聊天界面
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent=new Intent(getActivity(),ChatActivity.class);
            int chatId = viewModel.getChatList().get(position).chatId.get();
            String chatType=viewModel.getChatList().get(position).chatType.get();
            String chatName=viewModel.getChatList().get(position).chatName.get();
            intent.putExtra("chatId",chatId);
            intent.putExtra("chatType",chatType);
            intent.putExtra("chatName",chatName);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_chat, container, false);
    }
}