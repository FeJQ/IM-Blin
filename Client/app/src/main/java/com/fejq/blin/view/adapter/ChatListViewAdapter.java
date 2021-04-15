package com.fejq.blin.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.fejq.blin.R;
import com.fejq.blin.databinding.ChatListEntryBinding;
import com.fejq.blin.model.entity.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatListViewAdapter extends BaseAdapter
{

    public ChatListViewAdapter(List<Chat> chatList)
    {
        this.chatList = chatList;
    }

    //数据
    private List<Chat> chatList;

    public List<Chat> getList()
    {
        return chatList;
    }

    @Override
    public int getCount()
    {
        return chatList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return chatList.get(position);
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
        ChatListEntryBinding chatListEntryBinding;
        if (convertView == null)
        {
            //创建一个databinding
            chatListEntryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_list_entry, parent, false);
            //获取convertView
            convertView = chatListEntryBinding.getRoot();
        }
        else
        {
            //去除convertView中bangding的dataBinding
            chatListEntryBinding = DataBindingUtil.getBinding(convertView);
        }
        Chat chat = chatList.get(position);
        chatListEntryBinding.setChatEntity(chat);


        return convertView;
    }

    private class OnBtnClickListener implements View.OnClickListener
    {
        private int stats;//1,修改；2，删除
        private Chat chat;
        private int position;

        OnBtnClickListener(int stats, Chat chat)
        {
            this.stats = stats;
            this.chat = chat;
        }

        OnBtnClickListener(int stats, int position)
        {
            this.stats = stats;
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
            switch (stats)
            {
                case 1:
                    chat.entryName.set("修改后的名字");
                    break;
                case 2:
                    chatList.remove(position);
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}
