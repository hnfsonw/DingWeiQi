package com.hojy.bracelet.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.download.DownloadIntentService;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.AppSettingActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/26  14:jpush_notification_icon.
 * 描    述:
 */

public class AppSettingPresenter extends BasePresenter{

    private static final String TAG = "AppSettingActivity";
    private Context mContext;
    private AppSettingActivity mAppSettingActivity;
    private Call<ResponseInfoModel> mCall;

    public AppSettingPresenter(Context context, AppSettingActivity appSettingActivity) {
        super(context);
        mContext = context;
        mAppSettingActivity = appSettingActivity;
    }

    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.d(TAG, "parserJson: " + data.getMsg());
        mAppSettingActivity.chekVersionSuccess(data);
    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG,"onFaiure "+s.getMsg());
        mAppSettingActivity.printn(s.getMsg());
    }


    /**
     * 检查是否最新版本
     * @param token
     * @param version
     * @param versionCode
     */
    public void checkVersion(String token, String version, int versionCode) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("version", version);
        paramsMap.put("appType", "2");
        paramsMap.put("versionCode", versionCode);

        LogUtils.e(TAG, "检查是否最新版本: " + String.valueOf(paramsMap));
        mCall = mService.checkVersion(paramsMap);
        mAppSettingActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }


    /**
     * 下载
     * @param url
     */
    public void download(String url) {
        if (isServiceRunning(DownloadIntentService.class.getName())) {
            mAppSettingActivity.printn("正在下载");
            return;
        }
        String downloadUrl = "/qqmi/aphone_p2p/TencentVideo_V6.0.0.14297_848.apk";
        Intent intent = new Intent(mAppSettingActivity, DownloadIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putString("download_url", downloadUrl);
        bundle.putInt("download_id", 10);
        bundle.putString("download_file", downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1));
        intent.putExtras(bundle);
        mAppSettingActivity.startService(intent);
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    private boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
