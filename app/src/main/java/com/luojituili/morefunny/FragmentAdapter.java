package com.luojituili.morefunny;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import api.RCategory;
import api.ReceiveCategoryHandler;
import api.RobotApi;

/**
 * Created by sherlockhua on 2016/10/30.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private JokePage jokePage = null;
    private JokePage textPage = null;
    private JokePage picPage = null;
    private JokePage disPage = null;

    private ArrayList<JokePage> _jokePageList = new ArrayList<JokePage>();
    private RobotApi _robotApi = new RobotApi();

    private final String[] TITLES = {"推荐", "笑话", "段子", "图片", "美女", "推理",
            "Top New Free", "Trending"};


    private ReceiveCategoryHandler _handler = new ReceiveCategoryHandler() {
        public void onReceiveCategoryList(int code, ArrayList<RCategory> data){

            Log.e("code", String.format("%d", code));
            Log.e("count", String.format("%d", data.size()));
            for (int i = 0; i< data.size(); i++) {
                RCategory cat = data.get(i);
                Log.e("categoryId", String.format("%d", cat.GetCategoryId()));
                JokePage jokePage = new JokePage();
                jokePage.setCategoryId(cat.GetCategoryId());
                jokePage.setCategoryName(cat.GetCategoryName());
                _jokePageList.add(jokePage);
                notifyDataSetChanged();
            }

            if (code == 200) {
                //_adapter.appendThreadList(data);
            }

            //_swipeLayout.setRefreshing(false);
        }
    };

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        jokePage = new JokePage();
        textPage = new JokePage();
        picPage = new JokePage();
        disPage = new JokePage();

        _robotApi.getCategory(_handler);
    }


    @Override
    public int getCount() {
        return _jokePageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _jokePageList.get(position).getCategoryName();
    }
    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        if (_jokePageList.size() == 0) {
            return jokePage;
        }
        Fragment fragment = _jokePageList.get(position);
        /*
        switch (position) {
            case 0:
                fragment = jokePage;
                break;
            case 1:
                fragment = textPage;
                break;
            case 2:
                fragment = picPage;
                break;
            case 3:
                fragment = disPage;
                break;
        }*/
        return fragment;
    }


}
