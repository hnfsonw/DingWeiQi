package com.hojy.bracelet.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BasePresenter;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.ScanActivity;
import com.hojy.bracelet.ui.view.RemoveDialog;
import com.hojy.bracelet.ui.view.SendInvitationDialog;
import com.hojy.bracelet.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by LoyBin on 18/3/5  15:02.
 * 描    述:
 */

public class ScanPresenter extends BasePresenter{

    private static final String TAG = "ScanActivity";
    private Context mContext;
    private ScanActivity mScanActivity;
    private String mIMei;
    public Call<ResponseInfoModel> mQueryBindInfoByImei;
    public Call<ResponseInfoModel> mQueryDeviceAdmin;
    private Call<ResponseInfoModel> mApplyBindDevice;
    private SendInvitationDialog mRemoveDialog;

    public ScanPresenter(Context context, ScanActivity scanActivity) {
        super(context);
        mContext = context;
        mScanActivity = scanActivity;
    }


    /**
     * 根据设备IMEI 查询设备是否有管理员绑定
     * @param imei
     * @param token
     */
    public void queryDeviceAdmin(String imei, String token,long acountId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("imei", imei);
        paramsMap.put("token", token);
        paramsMap.put("acountId",acountId);

        LogUtils.e(TAG,"queryDeviceAdmin " +String.valueOf(paramsMap));
        //执行enqueue
        mQueryDeviceAdmin = mService.queryDeviceAdminByImei(paramsMap);
        mScanActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mQueryDeviceAdmin.enqueue(mCallback2);
    }


    /**
     * ：1：该设备已与该账户绑定；2：该设备已绑定管理员，未与该账户绑定，3：该设备未绑定管理员，未与该账户绑定"
     * @param data
     */
    @Override
    protected void onComplete(ResponseInfoModel data) {
        int hasBind = data.getData().getHasBind();
        switch (hasBind){
            case 1:
                //提示已于该账户绑定
                mScanActivity.printn("该设备已与该账户绑定");
                break;

            case 2:
                //弹出申请的对话框
                if (mRemoveDialog == null)
                mRemoveDialog = new SendInvitationDialog(mContext,mScanActivity);

                mRemoveDialog.show();
                break;

            case 3:
                mScanActivity.AuthenticationSuccess();
                break;
        }
    }


    @Override
    protected void onError(ResponseInfoModel s) {
        mScanActivity.printn(s.getMsg());
    }


    /**
     * 申请绑定设备
     * @param token
     * @param acountId
     * @param acountName
     * @param imei
     */
    public void applyBindDevice(String token, long acountId, String acountName, String imei) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("acountId",acountId);
        hashMap.put("acountName",acountName);
        hashMap.put("imei",imei);
        LogUtils.d(TAG,"申请绑定设备"+ String.valueOf(hashMap));
        mApplyBindDevice = mService.applyBindDevice(hashMap);
        mScanActivity.showLoading("",mContext);
        mApplyBindDevice.enqueue(mCallback);

    }


    @Override
    protected void onSuccess(ResponseInfoModel data) {
        LogUtils.e(TAG,"onSuccess "+data.getMsg());
        mScanActivity.printn(mContext.getString(R.string.send_success));
    }


    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.e(TAG,"onSuccess "+data.getMsg());
    }
}
