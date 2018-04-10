package com.hojy.bracelet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseFragment;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.RefreshModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.MapPresenter;
import com.hojy.bracelet.ui.activity.MapActivity;
import com.hojy.bracelet.ui.activity.MessageCenterActivity;
import com.hojy.bracelet.ui.adapter.MapFragemntAdapter;
import com.hojy.bracelet.ui.view.LinearTvView;
import com.hojy.bracelet.ui.view.MapLayout;
import com.hojy.bracelet.ui.view.PopupMenu;
import com.hojy.bracelet.ui.view.TranslucentScrollView;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LoyBin on 18/2/24.
 */

public class MapFragemnt extends BaseFragment implements View.OnClickListener, AMap.OnMapClickListener{

    private static final String TAG = "MapFragemnt";
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;
    public AMap mAMap;
    private MapView mMapView;
    private ImageView mIvReturn;
    private LinearLayout mLlIcon;
    private ImageView mIvIcon;
    private PopupMenu mPopupMenu;
    private float mOffsetX;
    private float mOffsetY;
    private float mMenuWidth;
    private ListView mListview;
    private LinearTvView mLtStepGauge;
    private LinearTvView mLtSleep;
    private LinearTvView mLtHeartRate;
    private LinearLayout mLlRight;
    private ResponseInfoModel.DataBean.BindingListBean mBindingListBean;
    private MapFragemntAdapter mMapFragemntAdapter;
    private UiSettings mUiSettings;
    private MapPresenter mMapPresenter;
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvShowTime;
    private TextView mTvShowAddress;
    private ResponseInfoModel.DataBean mDataBean;
    private TextView mTvRight;
    private ImageView mIvRegiht;
    private TranslucentScrollView mTranslucentScrollView;
    private MapLayout mLlayout;
    public CancelDialog mCancelDialog;
    private TextView mTvName;


    @Override
    protected View getLayoutRes(LayoutInflater inflater,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        mMapView = view.findViewById(R.id.map);
        mIvReturn = view.findViewById(R.id.iv_return);
        mLlIcon = view.findViewById(R.id.ll_icon);
        mIvIcon = view.findViewById(R.id.iv_icon);
        mTranslucentScrollView = view.findViewById(R.id.pullzoom_scrollview);
        mLtStepGauge = view.findViewById(R.id.lt_step_gauge);
        mLtSleep = view.findViewById(R.id.lt_sleep);
        mLtHeartRate = view.findViewById(R.id.lt_heart_rate);
        mTvAddress = view.findViewById(R.id.tv_address);
        mTvTime = view.findViewById(R.id.tv_time);
        mTvName = view.findViewById(R.id.tv_name);
        mLlayout = view.findViewById(R.id.ll_layout);
        mTvShowTime = view.findViewById(R.id.tv_show_time);
        mTvShowAddress = view.findViewById(R.id.tv_show_address);
        mTvRight = view.findViewById(R.id.tv_right);
        mLlRight = view.findViewById(R.id.ll_right);
        mIvRegiht = view.findViewById(R.id.iv_message_right);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        return view;
    }


    @Override
    protected void init() {
        LogUtils.e(TAG,"init");
        if (mMapPresenter == null){
        mMapPresenter = new MapPresenter(getActivity(),this);
        }
        initView();
        initMapView();
        initListener();
        initData();
        EventBus.getDefault().post(new RefreshModel());
    }

