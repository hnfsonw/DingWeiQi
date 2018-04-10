package com.hojy.bracelet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.presenter.DeviceDataPresenter;
import com.hojy.bracelet.presenter.DeviceSettingPresenter;
import com.hojy.bracelet.ui.view.LinearTvView;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LoyBin on 18/3/8  17:39.
 * 描    述:设备设置
 */

public class DeviceSettingActivity extends BaseActivity {
    private static final String TAG = "DeviceSettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.lt_binding_unbundling)
    LinearTvView mLtBindingUnbundling;

    @BindView(R.id.ll_positioning_mode)
    LinearLayout mLlPositioningMode;

    @BindView(R.id.tv_location_style)
    public TextView mTvLocationStyle;

    @BindView(R.id.lt_heart_rate_)
    LinearTvView mLtHeartRate;

    @BindView(R.id.lt_alarm_clock)
    LinearTvView mLtAlarmClock;

    @BindView(R.id.lt_font_size)
    LinearTvView mLtFontSize;

    @BindView(R.id.lt_device_version)
    LinearTvView mLtDeviceVersion;


    private int mDeviceId;
    private int mType;
    private String mImei;
    private int mIsAdmin;
    private int mState;
    private int mLocationStyle;
    private DeviceSettingPresenter mDeviceSettingPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_setting;
    }

    @Override
    protected void init() {
        if (mDeviceSettingPresenter == null)
        mDeviceSettingPresenter = new DeviceSettingPresenter(this,this);
        EventBus.getDefault().register(this);
        mDeviceId = getIntent().getIntExtra("deviceId", 0);
        mType = getIntent().getIntExtra("type", 0);
        mImei = getIntent().getStringExtra("imei");
        mIsAdmin = getIntent().getIntExtra("isAdmin", -1);
        LogUtils.e(TAG, "deviceId = " + mDeviceId + "  mImei=" + mImei);
        initView();
    }


    private void initView() {
        mTvTitle.setText(mType == 1 ? getString(R.string.locator_setting) : getString(R.string.locator_setting));
        mLtHeartRate.mIvSwitch.setVisibility(View.VISIBLE);
        mDeviceSettingPresenter.switchLocationStyle(1);
    }


    //更新了设备
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpData(NewDeviceModel newDeviceModel) {
        LogUtils.e(TAG, "解绑了");
        finishActivityByAnimation(this);
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.lt_binding_unbundling,R.id.ll_positioning_mode, R.id.lt_heart_rate_,
            R.id.lt_alarm_clock, R.id.lt_font_size, R.id.lt_device_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.lt_binding_unbundling:
                Intent intent = new Intent(this, BinDingActivity.class);
                intent.putExtra("deviceId", mDeviceId);
                intent.putExtra("type", mType);
                intent.putExtra("imei", mImei);
                intent.putExtra("isAdmin", mIsAdmin);
                startActivity(intent);
                break;

            case R.id.ll_positioning_mode:
                //定位模式
                toActivity(100, PositioningModeActivity.class, mLocationStyle + "");
                break;

            case R.id.lt_heart_rate_:
                mLtHeartRate.toggle();
                boolean toggle = mLtHeartRate.getToggle();
                if (toggle) {
                    mState = 1;
                } else {
                    mState = 0;
                }

//                mWatchSettingPresenter.insertOrUpdateDeviceSwtich(MyApplication.sToken,
//                        MyApplication.sDeviceId, MyApplication.sAcountId, mState, true);
                LogUtils.d(TAG, "mLtStrangerCalls: " + toggle);
                break;

            case R.id.lt_alarm_clock:
                break;

            case R.id.lt_font_size:
                break;

            case R.id.lt_device_version:
                break;

                default:
                    break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            if (data != null) {
                int locationStyle = data.getIntExtra("locationStyle", -1);
                LogUtils.e(TAG, "返回界面的数据locationStyle  " + locationStyle);
                mLocationStyle = locationStyle;
                mDeviceSettingPresenter.switchLocationStyle(locationStyle);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }



}
