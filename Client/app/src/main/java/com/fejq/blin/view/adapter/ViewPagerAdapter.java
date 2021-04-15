package com.fejq.blin.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fejq.blin.view.fragment.FriendPagerFragment;
import com.fejq.blin.view.fragment.ChatPagerFragment;
import com.fejq.blin.view.fragment.MinePagerFragment;
import com.fejq.blin.view.fragment.base.PagerFragment;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<PagerFragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
        fragments.add(new ChatPagerFragment());
        fragments.add(new FriendPagerFragment());
        fragments.add(new MinePagerFragment());
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }
}
