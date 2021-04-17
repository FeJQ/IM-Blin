package com.fejq.blin.view.fragment.home;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fejq.blin.R;
import com.fejq.blin.view.fragment.home.base.PagerFragment;


public class MinePagerFragment extends PagerFragment
{

    public MinePagerFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_mine, container, false);
    }
}