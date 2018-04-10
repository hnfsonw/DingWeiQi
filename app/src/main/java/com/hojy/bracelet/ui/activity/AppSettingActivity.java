package com.hojy.bracelet.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.factory.FragmentFactory;
import com.hojy.bracelet.model.ExitModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.AppSettingPresenter;
import com.hojy.bracelet.ui.view.LinearTvView;
import com.hojy.bracelet.ui.view.RemoveDialog;
import com.hojy.bracelet.ui.view.VesionDialog;
import com.hojy.bracelet.util.FileUtils;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;
import com.hojy.bracelet.util.UIUtils;
import com.hojy.bracelet.util.UserUtil;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/3/12  10:38.
 * 描    述:
 */

public class AppSettingActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener {


    private static final String TAG = "AppSettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.ll_push)
    LinearLayout mLlPush;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.lt_change_password)
    LinearTvView mLtChangePassword;

    @BindView(R.id.lt_check_version_update)
    LinearTvView mLtCheckTheUpdate;

    @BindView(R.id.lt_clear_the_cache)
    LinearTvView mLtClearTheCache;

    @BindView(R.id.tv_current_account)
    TextView mTvCurrentAccount;

    @BindView(R.id.tv_push_switch)
    TextView mTvPushSwitch;

    @BindView(R.id.btn_log_out)
    Button mBtnLogOut;

    @BindView(R.id.switch_button)
    SwitchButton mSwitchButton;
    private RemoveDialog mRemoveDialog;
    private boolean isClertCache;
    private AlertDialog.Builder mDialog;
    private AppSettingPresenter mAppSettingPresenter;
    private ResponseInfoModel.DataBean mResult;
    private VesionDialog mVesionDialog;
    private boolean mIsNetWork4G;
    private boolean mNotificationEnabled;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_app_setting;
    }


    @Override
    protected void init() {
        if (mAppSettingPresenter == null)
        mAppSettingPresenter = new AppSettingPresenter(this,this);

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.app_setting));
        mTvCurrentAccount.setText(MyApplication.sAcountName);
        mLtCheckTheUpdate.setAttribute("V" + UIUtils.getVersion());
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }

        if (mVesionDialog == null) {
            mVesionDialog = new VesionDialog(this, this);
        }
        mNotificationEnabled = UserUtil.isNotificationEnabled(this);
        if (mNotificationEnabled){
            mSwitchButton.setChecked(true);
        }else {
            mSwitchButton.setChecked(false);
        }
        mSwitchButton.setOnCheckedChangeListener(this);

        getFileSIze();
    }


    /**
     * 获取缓存大小
     */
    private void getFileSIze() {
        MyApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                LogUtils.e(TAG, "获取缓存大小");
                String totalCacheSize = "";
                try {
                    totalCacheSize = FileUtils.getTotalCacheSize();
                    LogUtils.e(TAG, "文件大小 " + totalCacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String finalTotalCacheSize = totalCacheSize;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalTotalCacheSize != null) {
                            mLtClearTheCache.setAttribute(finalTotalCacheSize);
                        }
                    }
                });
            }
        });
    }



    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.lt_change_password, R.id.lt_clear_the_cache, R.id.btn_log_out
            ,R.id.lt_check_version_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.lt_change_password:
                toActivity(ChangePasswordActivity.class);
                break;

            case R.id.lt_clear_the_cache:
                isClertCache = true;
                mRemoveDialog.show();
                mRemoveDialog.initTitle(getString(R.string.sure_you_want_to_clear_the_cache), true, getString(R.string.delete_care));
                break;

            case R.id.btn_log_out:
                mRemoveDialog.show();
                mRemoveDialog.initTitle(getString(R.string.confirm_to_exit), false);
                break;

            case R.id.lt_check_version_update:
                mAppSettingPresenter.checkVersion(MyApplication.sToken, "V" + UIUtils.getVersion(), UIUtils.getVersionCode());

                break;

            default:
                    break;
        }
    }


    /**
     * 退出
     */
    private void loginOut() {
        EventBus.getDefault().post(new ExitModel());
        finishActivityByAnimation(this);
    }


    /**
     * isClertCache false 退出
     */
    @Override
    public void cancel() {
        if (isClertCache) {
            FileUtils.cleanInternal();
            try {
                mLtClearTheCache.setAttribute(FileUtils.getTotalCacheSize());
            } catch (Exception e) {
                e.printStackTrace();
            }

            isClertCache = false;
        }else {
            //退出登录
            loginOut();
        }
    }


    /**
     * 检查版本更新成功的通知
     * @param data
     */
    public void chekVersionSuccess(ResponseInfoModel data) {
        mResult = data.getData();
        Boolean hasNewVesion = mResult.isHasNewVesion();
        if (hasNewVesion) {
            //有版本更新
            LogUtils.d(TAG, "chekVersionSuccess: " + mResult.getUrl());
            mVesionDialog.show();
            mVesionDialog.initUserName(mResult.getDesc());
        } else {
            //当前是最新版本
            printn(getString(R.string.is_currently_the_latest_version));
        }
    }


    @Override
    public void upDate() {
        if (mIsNetWork4G) {
            //提示当前是移动网
            //监听下载进度
            mDialog = new AlertDialog.Builder(this);

            mDialog.setMessage(getString(R.string.mobile_data));
            mDialog.setCancelable(false);
            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            mDialog.setPositiveButton(getString(R.string.local_tyrants_continued),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAppSettingPresenter.download(mResult.getUrl());
                        }
                    });
            mDialog.show();
        } else {
            //当前是wifi
            mAppSettingPresenter.download(mResult.getUrl());
        }
    }


    /**
     * 当前网络是4G的通知
     */
    @Override
    protected void netWork4G() {
        mIsNetWork4G = true;
        LogUtils.e(TAG, "当前网络是4G");
    }


    /**
     * 当前网络是wifi的通知
     */
    @Override
    protected void netWiFi() {
        mIsNetWork4G = false;
        LogUtils.e(TAG, "当前网络是wifi");
    }


    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {

        mNotificationEnabled = UserUtil.isNotificationEnabled(this);
        LogUtils.e(TAG,"notificationEnabled =" + mNotificationEnabled);

        if (!UserUtil.isNotificationEnabled(this) &&  isChecked) {
            mDialog = new AlertDialog.Builder(this);
            mDialog.setMessage("检测到您没有打开通知权限，是否去打开");
            mDialog.setPositiveButton(getString(R.string.open_the), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);

                        localIntent.setClassName("com.android.settings",
                                "com.android.settings.InstalledAppDetails");

                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                getPackageName());
                    }
                    startActivity(localIntent);
                    dialogInterface.dismiss();
                }
            });

            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            mDialog.show();

        }else if (isChecked){
            LogUtils.e(TAG,"打开通知");
            SharedPreferencesUtils.setParam(this,"push",true);
            JPushInterface.resumePush(MyApplication.sInstance);

        }else if (!isChecked){
            LogUtils.e(TAG,"暂停通知");
            SharedPreferencesUtils.setParam(this,"push",false);
            JPushInterface.stopPush(MyApplication.sInstance);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (mSwitchButton == null)
//            return;
//        mNotificationEnabled = UserUtil.isNotificationEnabled(this);
//        if (mNotificationEnabled){
//            mSwitchButton.setChecked(true);
//        }else {
//            mSwitchButton.setChecked(false);
//        }
    }
}
