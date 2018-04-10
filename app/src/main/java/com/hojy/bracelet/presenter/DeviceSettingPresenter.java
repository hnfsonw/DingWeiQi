package com.hojy.bracelet.presenter;

import android.content.Context;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.DeviceSettingActivity;

/**
 * Created by LoyBin on 18/4/2  16:38.
 * 描    述:
 */

public class DeviceSettingPresenter extends BasePresenter{

    private Context mContext;
    private DeviceSettingActivity mDeviceSettingActivity;

    public DeviceSettingPresenter(Context context, DeviceSettingActivity deviceSettingActivity) {
        super(context);
        mContext = context;
        mDeviceSettingActivity = deviceSettingActivity;
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {

    }


    /**
     * 定位模式设置
     *
     * @param locationStyle
     */
    public void switchLocationStyle(int locationStyle) {
        switch (locationStyle) {
            case -1:
                mDeviceSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.accurate_model));
                break;

            case 1:
                mDeviceSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.accurate_model));
                break;

            case 2:
                mDeviceSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.normal_mode));
                break;

            case 3:
                mDeviceSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.Save_mode));
                break;

            case 4:
                mDeviceSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.text_model));
                break;

        }

    }
}
