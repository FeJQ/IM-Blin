package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.fejq.blin.R;
import com.fejq.blin.databinding.ActivityLoginBinding;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.entity.ChatMessage;
import com.fejq.blin.model.message.MessageTask;
import com.fejq.blin.view.adapter.ChatMessageListViewAdapter;
import com.fejq.blin.view.fragment.chat.ChatInputFragment;
import com.fejq.blin.view.fragment.chat.ChatTitleFragment;
import com.fejq.blin.viewModel.ChatViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity
{
    private ChatViewModel viewModel;
    public ChatViewModel getViewModel()
    {
        return viewModel;
    }

    private ChatTitleFragment titleFragment;
    private ChatInputFragment inputFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 联系人或群组Id
    private int chatId;
    public int getChatId()
    {
        return chatId;
    }

    // "联系人" or "群组"
    private String chatType;
    public String getChatType()
    {
        return chatType;
    }

    // 联系人或群组名称
    private String chatName;

    // 消息列表数据源
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    public void updateList(List<ChatMessage> list)
    {
        chatMessageList.clear();
        chatMessageList.addAll(list);
        Message message=new Message();
        message.what=UPDATE_LIST;
        handler.sendMessage(message);
    }

    private final int UPDATE_LIST = 1;
    // Handler
    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UPDATE_LIST:
                    viewModel.getAdapter().notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // 获取传过来的chatId
        chatId = getIntent().getIntExtra("chatId", 0);
        chatType = getIntent().getStringExtra("chatType");
        chatName = getIntent().getStringExtra("chatName");
        if (chatId == 0)
        {
            onDestroy();
        }
        viewModel = new ChatViewModel(this);
        titleFragment = (ChatTitleFragment) fragmentManager.findFragmentById(R.id.chat_title_fragment);
        inputFragment = (ChatInputFragment) fragmentManager.findFragmentById(R.id.chat_input_fragment);

        // 设置适配器
        ListView listView =  findViewById(R.id.chat_message_list);
        listView.setAdapter(viewModel.getAdapter());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        MessageTask.getInstance().setOnRecvFriendMessageListener((uuid,status)->{
            Log.i("Blin","ChatActivity 收到好友消息" );
            int code = status.getInt("code");
            String message = status.getString("message");
            JSONObject data = status.getJSONObject("data");
            int senderId = data.getInt("senderId");
            int receiverId = data.getInt("receiverId");
            String content = data.getString("content");
            Date sendTime = new Date(data.getLong("sendTime"));
            String senderName = data.getString("senderName");
            List<ChatMessage> chatMessageList = viewModel.getChatMessageList();
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.senderId.set(senderId);
            chatMessage.senderName.set(senderName);
            chatMessage.content.set(content);
            chatMessage.time.set(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(sendTime));
            chatMessage.sendTime.set(sendTime);
            int type = Client.getInstance().getCurrentUserId() == senderId ? ChatMessage.SENDER : ChatMessage.RECEIVER;
            chatMessage.type.set(type);
            chatMessageList.add(chatMessage);
            updateList(chatMessageList);
        });
    }
}