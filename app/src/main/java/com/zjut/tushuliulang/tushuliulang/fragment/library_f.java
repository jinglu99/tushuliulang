package com.zjut.tushuliulang.tushuliulang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjut.tushuliulang.tushuliulang.R;
import com.zjut.tushuliulang.tushuliulang.fragment_2.*;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class library_f extends Fragment  implements ViewPager.OnPageChangeListener,View.OnClickListener,View.OnTouchListener {



    private ViewPager viewPager;
    private TextView tv_topicon_1;
    private TextView tv_topicon_2;
    private ImageView imageView_topicon;


    private List<Fragment> list_fragment;
    private FragmentPagerAdapter fragmentPagerAdapter;



    private int offset = 0;
    private LinearLayout.LayoutParams lp;
    private int leftMargin=0;       // 屏幕宽度
    private int screenWidth = 0;   // 屏幕宽度的二分之一
    private int screen1_2;
    private int currentIndex = 0;

//    private share_book bsc;
//    private share_book bsid;

    public library_f() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init(getView());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_library_f, container, false);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.topicon_borrow:
                viewPager.setCurrentItem(0);
                break;
            case R.id.topicon_my:
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

    private void init(View view) {

        imageView_topicon = (ImageView) view.findViewById(R.id.library_f_iv_topicon);

        list_fragment = new ArrayList<>();
        list_fragment.add(new mycollection_f());
        list_fragment.add(new mycollection_f());

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screen1_2 = screenWidth / 2;
        lp = (LinearLayout.LayoutParams) imageView_topicon.getLayoutParams();
        leftMargin = lp.leftMargin;

        viewPager = (ViewPager) view.findViewById(R.id.library_f_viewpager);

        fragmentPagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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


        tv_topicon_1 = (TextView) view.findViewById(R.id.topicon_borrow);
        tv_topicon_2 = (TextView) view.findViewById(R.id.topicon_my);

        tv_topicon_1.setOnClickListener(this);
        tv_topicon_2.setOnClickListener(this);



    }
}
