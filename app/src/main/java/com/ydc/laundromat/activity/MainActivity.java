package com.ydc.laundromat.activity;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ydc.laundromat.R;
import com.ydc.laundromat.fragment.Fragment_Home;
import com.ydc.laundromat.fragment.Fragment_Mime;
import com.ydc.laundromat.fragment.Fragment_Order;
import com.ydc.laundromat.util.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private Fragment home_Fragment, order_Fragment, mine_Fragment,
            currentFragment;

    private RadioButton home_rb,order_rb,mine_rb;

    /**
     * 作为页面容器的ViewPager
     */
    ViewPager mViewPager;
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;


    private RadioGroup rg_tab;
    private TitleBuilder titleBuilder;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*setStatusBar();*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.titlebar_blue));
        }

        initTitle();
        init();
    }


    private void initTitle() {
        titleBuilder = new TitleBuilder(this).setTitleText("自助洗衣");
    }

    private void init() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        rg_tab.setOnCheckedChangeListener(this);

        home_rb = (RadioButton) findViewById(R.id.rb_home);
        order_rb = (RadioButton) findViewById(R.id.rb_order);
        mine_rb = (RadioButton) findViewById(R.id.rb_mine);

        mViewPager=(ViewPager) findViewById(R.id.viewpager);

        fragmentList=new ArrayList<Fragment>();
        home_Fragment=new Fragment_Home();
        order_Fragment=new Fragment_Order();
        mine_Fragment=new Fragment_Mime();

        fragmentList.add(home_Fragment);
        fragmentList.add(order_Fragment);
        fragmentList.add(mine_Fragment);


        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if(arg0 == 0){
                    home_rb.setChecked(true);
                    titleBuilder.setTitleText("自助洗衣");
                }else if(arg0 == 1){
                    order_rb.setChecked(true);
                    titleBuilder.setTitleText("订单");
                }else if(arg0 == 2){
                    mine_rb.setChecked(true);
                    titleBuilder.setTitleText("我的");
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {


        public MyFrageStatePagerAdapter(FragmentManager fm)
        {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container)
        {
            super.finishUpdate(container);


        }

    }

   /* private void imageMove(int moveToTab)
    {
        int startPosition=0;
        int movetoPosition=0;

        startPosition=currenttab*(screenWidth/4);
        movetoPosition=moveToTab*(screenWidth/4);
        //平移动画
        TranslateAnimation translateAnimation=new TranslateAnimation(startPosition,movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);

    }*/
    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        mViewPager.setCurrentItem(desTab,false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.rb_home:

                changeView(0);
                titleBuilder.setTitleText("自助洗衣");
                break;
            case R.id.rb_order:

                changeView(1);
                titleBuilder.setTitleText("订单");
                break;
            case R.id.rb_mine:

                changeView(2);
                titleBuilder.setTitleText("我的");
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
