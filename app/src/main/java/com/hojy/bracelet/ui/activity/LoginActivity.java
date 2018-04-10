package com.hojy.bracelet.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.presenter.LoginPresenter;
import com.hojy.bracelet.ui.view.LastInputEditText;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/3/3  17:55.
 * 描    述:
 */

public class LoginActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_login_username)
    LastInputEditText mEtLoginUsername;

    @BindView(R.id.et_login_password)
    LastInputEditText mEtLoginPassword;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    @BindView(R.id.iv_remove)
    LinearLayout mIvRemove;

    @BindView(R.id.ll_eyes)
    LinearLayout mLlEyes;

    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.tv_login_forget)
    TextView mTvLoginForget;

    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;

    @BindView(R.id.edit_main)
    LinearLayout mEditMain;
    private LoginPresenter mLoginPresenter;
    private String mPhone;
    private String mPassword;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }


    @Override
    protected void init() {
        if (mLoginPresenter == null){
        mLoginPresenter = new LoginPresenter(this,this);
        }
        mPhone = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "phone", "17666103375");
        mPassword = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "password", "a1234567");
//        toSDmissions();
        initView();
        initListener();
    }


    private void initListener() {
        mEtLoginUsername.addTextChangedListener(this);
//        mEtLoginPassword.addTextChangedListener(this);
    }


    private void initView() {
        mEtLoginUsername.setText(mPhone);
        mEtLoginPassword.setText(mPassword);
        mEtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }


    @Override
    protected void dismissNewok() {
        if (mLoginPresenter.mLogin != null)
            mLoginPresenter.mLogin.cancel();
    }


    @Override
    protected void cameraSuccess() {
        LogUtils.e(TAG,"相机权限已打开");
    }


    @Override
    protected void cameraError() {
        LogUtils.e(TAG,"相机权限被拒绝");
    }


    @OnClick({R.id.ll_eyes, R.id.btn_login, R.id.tv_login_forget, R.id.tv_login_register,R.id.iv_remove})
    public void onViewClicked(View view) {
        mPhone = mEtLoginUsername.getText().toString().trim();
        mPassword = mEtLoginPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.ll_eyes:
                viewPassword();
                break;

            case R.id.btn_login:
                boolean pushStopped = JPushInterface.isPushStopped(MyApplication.sInstance);
                LogUtils.e(TAG,"pushStopped 停止推送 = " +pushStopped);
                if (pushStopped){
                    boolean push = (boolean) SharedPreferencesUtils.getParam(this, "push", true);
                    if (push){
                    JPushInterface.resumePush(MyApplication.sInstance);
                    }
                }
                mLoginPresenter.login(mPhone,mPassword);
                break;

            case R.id.tv_login_forget:
                toActivity(ForgetPasswordActivity.class);
                break;

            case R.id.tv_login_register:
                toActivity(RegisterActivity.class);
                break;

            case R.id.iv_remove:
                mEtLoginUsername.setText("");
                break;
        }
    }


    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void passwordIsEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }


    public void resultMsg(String resultMsg) {
        printn(resultMsg);
    }


    /**
     * 查看密码
     */
    private void viewPassword() {
        if (TextUtils.isEmpty(mEtLoginPassword.getText()))return;

        if (mIvNoView.isSelected()) {
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
        mEtLoginPassword.setSelection(mEtLoginPassword.getText().length());
    }


    /**
     * 登录成功
     */
    public void loginSuccess() {
//        printn(getString(R.string.succeed));
        toActivity(HomeActivity.class);
        finishActivityByAnimation(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String trim = charSequence.toString().trim();
        if (trim.length() >= 1){
            mIvRemove.setVisibility(View.VISIBLE);
        }else {
            mIvRemove.setVisibility(View.GONE);
        }
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    protected void grantedError() {
        LogUtils.e(TAG,"存储权限被拒绝");
    }
}
