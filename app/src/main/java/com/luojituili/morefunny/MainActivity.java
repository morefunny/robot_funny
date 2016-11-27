package com.luojituili.morefunny;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    //UI Objects
    // private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton robot_rb_home;
    private RadioButton robot_rb_gif;
    private RadioButton robot_rb_favorite;
    private RadioButton robot_rb_setting;


    private FragmentAdapter mAdapter;
    private FragmentManager fManager;

    private RobotHomepage _robotHomePage;
    private RobotHomepage _robotGifPage;
    private FavoritePage _robotFavoritePage;
    private SettingPage _robotSettingPage;

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
    }

@Override
public void onCheckedChanged(RadioGroup group, int checkedId) {
    FragmentTransaction fTransaction = fManager.beginTransaction();
    hideAllFragment(fTransaction);
    switch (checkedId){
        case R.id.robot_rb_home:
            if(_robotHomePage == null){
                _robotHomePage = new RobotHomepage();
                _robotHomePage.setManager(fManager);
                fTransaction.add(R.id.robot_bottom_page, _robotHomePage);
            }else{
                fTransaction.show(_robotHomePage);
            }
            break;
        case R.id.robot_rb_gif:
            if(_robotGifPage == null){
                _robotGifPage = new RobotHomepage();
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
}