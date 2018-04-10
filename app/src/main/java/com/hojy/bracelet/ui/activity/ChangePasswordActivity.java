package com.hojy.bracelet.ui.activity;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.presenter.EditPasswordPresenter;
import com.hojy.bracelet.ui.view.LastInputEditText;
import com.hojy.bracelet.util.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LoyBin on 18/3/12  11:47.
 * 描    述:
 */

public class ChangePasswordActivity extends BaseActivity{


    @BindView(R.id.et_phone)
    LastInputEditText mEtPhone;

    @BindView(R.id.et_code)
    LastInputEditText mEtCode;

    @BindView(R.id.tv_send_code)
    public TextView mTvSendCode;

    @BindView(R.id.et_password)
    LastInputEditText mEtPassword;

    @BindView(R.id.et_config_password)
    LastInputEditText mEtConfigPassword;

    @BindView(R.id.btn_resetpassword)
    Button mBtnResetpassword;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    private EditPasswordPresenter mEditPasswordPresenter;
    private String mPassword;
    private String mCode;
    private String mPhone;
    private String mConfigPassword;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_change_password;

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mEditPasswordPresenter = new EditPasswordPresenter (this, this);

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.ChangePassword_Title));
    }


    @OnClick({R.id.tv_send_code, R.id.btn_resetpassword, R.id.iv_back, R.id.iv_no_view})
    public void onViewClicked(View view) {
        mPhone = mEtPhone.getText().toString().trim();
        mCode = mEtCode.getText().toString().trim();
        mPassword = mEtPassword.getText().toString();
        mConfigPassword = mEtConfigPassword.getText().toString();
        switch (view.getId()) {
            case R.id.tv_send_code:
                mEditPasswordPresenter.resetCode(mPhone);
                break;

            case R.id.btn_resetpassword:
                mEditPasswordPresenter.resetPassword(mPhone, mCode, mPassword, mConfigPassword);
                break;

            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_no_view:
                viewPassword();
                if (mPassword != null) {
                    mEtPassword.setSelection(mPassword.length());
                }
                break;
        }
    }


    private void viewPassword() {
        if (mPassword == null)return;

        if (mIvNoView.isSelected()) {
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void codeIsEmpty() {
        printn(getString(R.string.codeIsEmpty));
    }


    public void passwordInEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }


    public void passwordError() {
        printn(getString(R.string.Register_PasswordError_Tips));
    }


    public void CheckCode() {
        printn(getString(R.string.CheckCode_Success));
    }


    public void error(String resultMsg) {
        printn(resultMsg);
    }


    public void editSuccess(String md5Password,String phone) {
        printn(getString(R.string.deit_password_success));
        SharedPreferencesUtils.setParam(this, "password", md5Password);
        SharedPreferencesUtils.setParam(this, "phone", phone);
        finishActivityByAnimation(this);
    }


    public void editError(String resultMsg) {
        printn(resultMsg);
    }


    public void passwordInconformity() {
        printn(getString(R.string.password_error));
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mEditPasswordPresenter.mEdit != null) {
            mEditPasswordPresenter.mEdit.cancel();
        }
        if (mEditPasswordPresenter.mSendCheckCode != null) {
            mEditPasswordPresenter.mSendCheckCode.cancel();
        }
    }
}
