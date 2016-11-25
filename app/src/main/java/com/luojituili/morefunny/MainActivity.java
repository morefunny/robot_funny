package com.luojituili.morefunny;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    //UI Objects
    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton robot_rb_home;
    private RadioButton robot_rb_gif;
    private RadioButton robot_rb_favorite;
    private RadioButton robot_rb_setting;
    private ViewPager vpager;

    private FragmentAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initImageLoader(getApplicationContext());


        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        bindViews();
        robot_rb_home.setChecked(true);

    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        robot_rb_home = (RadioButton) findViewById(R.id.robot_rb_home);
        robot_rb_gif = (RadioButton) findViewById(R.id.robot_rb_gif);
        robot_rb_favorite = (RadioButton) findViewById(R.id.robot_rb_favorite);
        robot_rb_setting = (RadioButton) findViewById(R.id.robot_rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

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