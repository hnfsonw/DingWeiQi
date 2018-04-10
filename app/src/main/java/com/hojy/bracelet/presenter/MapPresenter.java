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
import com.hojy.bracelet.ui.fragment.MapFragemnt;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/12  15:08.
 * 描    述:
 */

public class MapPresenter extends BasePresenter{

    private static final String TAG = "MapFragemnt";
    private MapFragemnt mMapFragemnt;
    private MarkerOptions markerOption;
    public Marker marker;
    private Context mContext;
    private Call<ResponseInfoModel> mLocationInfo;

    public MapPresenter(Context context, MapFragemnt mapFragemnt) {
        super(context);
        mContext = context;
        mMapFragemnt = mapFragemnt;
    }



    public void setLatLan(double lat, double lng) {
        if (marker != null){
            marker.remove();
        }
        LogUtils.e(TAG,"setLatLan + " +lat +"` "+lng);
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
        mMapFragemnt.mAMap.animateCamera(cameraUpdate, 300, null);
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_location))
                .position(latLng)
                .draggable(true);
        marker = mMapFragemnt.mAMap.addMarker(markerOption);
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
        mLocationInfo.enqueue(mCallbackFragment);
    }

    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.e(TAG,"onSuccess "+ data.getMsg());
        if (data.getData() != null){
        mMapFragemnt.onLocationSuccess(data.getData());
        }else {
            LogUtils.e(TAG,"data == null");
            Toast.makeText(mContext,mContext.getString(R.string.no_location_information),Toast.LENGTH_SHORT).show();
            mMapFragemnt.noLocation();
        }

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG,s.getMsg());
        Toast.makeText(mContext,s.getMsg(),Toast.LENGTH_SHORT).show();
        if (mMapFragemnt.mCancelDialog != null){
            mMapFragemnt.mCancelDialog.cancel();
        }
    }


    @Override
    protected void onFragmenDissme(String message) {
        LogUtils.e(TAG,message);
        Toast.makeText(mContext,mContext.getString(R.string.network_error),Toast.LENGTH_SHORT).show();
        if (mMapFragemnt.mCancelDialog != null){
            mMapFragemnt.mCancelDialog.cancel();
        }
    }
}
