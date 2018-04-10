package com.hojy.bracelet.ui.activity;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.config.Constants;
import com.hojy.bracelet.presenter.SplashPresenter;
import com.hojy.bracelet.util.IOUtils;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/3/12  10:13.
 * 描    述:
 */

public class SplashActivity extends BaseActivity{


    private static final String TAG = "SplashActivity";
    private SplashPresenter mSplashPresenter;
    private Boolean mLogin;
    private Boolean mMLogin;
    private String mToken;
    private long mAcountId;
    private String mAcountName;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        mMLogin = (Boolean) SharedPreferencesUtils.getParam(this, "login", false);
        mToken = (String) SharedPreferencesUtils.getParam(this, "token", "");
        mAcountId = (long) SharedPreferencesUtils.getParam(this, "acountId", 0L);
        mAcountName = (String) SharedPreferencesUtils.getParam(this, "phone", "");
        if (mSplashPresenter == null)
        mSplashPresenter = new SplashPresenter(this,this);

        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMLogin){
                    MyApplication.sToken = mToken;
                    MyApplication.sAcountName = mAcountName;
                    MyApplication.sAcountId = mAcountId;
                    toActivity(HomeActivity.class);
                    finishActivityByAnimation(SplashActivity.this);
                }else {
                    toActivity(LoginActivity.class);
                    finishActivityByAnimation(SplashActivity.this);
                }
            }
        },2000);
        initView();

        File file = new File(Constants.APP_ROOT_PATH + Constants.DOWNLOAD_DIR + "TencentVideo_V6.0.0.14297_848.apk");
        if (file.exists()){
            LogUtils.e(TAG,"下载文件存在");
//            file.delete();
        }


    }


    private void initView() {
        String registrationID = JPushInterface.getRegistrationID(this);
        LogUtils.e(TAG,"registrationID = " + registrationID);
    }

    @Override
    protected void dismissNewok() {

    }
}
