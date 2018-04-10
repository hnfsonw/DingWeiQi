package com.hojy.bracelet.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.AddressSettingActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/16 下午7:31
 * 描   述: 电子围栏提交的逻辑
 */
public class AddressSettingPresenter extends BasePresenter {

    private static final String TAG = "AddressSettingActivity";
    protected Context mContext;
    private AddressSettingActivity mAddressSettingActivity;
    private static final int ADDRESS_ONE = 1;//默认
    public Call<ResponseInfoModel> mCall;


    public AddressSettingPresenter(Context context, AddressSettingActivity addressSettingActivity) {
        super(context);
        mContext = context;
        mAddressSettingActivity = addressSettingActivity;
    }


    /**
     * 新增|修改电子围栏
     *
     * @param addressName
     * @param alarmType
     * @param acountId
     * @param deviceId
     * @param lng
     * @param lat
     * @param radius
     * @param token
     * @param fenceId
     * @param desc
     */
    public void save(String addressName, int alarmType, long acountId, int deviceId,
                     double lat, double lng, int radius, String token, int fenceId, String desc) {
        if (TextUtils.isEmpty(addressName)) {
            mAddressSettingActivity.nameEmpty();
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("name", addressName);
        params.put("alarmType", alarmType);
        params.put("state", ADDRESS_ONE);
        params.put("fenceType", ADDRESS_ONE);
        params.put("radius", radius);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("acountId", acountId);
        params.put("deviceId", deviceId);
        params.put("desc", desc);
        params.put("startTime", "00:00");
        params.put("endTime", "24:00");
        if (fenceId > 0) {
            params.put("id", fenceId);
        }
        Log.d(TAG, "sendCode: " + String.valueOf(params));

        mCall = mService.saveOrUpdateFenceInfo(params);
        mAddressSettingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCall.enqueue(mCallback);

    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        mAddressSettingActivity.success();
        LogUtils.d(TAG, "parserJson: " + data.getMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mAddressSettingActivity.error(s.getMsg());
        LogUtils.d(TAG, "onFaiure: " + s.getMsg());

    }


}
