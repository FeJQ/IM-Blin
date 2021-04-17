package com.fejq.blin.viewModel;


import android.content.Context;
import android.util.Log;

import com.fejq.blin.R;
import com.fejq.blin.database.DBHelper;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.entity.ChatMessage;
import com.fejq.blin.model.entity.User;
import com.fejq.blin.model.message.Request;
import com.fejq.blin.view.activity.MainActivity;
import com.fejq.blin.view.adapter.ChatListViewAdapter;
import com.fejq.blin.view.adapter.ViewPagerAdapter;
import com.fejq.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatPagerViewModel
{
    private MainActivity context;

    private List<Chat> chatList;

    public List<Chat> getChatList()
    {
        return chatList;
    }

    private ChatListViewAdapter adapter;

    public ChatListViewAdapter getAdapter()
    {
        return adapter;
    }

    public ChatPagerViewModel(MainActivity context)
    {
        this.context = context;
        chatList = new ArrayList<>();
        adapter = new ChatListViewAdapter(chatList);

        context.findViewById(R.id.btn).setOnClickListener(v -> {
            Chat chat = new Chat();
            chat.chatName.set("添加的");
            chatList.add(chat);
            adapter.notifyDataSetChanged();
            Log.i("add", chatList.size() + "");
        });

        User user = new User();
        user.setUserId(Client.getInstance().getCurrentUserId());
        user.setToken(Client.getInstance().getToken());
        Request request = new Request();
        Log.i("chatListSize", chatList.size() + "");
        request.getChatList(user).send((code, message, data) -> {
            if (code == 0)
            {
                Log.i("chatListResult", data.toString());
                JSONArray chatArray = data.getJSONArray("chatList");
                for (int i = 0; i < chatArray.length(); i++)
                {
                    JSONObject node = chatArray.getJSONObject(i);
                    int chatId = node.getInt("chatId");
                    String chatType = node.getString("chatType");
                    String chatName = node.getString("chatName");
                    String lastMessage = node.getString("lastMessage");
                    long lastMessageTime = node.getLong("lastMessageTime");
                    Chat chat = new Chat();
                    chat.chatId.set(chatId);
                    chat.chatType.set(chatType);
                    chat.chatName.set(chatName);
                    chat.lastMessage.set(lastMessage);
                    chat.lastMessageTime.set(new Date(lastMessageTime));
                    chatList.add(chat);
                    Collections.sort(chatList, (Chat c1, Chat c2) -> c1.lastMessageTime.get().compareTo(c2.lastMessageTime.get()));
                    adapter.notifyDataSetChanged();

                    Log.i("chatListSize", chatList.size() + "");
                }
            }
            else
            {
                ToastUtil.showShortToast(context, message);
                Log.i("chatListResult", data.toString());
            }
        });
    }


}
