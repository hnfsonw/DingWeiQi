package com.hojy.bracelet.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.ui.view.CircleIndicator;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LoyBin on 18/3/26  10:41.
 * 描    述:
 */

public class GuideActivity extends AppCompatActivity{


    public static final int PAGE_COUNT = 4;
    private static final java.lang.String TAG = "GuideActivity";
    public List<View> mList = new ArrayList<>();

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    GuideAdapter mAdapter;

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //TODO
            //status bar background color
        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.d(TAG,"onPageSelected = " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        init();

    }



    private void init() {
        Boolean guide = (Boolean) SharedPreferencesUtils.getParam(this, "guide", false);
            if (guide){
                Intent intent = new Intent(this,SplashActivity.class);
                startActivity(intent);
                finish();
            }else {
                initData();
                mAdapter = new GuideAdapter(this,mList);
                mViewPager.setAdapter(mAdapter);

                indicator.setViewPager(mViewPager);
                clearOverScrollEffect(mViewPager);
            }
    }




    private void initData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mList.add(inflater.inflate(R.layout.view_guide_01, null));
        mList.add(inflater.inflate(R.layout.view_guide_02, null));
        mList.add(inflater.inflate(R.layout.view_guide_01, null));
        mList.add(inflater.inflate(R.layout.view_guide_02, null));
    }

    void clearOverScrollEffect(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            view.setOverScrollMode(View.OVER_SCROLL_NEVER);
            view.setHorizontalFadingEdgeEnabled(false);
            view.setVerticalFadingEdgeEnabled(false);
        }
    }


    public static class GuideAdapter extends PagerAdapter {
        private GuideActivity mContext;
        private List<View> mList;

        public GuideAdapter(GuideActivity context ,List<View> list){
            mContext = context;
            mList = list;
        }


        @Override
        public int getCount() {
            return mList.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position), 0);
            //当滑动到最后一页的时候，监听按钮
            if (position == mList.size() - 1) {
                TextView tvEnter = container.findViewById(R.id.tv_guide_enter);

                tvEnter.setText(mContext.getString(R.string.login));
                tvEnter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LogUtils.d(TAG,"进入");
                        //在此记录曾经浏览过导航页，下次启动将不会进入导航页
                        SharedPreferencesUtils.setParam(mContext,"guide", true);
                        Intent intent = new Intent(mContext,SplashActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }
                });
            }
            return mList.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }




    @Override
    protected void onStop() {
        super.onStop();
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
