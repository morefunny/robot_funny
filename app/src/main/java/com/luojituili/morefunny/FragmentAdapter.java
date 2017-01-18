package com.luojituili.morefunny;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import api.RCategory;

/**
 * Created by sherlockhua on 2016/10/30.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private ArrayList<JokePage> _jokePageList = new ArrayList<JokePage>();


    public void setCategory(ArrayList<RCategory> data){

        Log.e("count", String.format("%d", data.size()));
        if (data.size() == 0) {
            return;
        }

        _jokePageList.clear();
        for (int i = 0; i< data.size(); i++) {
            RCategory cat = data.get(i);
            Log.e("categoryId", String.format("%d", cat.GetCategoryId()));

            JokePage jokePage = new JokePage();
            jokePage.setCategoryId(cat.GetCategoryId());
            jokePage.setCategoryName(cat.GetCategoryName());
            _jokePageList.add(jokePage);
        }

        notifyDataSetChanged();
    }

    public FragmentAdapter(FragmentManager fm) {

        super(fm);
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
        return  _jokePageList.get(position);
    }
}
