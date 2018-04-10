package com.hojy.bracelet.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.RegisterActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.MD5Util;
import com.hojy.bracelet.util.SharedPreferencesUtils;
import com.hojy.bracelet.util.UIUtils;
import com.hojy.bracelet.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/5  10:57.
 * 描    述:
 */

public class RegisterPresenter extends BasePresenter{

    private static final java.lang.String TAG = "RegisterActivity";
    private Context mContext;
    private RegisterActivity mRegisterActivity;
    public Call<ResponseInfoModel> mSendCheckCode;
    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    protected int mTime = 120;
    public Call<ResponseInfoModel> mRegister;
    private String mMD5Password;
    private String mPassword;
    private String mPhone;
    public Call<ResponseInfoModel> mLogin;

    public RegisterPresenter(Context context, RegisterActivity registerActivity) {
        super(context);
        mContext = context;
        mRegisterActivity = registerActivity;
    }


    /**
     *  验证电话是否可以发验证码
     * @param phone
     */
    public void checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mRegisterActivity.phoneError();
            return;
        }

        sendCode(phone);
    }


    /**
     * 验证注册的帐号信息
     */
    public void registerNumber(String userName, String code, String passWord, String confrimPassword) {
        if (TextUtils.isEmpty(userName)) {
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(userName)) {
            mRegisterActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            mRegisterActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            mRegisterActivity.passWordIsEmpty();
            return;
        }

        if (!UserUtil.judgePassword(passWord)) {
            mRegisterActivity.passwordInconformity();
            return;
        }

        String md5PassWord = MD5Util.getInstance().getMD5String(passWord);
        toRegister(userName, code, md5PassWord);
//        if (passWord.equals(confrimPassword) && UserUtil.judgePassword(passWord)) {
//            String md5PassWord = MD5Util.getInstance().getMD5String(passWord);
//            toRegister(userName, code, md5PassWord);
//        } else {
//            mRegisterActivity.passwordError();
//        }
    }


    /**
     * 注册请求网络
     *
     * @param phone    电话
     * @param code     验证码
     * @param passWord 密码
     */
    private void toRegister(String phone, String code, String passWord) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("acountName", phone);
        paramsMap.put("password", passWord);
        paramsMap.put("code", code);
        LogUtils.e(TAG, "注册请求网络: " + String.valueOf(paramsMap));
        //执行enqueue
        mRegister = mService.register(paramsMap);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mRegister.enqueue(mCallback2);
    }


    /**
     * 请求网络发送验证码
     */
    private void sendCode(String phone) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        LogUtils.d(TAG, "请求网络发送验证码: " + String.valueOf(params));

        mSendCheckCode = mService.sendCheckCode(params);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mSendCheckCode.enqueue(mCallback);
    }


    @Override
    protected void onComplete(ResponseInfoModel body) {
        LogUtils.d(TAG, "onSuccess: " + body.getMsg());
        mRegisterActivity.succeed();
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, "onError: " + body.getMsg());
        mRegisterActivity.onError(body.getMsg());
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        if (mPassword == null){
            upTime();
            //通知v层验证码发送成功
            mRegisterActivity.CheckCode();
        }else {
            MyApplication.sToken = data.getData().getToken();
            MyApplication.sAcountName = mPhone;
            MyApplication.sAcountId = data.getData().getAcountId();
            SharedPreferencesUtils.setParam(mContext,"token",data.getData().getToken());
            SharedPreferencesUtils.setParam(mContext,"phone",mPhone);
            SharedPreferencesUtils.setParam(mContext,"password",mPassword);
            SharedPreferencesUtils.setParam(mContext,"acountId",data.getData().getAcountId());
            SharedPreferencesUtils.setParam(mContext,"login",true);
            SharedPreferencesUtils.setParam(mContext,"token",data.getData().getToken());
            SharedPreferencesUtils.setParam(mContext,"acountName",mPhone);
            mRegisterActivity.loginSuccess();
        }
    }

    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.d(TAG, "onFaiure: " + data.getMsg());
        mRegisterActivity.error(data.getMsg());
    }



    protected void upTime() {
        mRegisterActivity.mTvSendCode.setText(mTime + mContext.getString(R.string.Second));
        mRegisterActivity.mTvSendCode.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                for (; mTime > 0; mTime--) {
                    SystemClock.sleep(1000);
                    mHandler.sendEmptyMessage(TIME_MINUS);
                }
                mHandler.sendEmptyMessage(TIME_IS_OUT);
            }
        }.start();
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MINUS:
                    mRegisterActivity.mTvSendCode.setText(mTime + mContext.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mRegisterActivity.mTvSendCode.setText(mContext.getString(R.string.To_Resend));
                    mTime = 120;
                    mRegisterActivity.mTvSendCode.setEnabled(true);
                    break;
            }

        }
    };


    /**
     * login_background
     * @param phone
     * @param password
     */
    public void login(String phone, String password) {
        mMD5Password = MD5Util.getInstance().getMD5String(password);
        LogUtils.e(TAG, "MD5 :" + mMD5Password);
        mPassword = password;
        mPhone = phone;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("acountName", phone);
        hashMap.put("password", mMD5Password);
        //登陆接口增加参数
        Log.d(TAG, "login_background: " + String.valueOf(hashMap));
        mLogin = mService.acountLogin(hashMap);
        mRegisterActivity.showLoading(mContext.getString(R.string.Login_Loding),mContext);
        mLogin.enqueue(mCallback);
    }
}
