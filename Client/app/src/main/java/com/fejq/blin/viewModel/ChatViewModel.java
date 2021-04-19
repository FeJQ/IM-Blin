package com.fejq.blin.viewModel;

import android.content.Context;
import android.telephony.MbmsGroupCallSession;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.view.menu.MenuWrapperICS;

import com.fejq.blin.R;
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
import java.util.HashMap;
import java.util.List;

public class ChatViewModel
{
    // 上下文
    private ChatActivity context;
    // 数据
    private List<ChatMessage> chatMessageList;

    public List<ChatMessage> getChatMessageList()
    {
        return chatMessageList;
    }


    // 消息列表 listView 适配器
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

        // 获取历史消息
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

                    // 更新数据
                    chatMessageList.add(chatMessage);

                    // 更新界面
                    context.updateList(chatMessageList);

                }
            }
            else
            {
                ToastUtil.showShortToast(context, message);
            }
        });
    }

    /**
     * 发送新消息
     */
    public void onMessageSend()
    {
        EditText editTextContent = context.findViewById(R.id.edt_chat_content);
        String contentText = editTextContent.getText().toString();
        if (contentText.isEmpty()) return;

        int chatId = context.getChatId();
        String chatType = context.getChatType();

        User sender = new User(Client.getInstance().getCurrentUserId(), Client.getInstance().getToken());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderId.set(Client.getInstance().getCurrentUserId());
        chatMessage.senderName.set("我");
        chatMessage.content.set(contentText);
        Date date = new Date();
        chatMessage.time.set(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(date));
        chatMessage.sendTime.set(date);
        int type = ChatMessage.SENDER;
        chatMessage.type.set(type);

        Request request = new Request();

        // 给消息标识uuid
        chatMessage.uuid.set(request.getUuid());
        chatMessage.status.set(ChatMessage.ChatMessageStatus.SENDING);

        // 更新数据
        chatMessageList.add(chatMessage);
        // 更新界面
        context.updateList(chatMessageList);
        new Thread(() -> {
            ChatMessage temp = chatMessage;
            request.sendTextMessage(sender, chatId, contentText, chatType).send((code, message, data) -> {
                String uuid = data.getString("uuid");
                if (code == 0)
                {
                    if (temp.uuid.equals(uuid))
                    {
                        temp.status.set(ChatMessage.ChatMessageStatus.SEND_SUCCESSFUL);
                        context.updateList(chatMessageList);
                    }
                }
                else
                {
                    temp.status.set(ChatMessage.ChatMessageStatus.SEND_FAILED);
                    context.updateList(chatMessageList);
                }
            });
        }).start();

    }
}
