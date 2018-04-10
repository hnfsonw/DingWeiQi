package com.hojy.bracelet.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.ForgetActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.MD5Util;
import com.hojy.bracelet.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/5  11:33.
 * 描    述:
 */

public class ForgetPresenter extends BasePresenter{

    private static final java.lang.String TAG = "ForgetActivity";
    private Context mContext;
    private ForgetActivity mForgetActivity;
    public Call<ResponseInfoModel> mSendCheckCode;
    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    protected int mTime = 120;
    public Call<ResponseInfoModel> mEdit;

    public ForgetPresenter(Context context,ForgetActivity forgetActivity) {
        super(context);
        mContext = context;
        mForgetActivity = forgetActivity;
    }


    /**
     * 发送验证码
     * @param phone
     */
    public void checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mForgetActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mForgetActivity.phoneError();
            return;
        }

        sendCode(phone);
    }


    /**
     * 验证身份信息
     *
     * @param code           验证码
     * @param password       密码
     * @param configPassword 确认密码
     */
    public void resetPassword(String phone, String code, String password, String configPassword) {

        if (TextUtils.isEmpty(code)) {
            mForgetActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mForgetActivity.passwordInEmpty();
            return;
        }

        if (!UserUtil.judgePassword(password)) {
            mForgetActivity.passwordInconformity();
            return;
        }


        reset(phone, code, password);
//        if (password.equals(configPassword) && UserUtil.judgePassword(password)) {
//            reset(phone, code, password);
//        } else {
//            mForgetActivity.passwordError();
//        }

    }

    /**
     * 请求网络 修改密码
     *
     * @param code
     * @param password
     */
    private void reset(String phone, String code, String password) {
        String md5Password = MD5Util.getInstance().getMD5String(password);
        HashMap<String, String> params = new HashMap<>();
        params.put("acountName", phone);
        params.put("password", md5Password);
        params.put("code", code);
        LogUtils.d(TAG, "请求网络 修改密码: " + String.valueOf(params));

        mEdit = mService.resetAcountPassword(params);
        mForgetActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mEdit.enqueue(mCallback2);
    }


    /**
     * 请求网络发送验证码
     */
    private void sendCode(String phone) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        LogUtils.d(TAG, "请求网络发送验证码: " + String.valueOf(params));

        mSendCheckCode = mService.sendCheckCode(params);
        mForgetActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mSendCheckCode.enqueue(mCallback);
    }


    @Override
    protected void onComplete(ResponseInfoModel data) {
        LogUtils.d(TAG, "onComplete: " + data.getMsg());
        mForgetActivity.editSuccess();
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, "onError: " + body.getMsg());
        mForgetActivity.editError(body.getMsg());

    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        upTime();
        mForgetActivity.CheckCode();
    }


    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.d(TAG, "onFaiure: " + data.getMsg());
        mForgetActivity.error(data.getMsg());
    }


    protected void upTime() {
        mForgetActivity.mTvSendCode.setText(mTime + mContext.getString(R.string.Second));
        mForgetActivity.mTvSendCode.setEnabled(false);
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
                    mForgetActivity.mTvSendCode.setText(mTime + mContext.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mForgetActivity.mTvSendCode.setText(mContext.getString(R.string.To_Resend));
                    mTime = 120;
                    mForgetActivity.mTvSendCode.setEnabled(true);
                    break;
            }

        }
    };


}