    private void initMapView() {
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);

//        mUiSettings.setAllGesturesEnabled (false);
        mUiSettings.setZoomGesturesEnabled(true);
        mAMap.setOnMapClickListener(this);

    }


    private void initData() {
        LogUtils.e(TAG,"initData");
        if (mMapFragemntAdapter == null)
        mMapFragemntAdapter = new MapFragemntAdapter(getActivity(),this,mBindingListBeans);
        mListview.setAdapter(mMapFragemntAdapter);

        if (mBindingListBean != null){
            mTvName.setText(mBindingListBean.getNickName());
            setTypeSwitch(mBindingListBean.getType());
            getLastLocationInfo(MyApplication.sToken,mBindingListBean.getDeviceId());
        }
    }


    private void getLastLocationInfo(String token, int deviceId) {
        if (mMapPresenter != null)
        mMapPresenter.getLastLocationInfo(token,deviceId);
    }


    private void initListener() {
        mLlIcon.setOnClickListener(this);
        mLlRight.setOnClickListener(this);
    }


    private void initView() {
        mIvReturn.setVisibility(View.GONE);
        mIvIcon.setVisibility(View.VISIBLE);
        mIvRegiht.setVisibility(View.VISIBLE);
        mLlayout.setView(mTranslucentScrollView);


        View menuLayout = getLayoutInflater().inflate(R.layout.dialog_device, null);
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mListview = menuLayout.findViewById(R.id.listview);
        mPopupMenu = new PopupMenu((ViewGroup) menuLayout);
        mPopupMenu.setMenuItemBackgroundColor(0xffb1df83);
        mPopupMenu.setMenuItemHoverBackgroundColor(0x22000000);

        mOffsetX = 0;
        mOffsetY = 0;
        mMenuWidth = menuLayout.getMeasuredWidth();
    }


    @Override
    public void setData(List<ResponseInfoModel.DataBean.BindingListBean> data) {
        LogUtils.e(TAG,"setData = " + MyApplication.sDeivceNumber);
        mBindingListBeans = data;
        mBindingListBean = mBindingListBeans.get(MyApplication.sDeivceNumber);
        MyApplication.sDeviceId = mBindingListBean.getDeviceId();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG,"onDestroy ");
        mMapView.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    public void setList(List<ResponseInfoModel.DataBean.BindingListBean> bindingListBeans) {
        mBindingListBeans = bindingListBeans;
        LogUtils.e(TAG,"setList " + MyApplication.sDeivceNumber);
        mBindingListBean = bindingListBeans.get(MyApplication.sDeivceNumber);
        if (mMapFragemntAdapter != null){
            mMapFragemntAdapter.setData(mBindingListBeans);
            mMapFragemntAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_icon:
                if (mPopupMenu.isShowing()) {
                    mPopupMenu.dismiss();
                } else {
                    mPopupMenu.show(view, (int) (view.getWidth() - mOffsetX - mMenuWidth), (int) mOffsetX);
//                    mPopupMenu.showAsDropDown(mIvIcon);
                }
                break;

            case R.id.ll_right:
                Intent intent = new Intent(getActivity(),MessageCenterActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }


    /**
     * 选择设备的点击事件
     * @param position
     */
    public void setTypeOnClick(int position) {
        MyApplication.sDeivceNumber = position;
        mBindingListBean = mBindingListBeans.get(position);
        mTvName.setText(mBindingListBean.getNickName());
        getLastLocationInfo(MyApplication.sToken,mBindingListBean.getDeviceId());
        int type = mBindingListBean.getType();
        mPopupMenu.dismiss();
        setTypeSwitch(type);
    }


    private void setTypeSwitch(int type) {
        if (type == 1){
            LogUtils.e(TAG,"手环");
            braceletSwitch();

        }else if (type == 2){
            LogUtils.e(TAG,"定位器");
            locatorSwitch();
        }
    }


    /**
     * 定位器切换
     */
    private void locatorSwitch() {
        mLtStepGauge.setVisibility(View.GONE);
        mLtHeartRate.setVisibility(View.GONE);
        mLtSleep.setVisibility(View.GONE);
    }


    /**
     * 手环切换
     */
    private void braceletSwitch() {
        mLtStepGauge.setVisibility(View.GONE);
        mLtHeartRate.setVisibility(View.GONE);
        mLtSleep.setVisibility(View.GONE);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        LogUtils.e(TAG,"地图点击了");
        if (mDataBean.getAddress() != null){
            Intent intent = new Intent(getActivity(),MapActivity.class);
            intent.putExtra("location", mDataBean);
            intent.putExtra("deviceId",mBindingListBean.getDeviceId());
            LogUtils.e(TAG,"deviceId " + mBindingListBean.getDeviceId());
            getActivity().startActivity(intent);
        }else {
            LogUtils.e(TAG,"无数据");
        }

    }


    /**
     * 获取最新一条定位消息成功
     * @param data
     */
    public void onLocationSuccess(ResponseInfoModel.DataBean data) {
        if (mCancelDialog != null){
            mCancelDialog.cancel();
        }

        mDataBean = data;
        mTvShowAddress.setText(getString(R.string.place));
        mTvShowTime.setVisibility(View.VISIBLE);
        mTvAddress.setText(data.getAddress());
        mTvTime.setText(data.getLocationTime());
        mMapPresenter.setLatLan(data.getLat(),data.getLng());

        LogUtils.e(TAG,data.getAddress());
        LogUtils.e(TAG,data.getLat()+"");
        LogUtils.e(TAG,mBindingListBean.getNickName()+"");
    }


    /**
     * 无定位信息
     */
    public void noLocation() {
        if (mCancelDialog != null){
        mCancelDialog.cancel();
        }

        mDataBean = new ResponseInfoModel.DataBean();
        mTvTime.setText("");
        mTvAddress.setText("");
        mTvShowAddress.setText(getString(R.string.no_location_information));
        mTvShowTime.setVisibility(View.GONE);
        mAMap.clear();
    }


    /**
     * 添加的新的设备 更新定位数据
     * @param isAddDevices
     */
    public void setNewDevice(boolean isAddDevices) {
        LogUtils.d(TAG,"添加的新的设备 更新定位数据");
        if (isAddDevices){
            getLastLocationInfo(MyApplication.sToken,mBindingListBean.getDeviceId());
        }else {
            LogUtils.e(TAG,"没有添加新的设备 ");
        }
    }


    public void cancel(CancelDialog cancelDialog){
        mCancelDialog = cancelDialog;
    }


    public interface CancelDialog{
        void cancel();
    }
}
