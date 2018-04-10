package com.hojy.bracelet.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.DeviceDataActivity;

import java.util.HashMap;

import retrofit2.Call;


/**
 * Created by LoyBin on 18/3/8  16:03.
 * 描    述:
 */

public class DeviceDataPresenter extends BasePresenter{

    private Context mContext;
    private String mIMei;
    private DeviceDataActivity mDeviceDataActivity;
    private Call<ResponseInfoModel> mBindDevice;

    public DeviceDataPresenter(Context context, DeviceDataActivity deviceDataActivity) {
        super(context);
        mContext = context;
        mDeviceDataActivity = deviceDataActivity;
    }


    /**
     * 绑定设备
     * @param imei
     * @param token
     * @param acountName
     * @param deviceName
     */
    public void bindDevice(String imei, String token, String acountName, String deviceName) {
        mIMei = imei;
        if (TextUtils.isEmpty(deviceName)) {
            mDeviceDataActivity.isNameEmpty();
            return;
        }
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("imei", imei);
        paramsMap.put("token", token);
        paramsMap.put("acountName", acountName);
        paramsMap.put("nickName", deviceName);

        //执行enqueue
        mBindDevice = mService.bindDevice(paramsMap);
        mDeviceDataActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mBindDevice.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        mDeviceDataActivity.bindSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mDeviceDataActivity.printn(s.getMsg());
    }

}
