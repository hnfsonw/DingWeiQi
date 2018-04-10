package com.hojy.bracelet.presenter;

import android.content.Context;
import android.util.Log;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.GeoFenceListActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/16 下午8:26
 * 描   述: 电子围栏列表
 */
public class GeoFencePresenter extends BasePresenter {

    private static final String TAG = "GeoFenceListActivity";
    private Context mContext;

    private GeoFenceListActivity mGeoFenceListActivity;

    public Call<ResponseInfoModel> mQueryFenceInfoByDeviceId;
    public Call<ResponseInfoModel> mDeleteFenceById;
    private boolean mIsSwitch; //true 是开关接口 false 是删除


    public GeoFencePresenter(Context context, GeoFenceListActivity geoFenceListActivity) {
        super(context);
        mContext = context;
        mGeoFenceListActivity = geoFenceListActivity;
    }


    /**
     * 根据设备id查询设备的电子围栏信息
     *
     * @param token
     * @param deviceId
     * @param isShow
     */
    public void loadingColumn(String token, int deviceId, boolean isShow) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("deviceId", deviceId);
        LogUtils.d(TAG, "根据设备id查询设备的电子围栏信息 : " + String.valueOf(params));

        mQueryFenceInfoByDeviceId = mService.queryFenceListByDeviceId(params);
        if (!isShow) {
            mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mQueryFenceInfoByDeviceId.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        mGeoFenceListActivity.success(data);
        LogUtils.d(TAG, "getResultMsg: " + data.getData().getFenceList().size());

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mGeoFenceListActivity.error(s);
    }


    /**
     * 修改开关的状态
     *
     * @param resultData
     */
    public void changes(ResponseInfoModel.DataBean.FenceListBean resultData, String token,boolean isSwitch
    ,int state) {
        mIsSwitch = isSwitch;
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("name", resultData.getName());
        params.put("alarmType", resultData.getAlarmType());
        params.put("state", state);
        params.put("fenceType", resultData.getFenceType());
        params.put("radius", resultData.getRadius());
        params.put("lat", resultData.getLat());
        params.put("lng", resultData.getLng());
        params.put("acountId", MyApplication.sAcountId);
        params.put("deviceId", resultData.getDeviceId());
        params.put("desc", resultData.getDesc() + "");
        params.put("id", resultData.getId());
        params.put("startTime", "00:00");
        params.put("endTime", "24:00");
        LogUtils.d(TAG, "修改开关的状态: " + String.valueOf(params));

        Call<ResponseInfoModel> insertOrUpdateFence = mService.saveOrUpdateFenceInfo(params);
//        mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        insertOrUpdateFence.enqueue(mCallback2);
    }


    @Override
    protected void onComplete(ResponseInfoModel body) {
        LogUtils.d(TAG, "onSuccess: " + body.getMsg());
        if (!mIsSwitch){
        mGeoFenceListActivity.deleteSuccess();
        }else {
            mGeoFenceListActivity.upDataSwitch();
        }
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        mGeoFenceListActivity.error(body);
        LogUtils.d(TAG, "onError: " + body.getMsg());
    }


    /**
     * 删除电子围栏
     *
     * @param resultDataBean
     */
    public void delete(ResponseInfoModel.DataBean.FenceListBean resultDataBean, String token,boolean isSwitch) {
        mIsSwitch = isSwitch;
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("id", resultDataBean.getId());
        LogUtils.d(TAG, "删除电子围栏: " + String.valueOf(params));

        mDeleteFenceById = mService.deleteFenceInfoById(params);
        mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDeleteFenceById.enqueue(mCallback2);
    }

}
