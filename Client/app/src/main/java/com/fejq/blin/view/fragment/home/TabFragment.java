package com.fejq.blin.view.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fejq.blin.R;


public class TabFragment extends Fragment implements View.OnClickListener
{
    private View rootView;

    private TextView txtTabMessage;
    private TextView txtTabFriend;
    private TextView txtTabMine;

    private OnTabClickListenser onTabClickListenser;

    private int defaultTab = 0;
    private int currentTab = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View root)
    {
        txtTabMessage = (TextView) root.findViewById(R.id.tab_text_message);
        txtTabMessage.setTag(Integer.valueOf(0));
        txtTabMessage.setOnClickListener(this);
        txtTabFriend = (TextView) root.findViewById(R.id.tab_text_friend);
        txtTabFriend.setTag(Integer.valueOf(1));
        txtTabFriend.setOnClickListener(this);
        txtTabMine = (TextView) root.findViewById(R.id.tab_text_mine);
        txtTabMine.setTag(Integer.valueOf(2));
        txtTabMine.setOnClickListener(this);
        setCurrentTab(defaultTab);
    }

    public interface OnTabClickListenser
    {
         void onTabClick(int tab);
    }

    @Override
    public void onClick(View v)
    {
        if (v != null && v.getTag() instanceof Integer)
        {
            final int tab = (Integer) v.getTag();
            if (tab != currentTab && onTabClickListenser != null)
            {
                setCurrentTab(tab);
                onTabClickListenser.onTabClick(tab);
            }
        }
    }

    public void setOnTabClickListenser(OnTabClickListenser listenser)
    {
        onTabClickListenser = listenser;
    }

    public void setCurrentTab(int tab)
    {
        if (txtTabMessage != null && txtTabFriend != null && txtTabMine != null)
        {
            currentTab = tab;
            txtTabMessage.setTextColor(getResources().getColor(R.color.bg_tab_unselect));
            txtTabFriend.setTextColor(getResources().getColor(R.color.bg_tab_unselect));
            txtTabMine.setTextColor(getResources().getColor(R.color.bg_tab_unselect));
            switch (tab)
            {
                case 0:
                    txtTabMessage.setTextColor(getResources().getColor(R.color.bg_tab_select));
                    break;
                case 1:
                    txtTabFriend.setTextColor(getResources().getColor(R.color.bg_tab_select));
                    break;
                case 2:
                    txtTabMine.setTextColor(getResources().getColor(R.color.bg_tab_select));
                    break;
                default:
                    break;
            }
        }
        else
        {
            //TabFragment在Activity的onResume之后才会onCreateView
            //setCurrentTab的时候控件还没初始化，存一下初始值在initView里再初始化
            defaultTab = tab;
        }
    }
}