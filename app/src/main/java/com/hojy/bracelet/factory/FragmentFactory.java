package com.hojy.bracelet.factory;


import android.support.v4.app.Fragment;

import com.hojy.bracelet.ui.fragment.HomeFragemnt;
import com.hojy.bracelet.ui.fragment.MapFragemnt;
import com.hojy.bracelet.ui.fragment.MyFragemnt;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类    名:  FragmentFactory
 * 创 建 者:  LogBin
 * 创建时间:  2017/9/7 3:19
 * 描    述： 封装Fragment的创建过程
 */
public class FragmentFactory {
    public static final int FRAGMENT_HOME                    = 0;//首页
    public static final int FRAGMENT_STORY_BOOKS             = 1;//地图
    public static final int FRAGMENT_ENGLISH_ENLIGHTENMENT   = 2;//无设备
    private static final String TAG = "FragmentFactory";

    public static Map<Integer, Fragment> mCacheFragmentMap = new HashMap<>();

    public static Fragment createFragment(int position) {
        Fragment fragment = null;
        //优先从集合中取出来
        if (mCacheFragmentMap.containsKey(position)) {
            LogUtils.d(TAG,"优先从集合中取出来");
            return mCacheFragmentMap.get(position);
        }

        switch (position) {
            case FRAGMENT_HOME:
                fragment = new MapFragemnt();
//                fragment = new HomeFragemnt();
                break;

            case FRAGMENT_STORY_BOOKS:
                fragment = new MyFragemnt();

                break;

            case FRAGMENT_ENGLISH_ENLIGHTENMENT:
                fragment = new HomeFragemnt();
                break;

            default:
                break;
        }
        //保存fragment到集合中
        mCacheFragmentMap.put(position, fragment);

        return fragment;
    }
}
