package com.hojy.bracelet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseFragment;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.AppSettingActivity;
import com.hojy.bracelet.ui.activity.ScanActivity;
import com.hojy.bracelet.ui.adapter.MyFragemntAdapter;
import com.hojy.bracelet.ui.view.LinearTvView;
import com.hojy.bracelet.ui.view.TranslucentScrollView;
import com.hojy.bracelet.util.LogUtils;

import java.util.List;

/**
 * Created by LoyBin on 18/2/24.
 */

public class MyFragemnt extends BaseFragment implements View.OnClickListener {


    private static final String TAG = "MyFragemnt";
    private TextView mTvTitle;
    private LinearTvView mAppSetting;
    private LinearTvView mLtAbout;
    private LinearTvView mLtHelp;
    private RecyclerView mRecyclerView;
    private TextView mTvDeviceSize;
    private MyFragemntAdapter mMyFragemntAdapter;
    private LinearLayout mLlAddDevice;
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;
    private Context mContext;
    private LinearLayout mLlBack;
    private RelativeLayout mLl_layout;
    private TranslucentScrollView mTranslucentScrollView;
    public CancelDialog mCancelDialog;


    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG,"getLayoutRes");
        View view  = inflater.inflate(R.layout.fragment_my,null);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvDeviceSize = view.findViewById(R.id.tv_device_size);
        mAppSetting = view.findViewById(R.id.lt_app_setting);
        mLtAbout = view.findViewById(R.id.lt_about);
        mLtHelp = view.findViewById(R.id.lt_help);
        mLl_layout = view.findViewById(R.id.ll_layout);
        mTranslucentScrollView = view.findViewById(R.id.pullzoom_scrollview);
        mLlAddDevice = view.findViewById(R.id.ll_add_device);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLlBack = view.findViewById(R.id.iv_back);
        return view;
    }


    @Override
    protected void init() {
        initView();
        initListener();
    }


    @Override
    public void setData(List<ResponseInfoModel.DataBean.BindingListBean> data) {
        LogUtils.e(TAG,"setData "+data.size());
        mBindingListBeans = data;
        if (mMyFragemntAdapter != null){
            LogUtils.e(TAG,"onResume size = "+mBindingListBeans.size()+"");
            mTvDeviceSize.setText(mBindingListBeans.size()+"");
            mMyFragemntAdapter.setData(mBindingListBeans);
            mMyFragemntAdapter.notifyDataSetChanged();
        }
    }


    private void initListener() {
        mAppSetting.setOnClickListener(this);
        mLtAbout.setOnClickListener(this);
        mLtHelp.setOnClickListener(this);
        mLlAddDevice.setOnClickListener(this);
    }


    private void initView() {
        mTranslucentScrollView.setPullZoomView(mLl_layout);
        mLlBack.setVisibility(View.GONE);
        mTvTitle.setText(getActivity().getString(R.string.my));
        if (mBindingListBeans != null){
            mTvDeviceSize.setText(mBindingListBeans.size()+"");
            LogUtils.d(TAG, "getLayoutRes: " + mBindingListBeans.size());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.sInstance));
            mMyFragemntAdapter = new MyFragemntAdapter(getActivity(), mBindingListBeans);
            mRecyclerView.setAdapter(mMyFragemntAdapter);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lt_app_setting:
                LogUtils.e(TAG,"app 设置");
                Intent appIntent = new Intent(getActivity(),AppSettingActivity.class);
                getActivity().startActivity(appIntent);
                break;

            case R.id.lt_about:
                LogUtils.e(TAG,"关于我们");
                break;

            case R.id.lt_help:
                LogUtils.e(TAG,"帮助");
                break;

            case R.id.ll_add_device:
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setList(Context context) {
        mContext = context;
    }

    public void cancel(CancelDialog cancelDialog){
        mCancelDialog = cancelDialog;
    }



    public interface CancelDialog{
        void cancel();
    }
}
