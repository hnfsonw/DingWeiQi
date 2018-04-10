package com.hojy.bracelet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseFragment;
import com.hojy.bracelet.model.RefreshModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.ScanActivity;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LoyBin on 18/2/24
 * 无设备添加界面
 */

public class HomeFragemnt extends BaseFragment {

    private static final String TAG = "HomeFragemnt";
    private LinearLayout mLLAdd;

    @Override
    protected void init() {
        initView();

        EventBus.getDefault().post(new RefreshModel());
    }

    @Override
    public void setData(List<ResponseInfoModel.DataBean.BindingListBean> data) {

    }

    @Override
    protected View getLayoutRes(LayoutInflater inflater,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_add_device,null);
        mLLAdd = view.findViewById(R.id.ll_add);
        return view;
    }


    private void initView() {
        mLLAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e(TAG,"点击添加");
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }



}
