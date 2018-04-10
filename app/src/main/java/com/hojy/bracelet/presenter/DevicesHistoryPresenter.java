package com.hojy.bracelet.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.DevicesHistoryActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/26 上午11:43
 * 描   述: 历史轨迹的业务逻辑
 */
public class DevicesHistoryPresenter extends BasePresenter {

    private static final String TAG = "DevicesHistoryActivity";
    private Context mContext;
    private DevicesHistoryActivity mDHA;
    public LatLng mLatLng;
    public Call<ResponseInfoModel> mHistoryLocations;
    public double mStartingLat;
    public double mStartingLng;
    public double mEndLag;
    public double mEndLng;
    public Marker mMarker;
    private Polyline mPolyline;

    public DevicesHistoryPresenter(Context context, DevicesHistoryActivity devicesHistoryActivity) {
        super(context);
        mContext = context;
        mDHA = devicesHistoryActivity;
    }


    /**
     * 获取历史轨迹
     *
     * @param data
     * @param token
     * @param deviceId
     */
    public void getHistoryLocations(String data, String token, long deviceId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("deviceId", deviceId);
        params.put("startDate", data);
        params.put("endDate", data);
        LogUtils.d(TAG, "getLocationList: " + String.valueOf(params));

        mHistoryLocations = mService.getHistoryLocations(params);
        mDHA.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mHistoryLocations.enqueue(mCallback);

    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.d(TAG, "getResultMsg: " + data.getMsg().toString());
        if (data.getData().getLocationList().size() > 0) {
            LogUtils.d(TAG, "size: " + data.getData().getLocationList().size());
            LogUtils.d(TAG, "getLat: " + data.getData().getLocationList().get(0).getLat());
            LogUtils.d(TAG, "getLng: " + data.getData().getLocationList().get(0).getLng());
        }
        mDHA.onSuccess(data);

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        Log.d(TAG, "onFaiure: " + s.getMsg());
        mDHA.onError(s.getMsg());
    }



    /**
     * 画线
     */
    public void openLine(List<ResponseInfoModel.DataBean.LocationListBean> getDeviceHistoryList) {
        mDHA.mAMap.clear();
        //记录开始和终点位置
        recordStartingPoint(getDeviceHistoryList);
        //移动地图的某个位置
        moveToPoint(mLatLng, true);
        //画线
        LogUtils.e(TAG, " mDHA.mLatLngList" + mDHA.mLatLngList.size());
        if (mDHA.mLatLngList.size() >= 2) {
            drawLine(mDHA.mLatLngList);
        }

        mDHA.timeCount += 1;
        mDHA.pointCount += 1;
    }


    /**
     * 记录开始和终点位置
     *
     * @param getDeviceHistoryList
     */
    public void recordStartingPoint(List<ResponseInfoModel.DataBean.LocationListBean> getDeviceHistoryList) {
        mDHA.mLatLngList.clear();

        for (int i = 0; i < getDeviceHistoryList.size(); i++) {
            double lat = getDeviceHistoryList.get(i).getLat();
            double lng = getDeviceHistoryList.get(i).getLng();


            mLatLng = new LatLng(lat, lng);
            Log.d(TAG, "高德+lat: " + lat);
            Log.d(TAG, "高德+ lng: " + lng);
            Log.d(TAG, "百度+ lag: " + mLatLng.latitude);
            Log.d(TAG, "百度+ lng: " + mLatLng.longitude);
            mDHA.index++;
            mDHA.mLatLngList.add(mLatLng);
            //记录起始位置
            if (i == 0) {
                mMarker = mDHA.mAMap.addMarker(new MarkerOptions().position(mLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.starting_point)));
                mMarker.setToTop();
                mStartingLat = mLatLng.latitude;
                mStartingLng = mLatLng.longitude;
                //记录终点位置
            } else if (i == getDeviceHistoryList.size() - 1) {
                //添加marker
                mDHA.mAMap.addMarker(new MarkerOptions().position(mLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_of)));

            } else {
                //添加marker
                mDHA.mAMap.addMarker(new MarkerOptions().position(mLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bule))).setAnchor(0.5f, 0.5f);
            }

            if (i == getDeviceHistoryList.size() / 2) {
                mEndLag = mLatLng.latitude;
                mEndLng = mLatLng.longitude;
            }
        }
    }


    /**
     * 移动到地图的某个位置
     *
     * @param ll
     */
    public void moveToPoint(final LatLng ll, boolean flag) {
        if (mDHA.mAMap == null) {
            return;
        }

        try {
            if (mDHA.mHistoryList != null && mDHA.mHistoryList.size() > 0) {

                if (mDHA.mHistoryList.size() == 1) {
                    mDHA.mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 18f));
                } else {
                    LatLngBounds bounds = getLatLngBounds();
                    mDHA.mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (ResponseInfoModel.DataBean.LocationListBean overlay : mDHA.mHistoryList) {
            // polyline 中的点可能太多，只按marker 缩放
            LatLng sourceLatLng = new LatLng(overlay.getLat(), overlay.getLng());
            b.include(sourceLatLng);
        }
        return b.build();
    }


    /**
     * 画历史轨迹线
     *
     * @param list
     */
    public void drawLine(List<LatLng> list) {
        LogUtils.e(TAG, "画历史轨迹线");
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(200, 63, 145, 252));
        polylineOptions.setPoints(list);
        mDHA.mAMap.addPolyline(polylineOptions);

    }


}
