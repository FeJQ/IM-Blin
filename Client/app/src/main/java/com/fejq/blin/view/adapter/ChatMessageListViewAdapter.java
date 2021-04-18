package com.fejq.blin.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.fejq.blin.R;
import com.fejq.blin.databinding.ChatListEntryBinding;
import com.fejq.blin.databinding.ChatMessageListLeftEntryBinding;
import com.fejq.blin.databinding.ChatMessageListRightEntryBinding;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.entity.ChatMessage;

import java.util.List;

public class ChatMessageListViewAdapter extends BaseAdapter
{
    private List<ChatMessage> chatMessageList;

    public ChatMessageListViewAdapter(List<ChatMessage> chatMessageList)
    {
        this.chatMessageList = chatMessageList;
    }

    @Override
    public int getCount()
    {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return chatMessageList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //这个databinding也是根据布局文件item_mvvm而命名的
        ChatMessage chatMessage = chatMessageList.get(position);
        boolean left = chatMessage.type.get() == ChatMessage.RECEIVER ? true : false;
        final int layoutId = left ? R.layout.chat_message_list_left_entry : R.layout.chat_message_list_right_entry;

        if (convertView == null)
        {
            if (left)
            {
                // 对方发来的消息
                ChatMessageListLeftEntryBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
                convertView = viewDataBinding.getRoot();
                viewDataBinding.setChatMessageEntry(chatMessage);
            }
            else
            {
                // 我发送的消息
                ChatMessageListRightEntryBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
                convertView = viewDataBinding.getRoot();
                viewDataBinding.setChatMessageEntry(chatMessage);
            }
        }
        else
        {
            ViewDataBinding binding = DataBindingUtil.getBinding(convertView);
            if (binding instanceof ChatMessageListLeftEntryBinding)
            {
                ChatMessageListLeftEntryBinding viewDataBinding = (ChatMessageListLeftEntryBinding) binding;
                viewDataBinding.setChatMessageEntry(chatMessage);
            }
            else if (binding instanceof ChatMessageListRightEntryBinding)
            {
                ChatMessageListRightEntryBinding viewDataBinding = (ChatMessageListRightEntryBinding) binding;
                viewDataBinding.setChatMessageEntry(chatMessage);
            }
        }
        return convertView;
    }
}
