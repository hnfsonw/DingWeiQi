package com.hojy.bracelet.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.LoginActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.MD5Util;
import com.hojy.bracelet.util.SharedPreferencesUtils;
import com.hojy.bracelet.util.UserUtil;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/6  16:26.
 * 描    述:
 */

public class LoginPresenter extends BasePresenter{
    private static final String TAG = "LoginActivity";
    private Context mContext;
    private LoginActivity mLoginActivity;
    private String mMD5Password;
    private String mPassword;
    public Call<ResponseInfoModel> mLogin;
    private String mPhone;
    private Call<ResponseInfoModel> mUpdateAcountCommuIdByAcountId;


    public LoginPresenter(Context context, LoginActivity loginActivity) {
        super(context);
        mContext = context;
        mLoginActivity = loginActivity;
    }


    /**
     * login_background
     * @param phone
     * @param password
     */
    public void login(String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            mLoginActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)) {
            mLoginActivity.phoneError();
            return;
        }

        if (!TextUtils.isEmpty(password)) {
            onStartLogin(phone, password);
        } else {
            mLoginActivity.passwordIsEmpty();//            密码不能为空
        }

    }


    /**
     * login_background
     *
     * @param phone
     * @param password
     */
    private void onStartLogin(String phone, String password) {
        mMD5Password = MD5Util.getInstance().getMD5String(password);
        LogUtils.e(TAG, "MD5 :" + mMD5Password);
        mPassword = password;
        mPhone = phone;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("acountName", phone);
        hashMap.put("password", mMD5Password);
        //登陆接口增加参数
        LogUtils.d(TAG, "login_background: " + String.valueOf(hashMap));
        mLogin = mService.acountLogin(hashMap);
        mLoginActivity.showLoading(mContext.getString(R.string.Login_Loding),mContext);
        mLogin.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.d(TAG, "onSuccess: " + data.getData().getToken());
        LogUtils.e(TAG,"getCommuId = "+data.getData().getCommuId());
        MyApplication.sToken = data.getData().getToken();
        MyApplication.sAcountName = mPhone;
        MyApplication.sAcountId = data.getData().getAcountId();
        SharedPreferencesUtils.setParam(mContext,"token",data.getData().getToken());
        SharedPreferencesUtils.setParam(mContext,"phone",mPhone);
        SharedPreferencesUtils.setParam(mContext,"password",mPassword);
        SharedPreferencesUtils.setParam(mContext,"acountId",data.getData().getAcountId());
        SharedPreferencesUtils.setParam(mContext,"login",true);

        chekCommuId(data.getData().getCommuId());


    }


    /**
     * 验证极光的ID
     * @param commuId
     */
    private void chekCommuId(String commuId) {
        String registrationID = JPushInterface.getRegistrationID(mContext);
        LogUtils.e(TAG,"registrationID = " + registrationID);
        if (TextUtils.isEmpty(registrationID)){
            SharedPreferencesUtils.setParam(mContext,"isUpData",false);
            mLoginActivity.loginSuccess();
            return;
        }


        if (commuId == null){
            //未设置 极光注册ID
                SharedPreferencesUtils.setParam(mContext,"registrationID",registrationID);
                updateAcountCommuIdByAcountId(MyApplication.sToken,MyApplication.sAcountId,registrationID);

        }else {

            if (registrationID == commuId){
                SharedPreferencesUtils.setParam(mContext,"isUpData",true);
                mLoginActivity.loginSuccess();
            }else {
                SharedPreferencesUtils.setParam(mContext,"registrationID",registrationID);
                updateAcountCommuIdByAcountId(MyApplication.sToken,MyApplication.sAcountId,registrationID);
            }

        }
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, "onFaiure: " + s.getMsg());
        mLoginActivity.resultMsg(s.getMsg());
    }


    /**
     * APP更新极光通讯ID
     * @param token
     * @param acountId
     * @param registrationID
     */
    private void updateAcountCommuIdByAcountId(String token, long acountId, String registrationID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("acountId", acountId);
        hashMap.put("token", token);
        hashMap.put("commuId", registrationID);
        LogUtils.e(TAG,"APP更新极光通讯ID " +String.valueOf(hashMap));
        mUpdateAcountCommuIdByAcountId = mService.updateAcountCommuIdByAcountId(hashMap);
        mUpdateAcountCommuIdByAcountId.enqueue(mCallback2);
    }


    @Override
    protected void onComplete(ResponseInfoModel data) {
        LogUtils.e(TAG,"onComplete更新成功 ");
        SharedPreferencesUtils.setParam(mContext,"isUpData",true);
        mLoginActivity.loginSuccess();
    }


    @Override
    protected void onError(ResponseInfoModel s) {
        LogUtils.e(TAG,"onError "+s.getMsg());
        SharedPreferencesUtils.setParam(mContext,"isUpData",false);
        mLoginActivity.loginSuccess();
    }
}
