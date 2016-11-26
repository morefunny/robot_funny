package com.luojituili.morefunny;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    //UI Objects
    // private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton robot_rb_home;
    private RadioButton robot_rb_gif;
    private RadioButton robot_rb_favorite;
    private RadioButton robot_rb_setting;
    private ViewPager vpager;


    private FragmentAdapter mAdapter;
    private FragmentManager fManager;

    private JokePage _robotHomePage;
    private JokePage _robotGifPage;
    private FavoritePage _robotFavoritePage;
    private SettingPage _robotSettingPage;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fManager = getSupportFragmentManager();
        //mAdapter = new FragmentAdapter(fManager);
        bindViews();
        robot_rb_home.setChecked(true);

    }

    private void bindViews() {
        //txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        robot_rb_home = (RadioButton) findViewById(R.id.robot_rb_home);
        robot_rb_gif = (RadioButton) findViewById(R.id.robot_rb_gif);
        robot_rb_favorite = (RadioButton) findViewById(R.id.robot_rb_favorite);
        robot_rb_setting = (RadioButton) findViewById(R.id.robot_rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);
/*
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.robot_top_menu);
        tabs.setViewPager(vpager);*/
    }
/*
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.robot_rb_home:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.robot_rb_gif:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.robot_rb_favorite:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.robot_rb_setting:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }
*/
@Override
public void onCheckedChanged(RadioGroup group, int checkedId) {
    FragmentTransaction fTransaction = fManager.beginTransaction();
    hideAllFragment(fTransaction);
    switch (checkedId){
        case R.id.robot_rb_home:
            if(_robotHomePage == null){
                _robotHomePage = new JokePage();
                fTransaction.add(R.id.robot_bottom_page, _robotHomePage);
            }else{
                fTransaction.show(_robotHomePage);
            }
            break;
        case R.id.robot_rb_gif:
            if(_robotGifPage == null){
                _robotGifPage = new JokePage();
                fTransaction.add(R.id.robot_bottom_page,_robotGifPage);
            }else{
                fTransaction.show(_robotGifPage);
            }
            break;
        case R.id.robot_rb_favorite:
            if(_robotFavoritePage == null){
                _robotFavoritePage = new FavoritePage();
                fTransaction.add(R.id.robot_bottom_page,_robotFavoritePage);
            }else{
                fTransaction.show(_robotFavoritePage);
            }
            break;
        case R.id.robot_rb_setting:
            if(_robotSettingPage == null){
                _robotSettingPage = new SettingPage();
                fTransaction.add(R.id.robot_bottom_page,_robotSettingPage);
            }else{
                fTransaction.show(_robotSettingPage);
            }
            break;
    }
    fTransaction.commit();
}

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(_robotHomePage != null) {
            fragmentTransaction.hide(_robotHomePage);
        }

        if(_robotGifPage != null){
            fragmentTransaction.hide(_robotGifPage);
        }

        if(_robotFavoritePage != null){
            fragmentTransaction.hide(_robotFavoritePage);
        }

        if(_robotSettingPage != null){
            fragmentTransaction.hide(_robotSettingPage);
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    robot_rb_home.setChecked(true);
                    break;
                case PAGE_TWO:
                    robot_rb_gif.setChecked(true);
                    break;
                case PAGE_THREE:
                    robot_rb_favorite.setChecked(true);
                    break;
                case PAGE_FOUR:
                    robot_rb_setting.setChecked(true);
                    break;
            }
        }
    }
}