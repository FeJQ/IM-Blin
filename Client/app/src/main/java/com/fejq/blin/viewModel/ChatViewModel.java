package com.fejq.blin.viewModel;

import android.content.Context;
import android.util.Log;

import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.entity.ChatMessage;
import com.fejq.blin.model.entity.User;
import com.fejq.blin.model.message.Request;
import com.fejq.blin.view.activity.ChatActivity;
import com.fejq.blin.view.adapter.ChatMessageListViewAdapter;
import com.fejq.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatViewModel
{
    private ChatActivity context;
    private List<ChatMessage> chatMessageList;

    private ChatMessageListViewAdapter adapter;

    public ChatMessageListViewAdapter getAdapter()
    {
        return adapter;
    }

    public ChatViewModel(ChatActivity context)
    {
        this.context = context;
        chatMessageList = new ArrayList<>();

        adapter = new ChatMessageListViewAdapter(chatMessageList);
        User user = new User(Client.getInstance().getCurrentUserId(), Client.getInstance().getToken());
        Request request = new Request();
        request.getChatHistory(user, context.getChatId(), context.getChatType()).send((code, message, data) -> {
            if (code == 0)
            {
                JSONArray chatHistoryArray = data.getJSONArray("chatHistoryList");
                for (int i = 0; i < chatHistoryArray.length(); i++)
                {
                    JSONObject node = chatHistoryArray.getJSONObject(i);
                    int senderId = node.getInt("senderId");
                    String senderName = node.getString("senderName");
                    String content = node.getString("content");
                    long sendTime = node.getLong("sendTime");
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId.set(senderId);
                    chatMessage.senderName.set(senderName);
                    chatMessage.content.set(content);
                    chatMessage.time.set(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date(sendTime)));
                    chatMessage.sendTime.set(new Date(sendTime));
                    int type = Client.getInstance().getCurrentUserId() == senderId ? ChatMessage.SENDER : ChatMessage.RECEIVER;
                    chatMessage.type.set(type);
                    chatMessageList.add(chatMessage);
                    adapter.notifyDataSetChanged();
                }
            }
            else
            {
                ToastUtil.showShortToast(context, message);
            }
        });


    }
}
