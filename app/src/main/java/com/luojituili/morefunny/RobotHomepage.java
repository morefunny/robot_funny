package com.luojituili.morefunny;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by sherlockhua on 2016/11/27.
 */

public class RobotHomepage extends Fragment implements ViewPager.OnPageChangeListener{

    private ViewPager vpager;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private FragmentAdapter mAdapter;
    private FragmentManager fManager;

    public RobotHomepage() {

    }

    public void setManager(FragmentManager manager) {
        fManager = manager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.robot_homepage, container, false);

        mAdapter = new FragmentAdapter(fManager);

        vpager = (ViewPager) view.findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.robot_top_menu);
        tabs.setViewPager(vpager);

        int fontSize = tabs.getTextSize();
        Log.e("tab", String.format("font-size:%d", fontSize));
        tabs.setTextSize((int)(fontSize*1.2));

        return view;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {

        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }
}