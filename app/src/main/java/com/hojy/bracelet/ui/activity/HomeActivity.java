package com.hojy.bracelet.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.BaseFragment;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.factory.FragmentFactory;
import com.hojy.bracelet.model.ExitModel;
import com.hojy.bracelet.model.ExtrasModel;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.model.RefreshModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.model.TextModel;
import com.hojy.bracelet.presenter.HomePresenter;
import com.hojy.bracelet.ui.fragment.MapFragemnt;
import com.hojy.bracelet.ui.fragment.MyFragemnt;
import com.hojy.bracelet.ui.view.BottomTab;
import com.hojy.bracelet.ui.view.BottomTabLayout;
import com.hojy.bracelet.ui.view.UnBunDlingDialog;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/2/27  11:29.
 * 描    述:
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    @BindView(R.id.home_container)
    FrameLayout mHomeContainer;

    @BindView(R.id.mBottomTabLayout)
    BottomTabLayout mMBottomTabLayout;

    @BindView(R.id.iv_error)
   public ImageView mIvError;

    @BindView(R.id.include)
    View mView;


    private String[] mTabNames = { "首页", "我的"};
    public static boolean isForeground = false;
    public boolean isOnResume;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String MESSAGE_RECEIVED_ACTION_ID = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION_ID";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private int mQueryState = 0;   //用来控制当前界面不在HomeActivity 刷新UI
    //底部控件
    //tab数据
    private List<BottomTab> mBottomTabs = new ArrayList<>();
    private Fragment fm;
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;
    public boolean isAddDevices = false;

    /**
     * 默认状态下的本地图片
     */
    private int[] mUnSelectIcons = {
            R.mipmap.tabbar_home,
            R.mipmap.home_my};

    /**
     * 默认状态下的本地图片
     */
    private int[] mSelectIcons = {
            R.mipmap.home,
            R.mipmap.home_my2};


    /**
     * 默认状态下的网络图片
     */
    private String[] mUnSelectUrls={
            "http://www.androidstudy.cn/img/tabbar_home.png",
            "http://www.androidstudy.cn/img/tabbar_profile.png"};

    /**
     * 选中状态下的网络图片
     */
    private String[] mSelectUrls={
            "http://www.androidstudy.cn/img/tabbar_home_selected.png",
            "http://www.androidstudy.cn/img/tabbar_profile_highlighted.png"};

    /**
     * 未选中的颜色
     */
    private int mUnSelectColor = R.color.unSelectColor;

    /**
     * 选中的颜色
     */
    private int mSelectColor = R.color.SelectColor;
    private HomePresenter mHomePresenter;
    private Long mAcountId;
    private int mBindingListBeanSize;
    private MessageReceiver mMessageReceiver;
    private boolean mIsUpData; // 是否穿入了极光注册ID
    private Gson mGson;
    private Map<String, Object> mCacheMap;
    public int mNotifactionId;
    private UnBunDlingDialog mUnBunDlingDialog;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }


    @Override
    protected void init() {
        mIsUpData = (boolean) SharedPreferencesUtils.getParam(this, "isUpData", false);
        registerMessageReceiver();
        if (mHomePresenter == null)
        mHomePresenter = new HomePresenter(this,this);

        if (mGson == null){
            mGson = new Gson();
        }
        if (mUnBunDlingDialog == null)
        mUnBunDlingDialog = new UnBunDlingDialog(this, this);

        EventBus.getDefault().register(this);
        initView();
        initData(false);
    }


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }


    //更新了设备
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpData(NewDeviceModel newDeviceModel) {
        LogUtils.e(TAG,"更新了设备");
        isAddDevices = true;
        mHomePresenter.queryBindingListByAcountId(mAcountId, MyApplication.sToken);
    }


    //退出 销毁
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExit(ExitModel exitModel) {
        LogUtils.e(TAG,"退出");
        JPushInterface.stopPush(MyApplication.sInstance);
        SharedPreferencesUtils.setParam(this,"login",false);
        FragmentFactory.mCacheFragmentMap.clear();
        MyApplication.sDeivceNumber = 0;
        MyApplication.sAcountId = -1;
        MyApplication.sAcountName = "";
        toActivity(LoginActivity.class,0);
        finishActivityByAnimation(this);

    }


    //刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshModel refreshModel) {
        mView.setVisibility(View.GONE);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 初始化数据
     * @param isUrl
     */
    private void initData(boolean isUrl) {
        mBottomTabs.clear();
        mAcountId = (Long) SharedPreferencesUtils.getParam(this, "acountId", 0L);
        for (int i = 0; i < mUnSelectIcons.length; i++){
            BottomTab mBottomTab = new BottomTab(mTabNames[i],mUnSelectColor,
                    mSelectColor,mUnSelectIcons[i],mSelectIcons[i],isUrl?mUnSelectUrls[i]:null,isUrl?mSelectUrls[i]:null);
            mBottomTabs.add(mBottomTab);
        }
        mMBottomTabLayout.setBottomTabData(mBottomTabs);
        mHomePresenter.queryBindingListByAcountId(mAcountId, MyApplication.sToken);

        if (!mIsUpData){
            String registrationID = JPushInterface.getRegistrationID(this);
            LogUtils.e(TAG,"registrationID = " + registrationID+"更新极光ID 未成功");
            if (TextUtils.isEmpty(registrationID)){
                LogUtils.e(TAG,"极光注册ID为空");
            }else {
                mHomePresenter.updateAcountCommuIdByAcountId(MyApplication.sToken,MyApplication.sAcountId,registrationID);
            }
        }else {
            LogUtils.e(TAG,"极光ID 更新成功");
        }
    }


    /**
     * 获取极光缓存消息
     */
    private void loadCachedMessages() {
        try {
        mCacheMap = MyApplication.getCacheMap();
        LogUtils.d(TAG,"mCacheMap.size =  "+mCacheMap.size());
        for (int i = 0; i < mCacheMap.size(); i++) {
            LogUtils.d(TAG," i = " +i +Thread.currentThread().getName());
            if (mCacheMap.containsKey("JPush"+i)){
                Bundle bundle = (Bundle) mCacheMap.get("JPush"+i);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.e(TAG,"缓存消息 +"+bundle.getString(JPushInterface.EXTRA_EXTRA));
                TextModel textModel = mGson.fromJson(extras, TextModel.class);
                if (textModel.getFrom() != null){
                ExtrasModel extrasModel = mGson.fromJson(textModel.getFrom(), ExtrasModel.class);
                setCostomMsg(message,extrasModel);
                mCacheMap.remove("JPush"+i);
                if (mCacheMap.size() >= 1){
                loadCachedMessages();
                }
                LogUtils.e(TAG," remove mCacheMap.size = "+ mCacheMap.size());
                }
            }
        }
         }catch (Exception e){
            LogUtils.e(TAG,"获取极光缓存消息异常" +e.getMessage());
        }
    }


    private void initView() {
        mMBottomTabLayout.setOnTabChangeListener(new BottomTabLayout.OnTabChangeListener() {
            @Override
            public void onTabSelect(int position) {
                if (mBindingListBeans == null){
                    mHomePresenter.queryBindingListByAcountId(mAcountId, MyApplication.sToken);
                    printn(getString(R.string.in_the_connection));
                    return;
                }
                //没有设备
                if (mBindingListBeanSize == 0 && position == 0){
                    onTabItemSelected(2);
                }else if (mBindingListBeanSize == 0 && position == 1){

                    onTabItemSelected(position);
                }else {
                    onTabItemSelected(position);
                }

                if (position == 0 && mBindingListBeanSize >= 1){
                    if (isAddDevices){
                        MapFragemnt mapFragemnt = (MapFragemnt) fm;
                        mapFragemnt.setList(mBindingListBeans);
                        mapFragemnt.setNewDevice(isAddDevices);
                        isAddDevices = false;
                        showLoading("",HomeActivity.this);
                        mapFragemnt.cancel(new MapFragemnt.CancelDialog() {
                            @Override
                            public void cancel() {
                                dismissLoading();
                            }
                        });
                    }

                }

                if (position == 1 && mBindingListBeanSize >= 1){
                    MyFragemnt myFragemnt = (MyFragemnt) fm;
                    myFragemnt.setData(mBindingListBeans);
                }
            }

            @Override
            public void onTabSelected(int position) {
            }
        });
    }


    private void onTabItemSelected(int position) {
        changeFragment(FragmentFactory.createFragment(position));
    }


    //切换fragment
    public void changeFragment(Fragment ment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fm != null){
            transaction.hide(fm);
        }
        // isAdded:是否被添加过 被添加过 is true 反之
        if (!ment.isAdded()) {
            // 调用replace 添加fragment
            transaction.add(R.id.home_container, ment);
        } else {
            // 显示出来
            transaction.show(ment);
        }
        // 提交
        transaction.commit();

        // 记录当前的Fragment
        fm = ment;
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    LogUtils.d(TAG,"messge = " +messge);
                    LogUtils.d(TAG," extras= " +extras);
                    TextModel textModel = mGson.fromJson(extras, TextModel.class);
                    LogUtils.e(TAG,"textModel.getFrom() = " +textModel.getFrom());
                    ExtrasModel extrasModel = mGson.fromJson(textModel.getFrom(), ExtrasModel.class);
                    setCostomMsg(messge,extrasModel);
                }else if (MESSAGE_RECEIVED_ACTION_ID.equals(intent.getAction())){
                    mNotifactionId = intent.getIntExtra("id", 0);
                    LogUtils.e(TAG,"通知栏ID = "+ mNotifactionId);
                }
            } catch (Exception e){
                LogUtils.e(TAG,"极光推送异常"+e.getMessage());
            }
        }
    }


    private void setCostomMsg(final String messge, final ExtrasModel param) {
        switch (param.getType()){
            case 1:
                if (param.getCode() == 1){
                    //普通成员发起邀请绑定设备
                    mHomePresenter.sendBindingEquipment(messge,param);

                }else if (param.getCode() == 2){
                    //同意或拒绝收到的消息
                    mUnBunDlingDialog.show();
                    mUnBunDlingDialog.setIsCanl(false);
                    mUnBunDlingDialog.setTvRemove(messge);

                    mHomePresenter.ChekReplayStatus(param.getParam().getReplayStatus());
                }else if (param.getCode() == 3){
                    mUnBunDlingDialog.show();
                    mUnBunDlingDialog.setIsCanl(false);
                    mUnBunDlingDialog.setTvRemove(messge);

                    LogUtils.e(TAG,"收到被解绑的通知");
                    isAddDevices = true;
                    mHomePresenter.queryBindingListByAcountId(mAcountId, MyApplication.sToken);
                    if (MyApplication.sDeviceId == param.getParam().getDeviceId()){
                        LogUtils.e(TAG,"当前界面是自己");
                    }else {
                        LogUtils.e(TAG,"当前是其它设备");
                    }

                }else if (param.getCode() == 4){
                    mUnBunDlingDialog.show();
                    mUnBunDlingDialog.setIsCanl(false);
                    mUnBunDlingDialog.setTvRemove(messge);
                    LogUtils.d(TAG,"收到移交的管理员消息");
//                    if (MyApplication.sDeviceId != param.getParam().getDeviceId()){
//                        LogUtils.e(TAG,"不是当前设备移交的管理员消息");
//                        return;
//                    }
                    isAddDevices = true;
                    mHomePresenter.queryBindingListByAcountId(mAcountId, MyApplication.sToken);
                }

                break;
        }
    }


    /**
     * 设置小红点
     * @param view
     */
    public void onTabNum(View view){
        mMBottomTabLayout.setBottomTabNum(new Random().nextInt(4),new Random().nextInt(100));
    }

    /**
     * 取消小红点
     * @param view
     */
    public void onClearTabNum(View view){
        mMBottomTabLayout.clearAllBottomTabNum();
    }


    @Override
    protected void dismissNewok() {
        if (mHomePresenter.mResponseInfoModelCall != null)
            mHomePresenter.mResponseInfoModelCall.cancel();
    }


    /**
     * 查询设备列表成功
     * @param bindingList
     * isAddDevices true  有新的设备或被解绑 刷新
     */
    public void querySuccess(List<ResponseInfoModel.DataBean.BindingListBean> bindingList) {
        LogUtils.e(TAG," 查询设备列表成功 isOnResume " + isOnResume);
        mBindingListBeans = bindingList;
        mBindingListBeanSize = mBindingListBeans.size();
        if (isAddDevices){
            mQueryState = 1;
            if (isOnResume){
                MyApplication.sDeivceNumber = mBindingListBeanSize -1;
                mMBottomTabLayout.setCurrentTab(1);
                onTabItemSelected(1);
                MyFragemnt myFragemnt = (MyFragemnt) fm;
                myFragemnt.setData(mBindingListBeans);
                mQueryState = 0;
            }

        }else {
            if (mBindingListBeanSize == 0){
                onTabItemSelected(2);
            }else {
                mMBottomTabLayout.setCurrentTab(0);
                onTabItemSelected(0);
                MapFragemnt mapFragemnt = (MapFragemnt) fm;
                mapFragemnt.setData(mBindingListBeans);
            }
        }

        loadCachedMessages();

    }


    @Override
    protected void noNetwork() {
        LogUtils.e(TAG,"当前有网络异常");
    }


    @Override
    protected void theNetwork() {
        LogUtils.e(TAG,"当前有网络正常");
    }


    @Override
    protected void onResume() {
        isForeground = true;
        isOnResume = true;
        LogUtils.e(TAG,"onResume + "+isAddDevices  +"~~  mQueryState ="+mQueryState);
        if (isAddDevices){
            if (mQueryState == 1){
                MyApplication.sDeivceNumber = mBindingListBeanSize -1;
                mMBottomTabLayout.setCurrentTab(1);
                onTabItemSelected(1);
                BaseFragment baseFragment = (BaseFragment) fm;
                baseFragment.setData(mBindingListBeans);
                mQueryState = 0;
            }

        }

        super.onResume();
    }


    @Override
    protected void onPause() {
        isOnResume = false;
        LogUtils.e(TAG,"onPause =" +isOnResume);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        isForeground = false;
        super.onDestroy();
    }


    @Override
    public void cancel() {
        mHomePresenter.agreedTo();
    }

    @Override
    public void dismiss() {
        mHomePresenter.refusedTo();
    }
}
