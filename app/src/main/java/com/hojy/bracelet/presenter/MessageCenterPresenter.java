package com.hojy.bracelet.presenter;

import android.content.Context;

import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.MessageCenterActivity;

/**
 * Created by LoyBin on 18/3/14  16:56.
 * 描    述:
 */

public class MessageCenterPresenter extends BasePresenter{

    private Context mContext;
    private MessageCenterActivity mMessageCenterActivity;

    public MessageCenterPresenter(Context context, MessageCenterActivity messageCenterActivity) {
        super(context);
        mContext = context;
        mMessageCenterActivity = messageCenterActivity;
    }

    @Override
    protected void onSuccess(ResponseInfoModel data) {

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {

    }
}
