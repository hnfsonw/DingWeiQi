package com.hojy.bracelet.presenter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ExtrasModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.HomeActivity;
import com.hojy.bracelet.ui.view.RemoveDialog;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/7  15:11.
 * 描    述: 首页的业务逻辑
 */

public class HomePresenter extends BasePresenter{

    private static final String TAG = "HomeActivity";
    private Context mContext;
    private HomeActivity mHomeActivity;
    public Call<ResponseInfoModel> mResponseInfoModelCall;
    private Call<ResponseInfoModel> mUpdateAcountCommuIdByAcountId;
    private Call<ResponseInfoModel> mApplyBindDevice;
    private AlertDialog.Builder mBuilder;
    private final NotificationManager mNotificationManager;
    private RemoveDialog mRemoveDialog;
    private ExtrasModel mParam;

    public HomePresenter(Context context, HomeActivity homeActivity) {
        super(context);
        mContext = context;
        mHomeActivity = homeActivity;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mRemoveDialog == null)
            mRemoveDialog = new RemoveDialog(mContext,mHomeActivity);
    }


    /**
     * 查询设备列表
     * @param acountId
     * @param token
     */
    public void queryBindingListByAcountId(long acountId, String token) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("acountId",acountId+"");
        hashMap.put("token",token);
        mHomeActivity.showLoading("",mContext);
        mResponseInfoModelCall = mService.queryBindingListByAcountId(hashMap);
        mResponseInfoModelCall.enqueue(mCallback);
    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.e(TAG,"onSuccess " + data.getData().getBindingList().size());
        mHomeActivity.mIvError.setVisibility(View.GONE);
        mHomeActivity.querySuccess(data.getData().getBindingList());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG,"onFaiure "+s.getMsg());
        mHomeActivity.mIvError.setVisibility(View.VISIBLE);
        mHomeActivity.printn(s.getMsg());
    }


    /**
     * 更新极光注册ID
     * @param token
     * @param acountId
     * @param registrationID
     */
    public void updateAcountCommuIdByAcountId(String token, long acountId, String registrationID) {
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
        LogUtils.e(TAG,"更新成功");
    }

    @Override
    protected void onError(ResponseInfoModel s) {
        LogUtils.e(TAG,"更新失败");
    }


    /**
     * 管理员 同意 或拒绝
     * @param token
     * @param acountId
     * @param deviceId
     * @param replayStatus Y 同意 N 拒绝
     */
    public void replayApplyBindDevice(String token, long acountId, int deviceId, String replayStatus) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("acountId",acountId);
        hashMap.put("deviceId",deviceId);
        hashMap.put("replayStatus",replayStatus);

        LogUtils.e(TAG,"管理员 同意 或拒绝 " +String.valueOf(hashMap));

        mApplyBindDevice = mService.replayApplyBindDevice(hashMap);
        mApplyBindDevice.enqueue(mCallback2);
    }


    /**
     * 普通成员发起邀请绑定设备
     */
    public void sendBindingEquipment(String message,  ExtrasModel param) {
        mParam = param;
        mRemoveDialog.show();
        mRemoveDialog.initTitle(message);

//        mBuilder = new AlertDialog.Builder(mContext);
//
//        mBuilder.setTitle(message);
//
//        mBuilder.setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                replayApplyBindDevice(MyApplication.sToken,param.getParam().getAcountId(),param.getParam().getDeviceId(),"Y");
//                mNotificationManager.cancel(mHomeActivity.mNotifactionId);
//            }
//        });
//        mBuilder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                replayApplyBindDevice(MyApplication.sToken,param.getParam().getAcountId(),param.getParam().getDeviceId(),"N");
//                LogUtils.e(TAG,"ID = " +mHomeActivity.mNotifactionId);
//
//                mNotificationManager.cancel(mHomeActivity.mNotifactionId);
//            }
//        });
//        mBuilder.show();
    }


    /**
     * 收到管理员同意 或拒绝的消息
     * @param replayStatus Y 同意  N 拒绝
     */
    public void ChekReplayStatus(String replayStatus) {
        LogUtils.e(TAG,"replayStatus =" + replayStatus );
        if (replayStatus.equals("Y")){
            LogUtils.e(TAG,"同意");
            mHomeActivity.isAddDevices = true;
             queryBindingListByAcountId(MyApplication.sAcountId, MyApplication.sToken);
            mNotificationManager.cancel(mHomeActivity.mNotifactionId);
        }else {
            LogUtils.e(TAG,"被拒绝");
            mNotificationManager.cancel(mHomeActivity.mNotifactionId);
        }
    }


    /**
     * 同意
     */
    public void agreedTo() {
        replayApplyBindDevice(MyApplication.sToken,mParam.getParam().getAcountId(),mParam.getParam().getDeviceId(),"Y");
        mNotificationManager.cancel(mHomeActivity.mNotifactionId);
    }


    /**
     * 拒绝
     */
    public void refusedTo() {
        replayApplyBindDevice(MyApplication.sToken,mParam.getParam().getAcountId(),mParam.getParam().getDeviceId(),"N");
        LogUtils.e(TAG,"ID = " +mHomeActivity.mNotifactionId);
        mNotificationManager.cancel(mHomeActivity.mNotifactionId);
    }
}
