package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.fejq.blin.R;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.entity.Chat;
import com.fejq.blin.model.message.MessageTask;
import com.fejq.blin.view.adapter.ViewPagerAdapter;
import com.fejq.blin.view.fragment.home.ChatPagerFragment;
import com.fejq.blin.view.fragment.home.base.PagerFragment;
import com.fejq.blin.view.fragment.home.TabFragment;
import com.fejq.blin.view.fragment.home.TitleFragment;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{

    private static final String TAG = "MainActivity";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TitleFragment titleFragment;
    private TabFragment tabFragment;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    //默认页面
    private static final int DEFAULT_PAGE = 0;
    // 页面索引
    private static final int CHAT_PAGE_INDEX=0;
    private static final int FRIEND_PAGE_INDEX=1;
    private static final int MINE_PAGE_INDEX=2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 登录成功后才会跳转到该Activity
        MessageTask.setOnRecvFriendMessageListener((uuid, status) -> {
            int code=status.getInt("code");
            String message = status.getString("message");
            JSONObject data = status.getJSONObject("data");
            int senderId = data.getInt("senderId");
            int receiverId=data.getInt("receiverId");
            String content = data.getString("content");
            Date sendTime=new Date(data.getLong("sendTime"));
            String senderName=data.getString("senderName");

            ChatPagerFragment chatPagerFragment = (ChatPagerFragment)adapter.getItem(CHAT_PAGE_INDEX);
            List<Chat> chatList = chatPagerFragment.getViewModel().getChatList();
            for (int i = 0; i <chatList.size() ; i++)
            {
                Chat chat = chatList.get(i);
                if(chat.chatId.get()== senderId || chat.chatId.get()==receiverId)
                {
                    // 该聊天项已存在,更新信息
                    chat.lastMessage.set(content);
                    chat.lastMessageTime.set(sendTime);
                    return;
                }
            }
            // 聊天项不存在,创建之
            Chat chat=new Chat();
            chat.chatName.set(senderName);
            if(Client.getInstance().getCurrentUserId()==senderId)
            {
                chat.chatId.set(receiverId);
            }
            else
            {
                chat.chatId.set(senderId);
            }
            chat.lastMessage.set(content);
            chat.lastMessageTime.set(sendTime);
            chatList.add(chat);
        });


        titleFragment = (TitleFragment) fragmentManager.findFragmentById(R.id.main_top_fragment);
        tabFragment = new TabFragment();
        tabFragment.setOnTabClickListenser(tab -> {
            setCurrentItem(tab);
        });
        fragmentManager.beginTransaction().replace(R.id.main_bottom_fragment, tabFragment).commit();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter = new ViewPagerAdapter(fragmentManager));
        viewPager.addOnPageChangeListener(this);


        // 给ViewPager设置缓存界面数 有几页缓存几页
        viewPager.setOffscreenPageLimit(3);
        setCurrentItem(DEFAULT_PAGE);
    }

    @Override
    public void onPageSelected(int position)
    {
        setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }


    private void setCurrentItem(int item)
    {
        if (item == viewPager.getCurrentItem())
        {
            //此时是源于initView或onPageSelected的调用
            notifyPageChangeToFragments(item);
        }
        else
        {
            //此时是源于initView或onTabClick的调用，后续会自动触发一次onPageSelected
            viewPager.setCurrentItem(item);
        }
    }

    /**
     * 通知其他fragment,页面切换了
     *
     * @param item 页面索引
     */
    private void notifyPageChangeToFragments(int item)
    {
        for (int page = 0; page != adapter.getCount(); ++page)
        {
            final Fragment fragment = adapter.getItem(page);
            if (fragment instanceof PagerFragment)
            {
                if (page == item)
                {
                    ((PagerFragment) fragment).onPageIn();
                }
                else
                {
                    ((PagerFragment) fragment).onPageOut();
                }
            }
        }
        titleFragment.setCurrentTab(item);
        tabFragment.setCurrentTab(item);
    }

    // Used to load the 'native-lib' library on application startup.
    static
    {
        //System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();
}