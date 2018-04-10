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
import com.hojy.bracelet.ui.activity.ChangePasswordActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.MD5Util;
import com.hojy.bracelet.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述:  修改密码的业务逻辑
 */
public class EditPasswordPresenter extends BasePresenter {

    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    private static final String TAG = "EditPasswordPresenter";
    private boolean flag = true;

    private ChangePasswordActivity mModifyPasswordActivity;

    private Context mContext;
    public Call<ResponseInfoModel> mSendCheckCode;
    public Call<ResponseInfoModel> mEdit;
    public String mMd5Password;
    private String mPhone;

    public EditPasswordPresenter(Context context, ChangePasswordActivity modifyPasswordActivity) {
        super(context);
        mContext = context;
        mModifyPasswordActivity = modifyPasswordActivity;
    }


    /**
     * 验证身份信息
     *
     * @param phone          手机号码
     * @param code           验证码
     * @param password       密码
     * @param configPassword 确认密码
     */
    public void resetPassword(String phone, String code, String password, String configPassword) {
        if (TextUtils.isEmpty(phone)) {
            mModifyPasswordActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)) {
            mModifyPasswordActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            mModifyPasswordActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mModifyPasswordActivity.passwordInEmpty();
            return;
        }

        if (!UserUtil.judgePassword(password)) {
            mModifyPasswordActivity.passwordInconformity();
            return;
        }

        reset(phone, code, password);
//        if (password.equals(configPassword) && UserUtil.judgePassword(password)) {
//            reset(phone, code, password);
//        } else {
//            mModifyPasswordActivity.passwordError();
//        }

    }


    /**
     * 发送验证码
     */
    public void resetCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mModifyPasswordActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mModifyPasswordActivity.phoneError();
            return;
        }

        sendCode(phone);
    }


    /**
     * 请求网络发送验证码
     *
     * @param phone
     */
    private void sendCode(String phone) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        LogUtils.d(TAG, "sendCode: " + String.valueOf(params));

        mSendCheckCode = mService.sendCheckCode(params);
        mModifyPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mSendCheckCode.enqueue(mCallback);
    }


    /**
     * 请求网络 修改密码
     *
     * @param phone
     * @param code
     * @param password
     */
    private void reset(String phone, String code, String password) {
        mMd5Password = MD5Util.getInstance().getMD5String(password);
        mPhone = phone;
        HashMap<String, String> params = new HashMap<>();
        params.put("acountName", phone);
        params.put("password", mMd5Password);
        params.put("code", code);
        Log.d(TAG, "修改密码: " + String.valueOf(params));

        mEdit = mService.resetAcountPassword(params);
        mModifyPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mEdit.enqueue(mCallback2);

    }


    /**
     * 验证码发送成功的回调
     *
     * @param data
     */
    @Override
    protected void onSuccess(ResponseInfoModel data) {
        upTime();

        //通知v层验证码发送成功
        mModifyPasswordActivity.CheckCode();
    }


    /**
     * 验证码80001失败的回调
     *
     * @param data
     */
    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.d(TAG, "onFaiure: " + data.getMsg());
        mModifyPasswordActivity.error(data.getMsg());
    }


    /**
     * 找回密码成功的回调
     *
     * @param body
     */
    @Override
    protected void onComplete(ResponseInfoModel body) {
        LogUtils.d(TAG, "onSuccess: " + body.getMsg());
        mModifyPasswordActivity.editSuccess(mMd5Password,mPhone);

    }


    /**
     * 找回密码失败的回调
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, "onError: " + body.getMsg());
        mModifyPasswordActivity.editError(body.getMsg());
    }



    /**
     * 验证码发送成功倒计时120秒
     */
    protected void upTime() {
        mModifyPasswordActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
        mModifyPasswordActivity.mTvSendCode.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                for (; time > 0; time--) {
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
                    mModifyPasswordActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mModifyPasswordActivity.mTvSendCode.setText(MyApplication.sInstance.getString(R.string.To_Resend));
                    time = 120;
                    mModifyPasswordActivity.mTvSendCode.setEnabled(true);
                    flag = false;
                    break;
            }

        }
    };
    protected int time = 120;


}
