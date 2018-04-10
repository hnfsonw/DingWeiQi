package com.hojy.bracelet.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.presenter.MessageCenterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LoyBin on 18/3/14  16:42.
 * 描    述:
 */

public class MessageCenterActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.listview)
    ListView mListview;

    private MessageCenterPresenter mMessageCenterPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void init() {
        if (mMessageCenterPresenter == null)
            mMessageCenterPresenter = new MessageCenterPresenter(this, this);

        initView();
        initData();
    }


    private void initData() {

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.message_center));
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        finishActivityByAnimation(this);
    }
}
