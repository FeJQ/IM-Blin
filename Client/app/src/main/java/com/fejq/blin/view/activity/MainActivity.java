package com.fejq.blin.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.fejq.blin.R;
import com.fejq.blin.view.adapter.ViewPagerAdapter;
import com.fejq.blin.view.fragment.base.PagerFragment;
import com.fejq.blin.view.fragment.TabFragment;
import com.fejq.blin.view.fragment.TitleFragment;
import com.fejq.blin.viewModel.ChatPagerViewModel;

import java.nio.channels.NonWritableChannelException;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        titleFragment = (TitleFragment) fragmentManager.findFragmentById(R.id.main_top_fragment);
        tabFragment = new TabFragment();
        tabFragment.setOnTabClickListenser(tab -> {
            setCurrentItem(tab);
        });
        fragmentManager.beginTransaction().replace(R.id.main_bottom_fragment, tabFragment).commit();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter = new ViewPagerAdapter(fragmentManager));
        viewPager.addOnPageChangeListener(this);
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
        System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}