package com.zjut.tushuliulang.tushuliulang.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.fragment_2.*;
import com.zjut.tushuliulang.tushuliulang.R;

import java.util.ArrayList;
import java.util.List;

public class Book_share_info extends ActionBarActivity implements ViewPager.OnPageChangeListener,ViewPager.OnClickListener,ViewPager.OnTouchListener {

    private ViewPager viewPager;
    private TextView tv_topicon_1;
    private TextView tv_topicon_2;
    private ImageView imageView_topicon;
    private ActionBar actionBar;


    private List<Fragment> list_fragment;
    private FragmentPagerAdapter fragmentPagerAdapter;



    private int offset = 0;
    private LinearLayout.LayoutParams lp;
    private int leftMargin=0;       // 屏幕宽度
    private int screenWidth = 0;   // 屏幕宽度的二分之一
    private int screen1_2;
    private int currentIndex = 0;




    private book_share_comment bsc;
    private book_share_info_detail bsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_share_info);



        init();


    }

    private void init() {

        imageView_topicon = (ImageView) findViewById(R.id.book_share_inof_iv_topicon);

        list_fragment = new ArrayList<>();
        list_fragment.add(bsid = new book_share_info_detail());
        list_fragment.add(bsc=new book_share_comment());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screen1_2 = screenWidth / 2;
        lp = (LinearLayout.LayoutParams) imageView_topicon.getLayoutParams();
        leftMargin = lp.leftMargin;

        viewPager = (ViewPager) findViewById(R.id.book_share_info_viewpager);
        viewPager.setOnPageChangeListener(this);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_fragment.get(position);
            }

            @Override
            public int getCount() {
                return list_fragment.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                offset = (screen1_2 - imageView_topicon.getLayoutParams().width) / 2;
                final float scale = getResources().getDisplayMetrics().density;
                if (position == 0) {// 0<->1
                    lp.leftMargin = (int) (positionOffsetPixels / 2) + offset;
                } else if (position == 1) {// 1<->2
                    lp.leftMargin = (int) (positionOffsetPixels / 2) + screen1_2 + offset;
                }
                imageView_topicon.setLayoutParams(lp);
                currentIndex = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tv_topicon_1 = (TextView) findViewById(R.id.topicon_details);
        tv_topicon_2 = (TextView) findViewById(R.id.topicon_comment);

        tv_topicon_1.setOnClickListener(this);
        tv_topicon_2.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_share_info, menu);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.topicon_details:
                viewPager.setCurrentItem(0);
                break;
            case R.id.topicon_comment:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}
