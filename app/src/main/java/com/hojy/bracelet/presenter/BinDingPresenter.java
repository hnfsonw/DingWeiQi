package com.hojy.bracelet.presenter;

import android.content.Context;


import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.BinDingActivity;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import retrofit2.Call;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 上午11:54
 * 描   述: 普通成员解绑自己的业务
 */
public class BinDingPresenter extends BasePresenter {

    private static final String TAG = "BinDingActivity";
    private Context mContext;

    private BinDingActivity mBinDingActivity;
    public Call<ResponseInfoModel> mUnbindDevice;
    private Call<ResponseInfoModel> mAdminUnbindAllDevice;

    public BinDingPresenter(Context context , BinDingActivity binDingActivity) {
        super(context);
        mContext = context;
        mBinDingActivity = binDingActivity;
    }


    /**
     * 解绑设备
     * @param deviceId
     * @param token
     * @param acountId
     */
    public void unbindDevice(int deviceId, String token, long acountId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("deviceId",deviceId);
        hashMap.put("token",token);
        hashMap.put("acountId",acountId);

        LogUtils.e(TAG,"解绑设备 " + String.valueOf(hashMap));

        mUnbindDevice = mService.unbindDevice(hashMap);
        mBinDingActivity.showLoading("",mContext);
        mUnbindDevice.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        mBinDingActivity.unbindSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG,"onFaiure " + s.getMsg());
        mBinDingActivity.printn(s.getMsg());
    }


    /**
     * 解绑所有成员
     * @param token
     * @param deviceId
     */
    public void adminUnbindAllDevice(String token, int deviceId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("deviceId",deviceId);
        hashMap.put("token",token);

        LogUtils.e(TAG,"解绑所有成员 " + String.valueOf(hashMap));

        mAdminUnbindAllDevice = mService.adminUnbindAllDevice(hashMap);
        mBinDingActivity.showLoading("",mContext);
        mAdminUnbindAllDevice.enqueue(mCallback);
    }
}
