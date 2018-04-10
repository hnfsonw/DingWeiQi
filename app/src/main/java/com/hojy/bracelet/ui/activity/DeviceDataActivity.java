package com.hojy.bracelet.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.presenter.DeviceDataPresenter;
import com.hojy.bracelet.ui.view.LastInputEditText;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LoyBin on 18/3/8  15:59.
 * 描    述:
 */

public class DeviceDataActivity extends BaseActivity {


    private static final String TAG = "DeviceDataActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_baby_name)
    LastInputEditText mEtBabyName;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    private String mImei;
    private DeviceDataPresenter mDeviceDataPresenter;
    private String mDeviceName;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_data;
    }


    @Override
    protected void init() {
        mImei = getIntent().getStringExtra(STRING);
        LogUtils.e(TAG, mImei);
        if (mDeviceDataPresenter == null) {
            mDeviceDataPresenter = new DeviceDataPresenter(this, this);
        }
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.device_data));
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.btn_complete})
    public void onViewClicked(View view) {
        mDeviceName = mEtBabyName.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_complete:
                mDeviceDataPresenter.bindDevice(mImei, MyApplication.sToken, MyApplication.sAcountName,mDeviceName);
                break;
        }
    }





    public void isNameEmpty() {
        printn(getString(R.string.name_enpty));
    }


    /**
     * 绑定成功
     */
    public void bindSuccess() {
        EventBus.getDefault().post(new NewDeviceModel());
        finishActivityByAnimation(this);
    }
}
