package com.hojy.bracelet.presenter;

import android.content.Context;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.PositioningModeActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/11 上午11:17
 * 描   述: 定位模式业务逻辑view
 */
public class PositioningModePreserter extends BasePresenter {
    private static final String TAG = "PositioningModeActivity";
    private PositioningModeActivity mPositioningModeActivity;
    private Context mContext;
    public Call<ResponseInfoModel> mCall;

    public PositioningModePreserter(Context context, PositioningModeActivity positioningModeActivity) {
        super(context);
        mContext = context;
        mPositioningModeActivity = positioningModeActivity;
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getMsg());
        int errorCode = data.getCode();
        chekErrorCode(errorCode);

    }


    private void chekErrorCode(int errorCode) {
        switch (errorCode) {
            case 92302:
                //当前设备不在线
                mPositioningModeActivity.onSuccess(mContext.getString(R.string.watch_off));
                break;

            case 0:
                mPositioningModeActivity.onSuccess(mContext.getString(R.string.setting_success));
                break;

            case 90211:
                mPositioningModeActivity.onSuccess(mContext.getString(R.string.watch_off));
                break;
        }
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, s.getMsg());
        mPositioningModeActivity.dismissLoading();
        mPositioningModeActivity.printn(s.getMsg());
    }


    /**
     * 设置定位模式请求
     *
     * @param token
     * @param deviceId
     * @param locationStyle
     */
    public void upLocationStyle(String token, int deviceId, int locationStyle) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("deviceId", deviceId);
//        hashMap.put("token", token);
//        hashMap.put("locationStyle", locationStyle);
//
//        LogUtils.e(TAG, "设置定位模式请求  " + String.valueOf(hashMap));
////        mCall = mService.insertOrUpdateDeviceAttr(hashMap);
//        mPositioningModeActivity.showLoading("",mContext);
//        mCall.enqueue(mCallback);
    }
}
