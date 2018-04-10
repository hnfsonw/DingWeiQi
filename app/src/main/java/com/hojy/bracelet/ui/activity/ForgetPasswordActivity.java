package com.hojy.bracelet.ui.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.presenter.ForgetPasswordPresenter;
import com.hojy.bracelet.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/20 上午10:03
 * 描   述: 忘记密码视图
 */
public class ForgetPasswordActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_phone)
    LastInputEditText mEtPhone;

    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private ForgetPasswordPresenter mForgetPasswordPresenter;
    private String mPhone;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget_password;
    }


    @Override
    protected void init() {
        mForgetPasswordPresenter = new ForgetPasswordPresenter( this,this);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.login_forgot_pwd_text));
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int mCount, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int mCount) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 11){
                    mBtnRegister.setEnabled(true);
                    mBtnRegister.setBackgroundResource(R.drawable.login_shape_white_selector);
                    mBtnRegister.setTextColor(Color.WHITE);
                }else {
                    mBtnRegister.setBackgroundResource(R.drawable.login_shape_white_selector);
                    mBtnRegister.setEnabled(false);
                    mBtnRegister.setTextColor(Color.GRAY);
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.btn_register})
    public void onViewClicked(View view) {
        mPhone = mEtPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_register:
                mForgetPasswordPresenter.checkAcount(mPhone);
                break;
        }
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void phoneEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void onError(String resultMsg) {
        printn(resultMsg);
    }


    /**
     * 验证成功跳转下个界面
     */
    public void onSuccess() {
        toActivity(ForgetActivity.class, mPhone);
        finishActivityByAnimation(this);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mForgetPasswordPresenter.mCheckAcount != null) {
            mForgetPasswordPresenter.mCheckAcount.cancel();
        }
    }

}
