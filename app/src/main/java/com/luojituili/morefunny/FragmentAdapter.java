package com.luojituili.morefunny;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by sherlockhua on 2016/10/30.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private JokePage jokePage = null;
    private TextPage textPage = null;
    private PicturePage picPage = null;
    private DiscoveryPage disPage = null;


    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        jokePage = new JokePage();
        textPage = new TextPage();
        picPage = new PicturePage();
        disPage = new DiscoveryPage();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
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
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = jokePage;
                break;
            case MainActivity.PAGE_TWO:
                fragment = textPage;
                break;
            case MainActivity.PAGE_THREE:
                fragment = picPage;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = disPage;
                break;
        }
        return fragment;
    }


}
