package com.hojy.bracelet.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hojy.bracelet.util.LogUtils;

/**
 * Created by LoyBin on 18/3/19  16:44.
 * 描    述:
 */

public class MapLayout extends LinearLayout{

    private TranslucentScrollView mScrollView;

    public MapLayout(Context context) {
        super(context);
    }

    public MapLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent. ACTION_UP) {
            mScrollView .requestDisallowInterceptTouchEvent( false); //手离开地图的时候不拦截scrollview的touch事件，这样手指滑动的时候scrollview就会滑动
        } else {
            mScrollView .requestDisallowInterceptTouchEvent( true); //底层view调用此方法后，true表示父层View的touch事件将不会被触发,父View不拦截事件，由子view自己处理
        }
        return false; //返回值表示是否拦截touch事件至子view，true表示拦截touch事件,不将touch传递至子view,而是执行自己的onTouchEvent方法；false表示不拦截,传递至子view，子view将会执行onTouchEvent方法
    }


    public void setView(TranslucentScrollView translucentScrollView) {
        mScrollView = translucentScrollView;
    }
}
