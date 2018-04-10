package com.hojy.bracelet.presenter;

import android.content.Context;
import android.util.Log;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.SelectUserActivity;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:24
 * 描   述: 请选择用户的业务逻辑
 */
public class SelectUserPresenter extends BasePresenter {

    private static final String TAG = "SelectUserActivity";
    private Context mContext;
    private SelectUserActivity mSelectUserActivity;
    private boolean mIsShow;
    public Call<ResponseInfoModel> mGetGroupMemberList;
    public Call<ResponseInfoModel> mDisBandOneAcount;
    public Call<ResponseInfoModel> mDisBandAcountAndChangeAdmin;

    public SelectUserPresenter(Context context, SelectUserActivity selectUserActivity) {
        super(context);
        mContext = context;
        mSelectUserActivity = selectUserActivity;

    }


    /**
     * 获取设备绑定成员列表
     *
     * @param deviceId
     * @param token
     */
    public void loadingList(long deviceId, String token, boolean isShow) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("deviceId", deviceId);

        LogUtils.e(TAG,"获取设备绑定成员列表 " +String.valueOf(paramsMap));
        mGetGroupMemberList = mService.queryAcountListByDeviceId(paramsMap);
        if (!isShow) {
            mIsShow = false;
            mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mGetGroupMemberList.enqueue(mCallback);
    }


    /**
     * 获取群组列表成功的回掉
     *
     * @param data
     */
    @Override
    protected void onSuccess(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getMsg());
        List<ResponseInfoModel.DataBean.AcountListBean> memberList = data.getData().getAcountList();

        mSelectUserActivity.onSuccess(memberList);
    }


    /**
     * 获取群组列表失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, "onFaiure: " + s.getMsg());
    }


    /**
     * 解除单个普通用户
     *
     * @param memberListBean
     * @param token
     */
    public void removeOrdinary(ResponseInfoModel.DataBean.AcountListBean memberListBean, String token, int deviceId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", memberListBean.getAcountId());
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        LogUtils.d(TAG, "解除单个普通用户: " + String.valueOf(paramsMap));
        mDisBandOneAcount = mService.unbindDevice(paramsMap);
        mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandOneAcount.enqueue(mCallback2);
    }


    @Override
    protected void onComplete(ResponseInfoModel body) {
        //回掉解除普通成员
        if (mSelectUserActivity.mOrdinary.equals("ordinary")) {
            mSelectUserActivity.removeSuccess();
            LogUtils.d(TAG, "onSuccess+ordinary: " + body.getMsg());
        }

        //回调移交成功,解除自己
        if (mSelectUserActivity.mManagement.equals("management")) {
            mSelectUserActivity.successManagement();
            LogUtils.d(TAG, "onSuccess + management: " + body.getMsg());
        }


    }


    @Override
    protected void onError(ResponseInfoModel body) {
        //解除普通成员失败
        if (mSelectUserActivity.mOrdinary.equals("ordinary")) {
            mSelectUserActivity.removeError(body.getMsg());
            LogUtils.d(TAG, "onError+ordinary: " + body.getMsg());
        }

        if (mSelectUserActivity.mManagement.equals("management")) {
            mSelectUserActivity.errorManagement(body.getMsg());
            LogUtils.d(TAG, "onError +management : " + body.getMsg());
        }

    }


    /**
     * 移交管理权限,解绑自己
     *
     * @param managementListBean 当前点击的条目Bean.获取要移交的ID
     * @param token
     * @param deviceId
     * @param acountId
     */
    public void adminUnbindDevice(ResponseInfoModel.DataBean.AcountListBean managementListBean,
                              String token, int deviceId, long acountId) {

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("oldAdminAcountId", acountId);
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        paramsMap.put("newAdminAcountId", managementListBean.getAcountId());
        Log.d(TAG, "移交管理权限,解绑自己: " + String.valueOf(paramsMap));

        mDisBandAcountAndChangeAdmin = mService.adminUnbindDevice(paramsMap);
        mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandAcountAndChangeAdmin.enqueue(mCallback2);
    }
}
