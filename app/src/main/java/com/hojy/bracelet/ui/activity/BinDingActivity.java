package com.hojy.bracelet.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.presenter.BinDingPresenter;
import com.hojy.bracelet.ui.view.AddressSettingDialog;
import com.hojy.bracelet.ui.view.RemoveDialog;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.UserUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午5:22
 * 描   述: 绑定与解绑的视图
 */
public class BinDingActivity extends BaseActivity {


    private static final String TAG = "BinDingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @BindView(R.id.tv_imei)
    TextView mTvImei;

    @BindView(R.id.btn_unbinding)
    Button mBtnUnBinding;

    @BindView(R.id.btn_unbind_submit)
    Button mBtnUnBindSubmit;

    private BinDingPresenter mBinDingPresenter;
    private int mDeviceId;
    private RemoveDialog mRemoveDialog;
    private long mAcountId;
    private String mImei;
    private long mGroupId;
    private Bitmap mBitmap;
    private boolean isRemoveDialog;
    private AddressSettingDialog mDialog;
    private int mIsAdmin;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_binding;
    }


    @Override
    protected void init() {
        mBinDingPresenter = new BinDingPresenter(this, this);
        mDeviceId = getIntent().getIntExtra("deviceId", 0);
        mImei = getIntent().getStringExtra("imei");
        mIsAdmin = getIntent().getIntExtra("isAdmin", -1);
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }

        initView();

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.binding_unbundling));
        if (mIsAdmin != 1) {
            mBtnUnBinding.setText(getString(R.string.remove_the_binding));
            mBtnUnBindSubmit.setVisibility(View.INVISIBLE);
        }
        mBitmap = UserUtil.createQRCode(mImei, 500);
        ivQrCode.setImageBitmap(mBitmap);
        mTvImei.setText(getString(R.string.watch_imei) + " " + mImei);

    }


    @OnClick({R.id.iv_back, R.id.btn_unbinding, R.id.btn_unbind_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_unbinding:
                //管理员 解除所有人, 非管理员解除自己
                if (mIsAdmin != 1){
                    //普通成员解绑
                    unBinding();
                    LogUtils.e(TAG,"普通成员解绑");
                }else {
                    if (mDialog == null)
                        mDialog = new AddressSettingDialog(this,this);
                    mDialog.show();
                    mDialog.setText("",getString(R.string.handed_over_to_the_administrator_and_unbundling),
                            getString(R.string.the_solution_to_all_members),true);
                }


                break;

            case R.id.btn_unbind_submit:
                //成员列表
                Intent intent = new Intent(this,SelectUserActivity.class);
                intent.putExtra("deviceId",mDeviceId);
                intent.putExtra(STRING,"ordinary");
                startActivityForResult(intent,102);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 102){
            LogUtils.e(TAG,"解绑成功通知销毁");
            finishActivityByAnimation(this);
        }
    }

    @Override
    public void setOptions(int postion) {
        switch (postion){
            case 2:
                LogUtils.e(TAG,"移交管理员");
                Intent intent = new Intent(this,SelectUserActivity.class);
                intent.putExtra("deviceId",mDeviceId);
                intent.putExtra(STRING,"management");
                startActivityForResult(intent,102);
                break;

            case 3:
                unBinding();
                LogUtils.e(TAG,"解除全部");
                break;

            default:
                break;
        }
    }

    /**
     * 解绑设备
     */
    private void unBinding() {
        if (mIsAdmin == 1) {
            //管理员解绑
            //解除所有人
            isRemoveDialog = true;
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.remove_all) + "?", true);
        } else {
            //接触自己
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.unbinding));
        }
    }


    @Override
    public void cancel() {
        if (mIsAdmin == 1){
            mBinDingPresenter.adminUnbindAllDevice(MyApplication.sToken,mDeviceId);
        }else {
        mBinDingPresenter.unbindDevice(mDeviceId,MyApplication.sToken,MyApplication.sAcountId);
        }

    }


    /**
     * 解除自己失败回调
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        printn(resultMsg);
    }


    /**
     * 解除自己成功回调
     */
    public void onSuccess() {

    }


    /**
     * 清除缓存
     */
    private void deleteData() {


    }


    /**
     * 加载网络,清除缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mBinDingPresenter.mUnbindDevice != null)
            mBinDingPresenter.mUnbindDevice.cancel();


    }


    /**
     * 解绑成功
     */
    public void unbindSuccess() {
        EventBus.getDefault().post(new NewDeviceModel());
        finishActivityByAnimation(this);
    }
}
