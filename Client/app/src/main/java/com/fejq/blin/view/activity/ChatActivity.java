package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.ListView;

import com.fejq.blin.R;
import com.fejq.blin.view.adapter.ChatMessageListViewAdapter;
import com.fejq.blin.view.fragment.chat.ChatInputFragment;
import com.fejq.blin.view.fragment.chat.ChatTitleFragment;
import com.fejq.blin.viewModel.ChatViewModel;

public class ChatActivity extends AppCompatActivity
{
    private ChatViewModel viewModel;
    private ChatTitleFragment titleFragment;
    private ChatInputFragment inputFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private int chatId;
    public int getChatId()
    {
        return chatId;
    }
    private String chatType;
    public String getChatType(){return chatType;}
    private String chatName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取传过来的chatId

        chatId=getIntent().getIntExtra("chatId", 0);
        chatType= getIntent().getStringExtra("chatType");
        chatName=getIntent().getStringExtra("chatName");
        if (chatId == 0)
        {
            onDestroy();
        }
        viewModel = new ChatViewModel(this);
        titleFragment = (ChatTitleFragment) fragmentManager.findFragmentById(R.id.chat_title_fragment);
        inputFragment = (ChatInputFragment) fragmentManager.findFragmentById(R.id.chat_input_fragment);

        // 设置适配器
        ListView listView = (ListView) findViewById(R.id.chat_message_list);

        listView.setAdapter(viewModel.getAdapter());


    }
}