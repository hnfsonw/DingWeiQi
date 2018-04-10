package com.hojy.bracelet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.MapPresenter;
import com.hojy.bracelet.presenter.MapViewPresenter;
import com.hojy.bracelet.ui.view.NavigationDialog;
import com.hojy.bracelet.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LoyBin on 18/3/12  15:25.
 * 描    述:
 */

public class MapActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MapActivity";
    @BindView(R.id.ll_back)
    LinearLayout mLlBack;

    @BindView(R.id.rl_refresh)
    RelativeLayout mRlRefresh;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.map_view)
    MapView mMapView;

    @BindView(R.id.iv_location)
    ImageView mIvLocation;

    @BindView(R.id.tv_address)
    public TextView mTvAddress;

    @BindView(R.id.ll_address)
    LinearLayout mLlAddress;

    @BindView(R.id.tv_type)
    TextView mTvType;

    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.ll_lontion)
    RelativeLayout mLlLontion;

    @BindView(R.id.cb_map_type)
    CheckBox mCbMapType;

    @BindView(R.id.ll_map_type)
    LinearLayout mLlMapType;

    @BindView(R.id.ll_text)
    LinearLayout mLlText;

    @BindView(R.id.add_zoom)
    TextView mAddZoom;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.narrow_zoom)
    TextView mNarrowZoom;

    @BindView(R.id.btn_location)
    ImageView mBtnLocation;

    @BindView(R.id.ll_history)
    LinearLayout mLlHistory;

    @BindView(R.id.ll_electronic_fence)
    LinearLayout mLlElectronicFence;

    @BindView(R.id.ll_navigation)
    LinearLayout mLlNavigation;

    public AMap mAMap;
    private UiSettings mUiSettings;
    private ResponseInfoModel.DataBean mBindingListBean;
    private int mDeviceId;
    private MapViewPresenter mMapViewPresenter;
    private NavigationDialog mNavigationDialog;
    public double mLat;
    public double mLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "onCreate");
        mMapView.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutRes() {
        LogUtils.e(TAG, "getLayoutRes");
        return R.layout.activity_map;
    }

    @Override
    protected void init() {
        mBindingListBean = (ResponseInfoModel.DataBean) getIntent().getSerializableExtra("location");
        mDeviceId = getIntent().getIntExtra("deviceId", -1);
        LogUtils.e(TAG, "getAddress " + mBindingListBean.getAddress() +"mDeviceId =" +mDeviceId);
        if (mMapViewPresenter == null)
            mMapViewPresenter = new MapViewPresenter(this,this);
        initMap();
        initView();
        initData();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.locator2));
        mCbMapType.setOnCheckedChangeListener(this);
        mLat = mBindingListBean.getLat();
        mLng = mBindingListBean.getLng();
        setLocationData(mBindingListBean.getAddress(),mBindingListBean.getLocationTime(),mLat,mLng);
    }


    /**
     * 更新定位数据
     * @param address
     * @param locationTime
     * @param lat
     * @param lng
     */
    public void setLocationData(String address, String locationTime, double lat, double lng) {
        mLat = lat;
        mLng = lng;
        mTvAddress.setText(address);
        mTvTime.setText(locationTime);
        mMapViewPresenter.setLatLan(lat, lng);
    }


    private void initData() {

    }


    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setTiltGesturesEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
        }

        mMapViewPresenter.setLatLan(mBindingListBean.getLat(),mBindingListBean.getLng());
    }

    @Override
    protected void dismissNewok() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @OnClick({R.id.ll_back, R.id.rl_refresh,R.id.ll_map_type,R.id.ll_electronic_fence,R.id.ll_history,
    R.id.ll_navigation,R.id.add_zoom,R.id.narrow_zoom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishActivityByAnimation(this);
                break;

            case R.id.rl_refresh:
                LogUtils.e(TAG,"刷新位置");
                mMapViewPresenter.getLastLocationInfo(MyApplication.sToken,mDeviceId);
                break;

            case R.id.ll_map_type:
                LogUtils.e(TAG,"切换地图");
                break;


            case R.id.ll_electronic_fence:
                MyApplication.sDeviceId = mDeviceId;
                Intent intent = new Intent(this,GeoFenceListActivity.class);
                intent.putExtra("location",mBindingListBean);
                startActivity(intent);
                break;


            case R.id.ll_history:
                toActivity(DevicesHistoryActivity.class);
                break;


            case R.id.ll_navigation:
                navigation();
                break;


            case R.id.add_zoom:
                changeCamera(CameraUpdateFactory.zoomIn(), null);
                break;


            case R.id.narrow_zoom:
                changeCamera(CameraUpdateFactory.zoomOut(), null);
                break;
        }
    }


    public void onLocationSuccess(ResponseInfoModel.DataBean data) {
        if (data.getLat() == mLat && data.getLng() == mLng){
            printn(getString(R.string.the_latest_data));
            return;
        }
        setLocationData(data.getAddress(),data.getLocationTime(),data.getLat(),data.getLng());
    }


    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        mAMap.animateCamera(update, 300, callback);
    }


    /**
     *  地图类型切换
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_map_type:
                if (isChecked) {
                    mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                } else {
                    mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                }
                break;

        }
    }


    /**
     * 导航地图的点击事件
     */
    public void navigation() {
        if (mNavigationDialog == null) {
            mNavigationDialog = new NavigationDialog(this, this);
        }
        mNavigationDialog.show();
    }


}
