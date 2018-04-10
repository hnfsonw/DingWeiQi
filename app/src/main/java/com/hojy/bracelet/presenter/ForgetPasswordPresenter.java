package com.hojy.bracelet.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.ForgetPasswordActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/5  11:28.
 * 描    述:
 */

public class ForgetPasswordPresenter extends BasePresenter{

    private static final String TAG = "ForgetPasswordActivity";
    private Context mContext;

    private ForgetPasswordActivity mForgetPasswordActivity;
    public Call<ResponseInfoModel> mCheckAcount;


    public ForgetPasswordPresenter(Context context, ForgetPasswordActivity forgetPasswordActivity) {
        super(context);
        mContext = context;
        mForgetPasswordActivity = forgetPasswordActivity;
    }


    /**
     * 验证手机号码有没注册
     *
     * @param phone
     */
    public void checkAcount(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mForgetPasswordActivity.phoneEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mForgetPasswordActivity.phoneError();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("acountName", phone);
        LogUtils.d(TAG, "验证手机号码有没注册: " + String.valueOf(params));

        mCheckAcount = mService.queryAcountByName(params);
        mForgetPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCheckAcount.enqueue(mCallback);

    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.d(TAG, "onSuccess: " + data.getMsg());
        LogUtils.e(TAG,data.getData().isResult()+"");
        if (data.getData().isResult()){
        mForgetPasswordActivity.onSuccess();
        }else {
            mForgetPasswordActivity.printn(mContext.getString(R.string.no_register));
        }
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mForgetPasswordActivity.onError(s.getMsg());
        LogUtils.d(TAG, "onFaiure: " + s.getMsg());
    }



}
