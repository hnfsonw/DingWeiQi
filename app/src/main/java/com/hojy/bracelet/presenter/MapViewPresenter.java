package com.hojy.bracelet.presenter;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.MapActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/13  15:24.
 * 描    述:
 */

public class MapViewPresenter extends BasePresenter{

    private static final String TAG = "MapActivity";
    private Context mContext;
    private MapActivity mMapActivity;
    private MarkerOptions markerOption;
    private Marker marker;
    private Call<ResponseInfoModel> mLocationInfo;


    public MapViewPresenter(Context context, MapActivity mapActivity) {
        super(context);
        mContext = context;
        mMapActivity = mapActivity;
    }


    public void setLatLan(double lat, double lng) {
        LatLng sourceLatLng = new LatLng(lat, lng);
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(sourceLatLng
                , 15, 0, 0)));
        addMarkersToMap(sourceLatLng);
    }


    /**
     * 移到latLng位置
     *
     * @param cameraUpdate
     */
    private void changeCamera(CameraUpdate cameraUpdate) {
//        mDLA.mAMap.moveCamera(cameraUpdate);
        mMapActivity.mAMap.animateCamera(cameraUpdate, 300, null);
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_location))
                .position(latLng)
                .draggable(true);
        marker = mMapActivity.mAMap.addMarker(markerOption);
    }


    /**
     * 获取最新的定位数据
     * @param token
     * @param deviceId
     */
    public void getLastLocationInfo(String token, int deviceId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("deviceId",deviceId);

        LogUtils.e(TAG,"获取最新的定位数据 "+ String.valueOf(hashMap));
        mLocationInfo = mService.getLastLocationInfo(hashMap);
        mLocationInfo.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.e(TAG,"onSuccess "+ data.getMsg());
        mMapActivity.onLocationSuccess(data.getData());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mMapActivity.printn(s.getMsg());
    }
}
