package com.hojy.bracelet.presenter;

import android.content.Context;

import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.SplashActivity;

/**
 * Created by LoyBin on 18/3/12  10:14.
 * 描    述:
 */

public class SplashPresenter extends BasePresenter{


    public SplashPresenter(Context context, SplashActivity splashActivity) {
        super(context);
    }

    @Override
    protected void onSuccess(ResponseInfoModel data) {

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {

    }
}
