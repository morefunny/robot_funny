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

import java.io.FileNotFoundException;
import java.util.ArrayList;

import api.RCategory;
import api.ReceiveCategoryHandler;
import api.RobotApi;

/**
 * Created by sherlockhua on 2016/11/27.
 */

public class RobotHomepage extends Fragment implements ViewPager.OnPageChangeListener{

    private ViewPager vpager;

    private FragmentAdapter mAdapter;
    private FragmentManager fManager;
    private SimpleDiskCache _categoryCache;
    private String _categoryType;
    private RobotApi _robotApi = new RobotApi();

    public RobotHomepage() {

    }

    public void setCategoryType(String cateType) {
        _categoryType = cateType;
    }

    public String getCacheKey() {
        return String.format("category-%s", _categoryType);
    }

    public ArrayList<RCategory> getCategoryFromCache() throws Exception {

        ArrayList<RCategory> list  = _categoryCache.getArrayList(getCacheKey());
        return list;
    }

    private ReceiveCategoryHandler _handler = new ReceiveCategoryHandler() {
        public void onReceiveCategoryList(int code, ArrayList<RCategory> data){

            Log.e("code", String.format("%d", code));
            Log.e("count", String.format("%d", data.size()));

            if (code != 200) {
                try {
                    data = getCategoryFromCache();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (code == 200 && data.size() > 0) {
                try {
                    _categoryCache.putArrayList(getCacheKey(), data);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mAdapter.setCategory(data);
        }
    };

    public void setManager(FragmentManager manager) {
        fManager = manager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.robot_homepage, container, false);

        try {
            _categoryCache = SimpleDiskCache.getCache(this.getContext().getString(R.string.text_cache));
        }catch  (FileNotFoundException e) {
            e.printStackTrace();
        }

        _robotApi.getCategory(_categoryType, _handler);
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