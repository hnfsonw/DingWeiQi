package com.hojy.bracelet.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.NewDeviceModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.SelectUserPresenter;
import com.hojy.bracelet.ui.adapter.SelectUserAdapter;
import com.hojy.bracelet.ui.view.RemoveDialog;
import com.hojy.bracelet.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:16
 * 描   述: 选择用户view
 */
public class SelectUserActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String TAG = "SelectUserActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.list_geofence)
    ListView mListGeofence;

    @BindView(R.id.swiper_geofence)
    public SwipeRefreshLayout mSwiperGeofence;

    private SelectUserPresenter mSelectUserPresenter;
    private SelectUserAdapter mSelectUserAdapter;
    private List<ResponseInfoModel.DataBean.AcountListBean> mMemberList;
    public String mOrdinary;
    public String mManagement;
    private RemoveDialog mRemoveDialog;
    private ResponseInfoModel.DataBean.AcountListBean mMemberListBean;
    private ResponseInfoModel.DataBean.AcountListBean mManagementListBean;
    private String mImei;
    private int mDeviceId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_user;
    }


    @Override
    protected void init() {
        if (mSelectUserPresenter == null)
        mSelectUserPresenter = new SelectUserPresenter(this, this);
        if (mMemberList == null)
        mMemberList = new ArrayList<>();
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }
        mOrdinary = getIntent().getStringExtra(STRING);
        mManagement = getIntent().getStringExtra(STRING);
        mDeviceId = getIntent().getIntExtra("deviceId", 0);

        initView();
        initData();
        initListener();

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initListener() {
        mListGeofence.setOnItemClickListener(this);
        mSwiperGeofence.setOnRefreshListener(this);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.Please_select_a_user));
        mSwiperGeofence.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
    }


    private void initData() {
        mSelectUserAdapter = new SelectUserAdapter(this, mMemberList);
        mListGeofence.setAdapter(mSelectUserAdapter);

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }


    @Override
    public void onRefresh() {
        mSelectUserPresenter.loadingList(mDeviceId, MyApplication.sToken, true);
        mSwiperGeofence.setRefreshing(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "sDeviceId: " + MyApplication.sDeviceId);
        LogUtils.d(TAG, "token: " + MyApplication.sToken);
        mSelectUserPresenter.loadingList(mDeviceId, MyApplication.sToken, false);
    }


    public void onSuccess(List<ResponseInfoModel.DataBean.AcountListBean> memberList) {
        mMemberList.addAll(memberList);
        mSelectUserAdapter.setData(memberList);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //解绑普通成员
        if (mOrdinary.equals("ordinary")) {

            mMemberListBean = mMemberList.get(position);
            if (MyApplication.sAcountId == mMemberListBean.getAcountId()) {
                printn(getString(R.string.no_solution_to_himself));
                return;
            }
            mRemoveDialog.show();
            mRemoveDialog.initUserName(mMemberListBean.getAcountName() + "?");

            setSelectUserInter(new SelectUserInter() {
                @Override
                public void deleteGeod() {
                    mMemberList.remove(position);
                    mSelectUserAdapter.notifyDataSetChanged();
                }
            });

        }

        //解绑自己
        if (mManagement.equals("management")) {
            mManagementListBean = mMemberList.get(position);
            if (MyApplication.sAcountId == mManagementListBean.getAcountId()) {
                printn(getString(R.string.you_have_is_the_facility_manager));
                return;
            }
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.hand_over_control_to));
            mRemoveDialog.initUserName(mManagementListBean.getAcountName() + "?");
        }

    }


    /**
     * 确定删除
     */

    @Override
    public void cancel() {
//        解除普通成员
        if (mOrdinary.equals("ordinary")) {
            mSelectUserPresenter.removeOrdinary(mMemberListBean, MyApplication.sToken, mDeviceId);
        }

        //移交管理权
        if (mManagement.equals("management")) {
            mSelectUserPresenter.adminUnbindDevice(mManagementListBean, MyApplication.sToken, mDeviceId, MyApplication.sAcountId);
        }
    }


    /**
     * 删除成功通知adapter 去删除
     */
    public void removeSuccess() {
        if (mSelectUserInter != null) {
            mSelectUserInter.deleteGeod();
        }
        printn(getString(R.string.remove_success));
    }


    /**
     * 删除失败的回掉
     *
     * @param resultMsg
     */
    public void removeError(String resultMsg) {
        printn(resultMsg);
    }


    /**
     * 移交管理员 解绑自己失败
     *
     * @param resultMsg
     */
    public void errorManagement(String resultMsg) {
        printn(resultMsg);
    }


    /**
     * 移交管理员 解绑自己成功
     */
    public void successManagement() {
        EventBus.getDefault().post(new NewDeviceModel());
        printn(getString(R.string.transfer_success));
        setResult(102);
        finishActivityByAnimation(this);
    }


    public SelectUserInter mSelectUserInter;


    public void setSelectUserInter(SelectUserInter selectUserInter) {
        mSelectUserInter = selectUserInter;
    }


    public interface SelectUserInter {

        void deleteGeod();

    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mSelectUserPresenter.mDisBandAcountAndChangeAdmin != null) {
            mSelectUserPresenter.mDisBandAcountAndChangeAdmin.cancel();
        }
        if (mSelectUserPresenter.mDisBandOneAcount != null) {
            mSelectUserPresenter.mDisBandOneAcount.cancel();
        }
        if (mSelectUserPresenter.mGetGroupMemberList != null) {
            mSelectUserPresenter.mGetGroupMemberList.cancel();
        }
    }
}
