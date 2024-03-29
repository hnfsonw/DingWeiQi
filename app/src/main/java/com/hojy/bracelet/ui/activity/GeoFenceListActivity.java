package com.hojy.bracelet.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.GeoFenceModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.GeoFencePresenter;
import com.hojy.bracelet.ui.adapter.GeoFenceListAdapter;
import com.hojy.bracelet.ui.view.PlayDialog;
import com.hojy.bracelet.util.LogUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 安全范围view
 */
public class GeoFenceListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "GeoFenceListActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.rl_tips)
    RelativeLayout mRlTips;

    @BindView(R.id.tv_right)
    public TextView mTvRight;

    @BindView(R.id.list_geofence)
    ListView mListGeofence;

    @BindView(R.id.btn_add)
    Button mBtnAdd;

    @BindView(R.id.swiper_geofence)
    SwipeRefreshLayout mSwiperGeofence;

    private ArrayList<GeoFenceModel> mGeoFenceModelList;
    private GeoFenceListAdapter mGeoFenceListAdapter;
    private boolean inNowork = false;
    private GeoFencePresenter mGeoFencePresenter;
    private List<ResponseInfoModel.DataBean.FenceListBean> mResultData;
    private PlayDialog mPlayDialog;
    private ResponseInfoModel.DataBean.FenceListBean mResultDataBean;
    private ResponseInfoModel.DataBean mDataBean;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_geo_fence;
    }


    @Override
    protected void init() {
        mDataBean = (ResponseInfoModel.DataBean) getIntent().getSerializableExtra("location");
        if (mGeoFenceModelList == null) {
            mGeoFenceModelList = new ArrayList<GeoFenceModel>();
        }
        initView();
        initListener();
        initData();

    }

    private void initView() {
        mTvTitle.setText(getString(R.string.the_safety_area));
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.the_editor));
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
        mSwiperGeofence.setOnRefreshListener(this);
        mIvBack.setOnClickListener(this);
        mListGeofence.setOnItemClickListener(this);
        mTvRight.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }


    private void initData() {
        if (mGeoFenceListAdapter == null)
        mGeoFenceListAdapter = new GeoFenceListAdapter(this, mGeoFenceModelList);
        mListGeofence.setAdapter(mGeoFenceListAdapter);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_add:
                if (inNowork) {
                    printn(getString(R.string.no_network));
                    return;
                }
//                if (MyApplication.sResult.getLng() == 0) {
//                    printn(getString(R.string.did_not_get_the_watch_please_try_again_later));
//                    return;
//                }
                addGeoFence();
                //申请定位权限
//                toLocationPermission();
                break;

            case R.id.tv_right:
                if (inNowork) {
                    printn(getString(R.string.no_network));
                    return;
                }
                mGeoFenceListAdapter.logo();
                break;
        }
    }


    /**
     * 添加围栏
     */
    private void addGeoFence() {
        Intent intent = new Intent(this, ElectronicFenceActivity.class);
        intent.putExtra("FromMark", "Add");
        intent.putExtra("location", mDataBean);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (inNowork) {
            printn(getString(R.string.no_network));
            return;
        }
        if (mResultData != null && mResultData.size() > 0) {
            int fenceId = mResultData.get(position).getId();
            Intent intent = new Intent(this, ElectronicFenceActivity.class);
            intent.putExtra("FromMark", "Edit");
            intent.putExtra("fenceId", fenceId);
            intent.putExtra("name", mResultData.get(position).getName());
            intent.putExtra("lng", mResultData.get(position).getLng() + "");
            intent.putExtra("lat", mResultData.get(position).getLat() + "");
            intent.putExtra("desc", mResultData.get(position).getDesc() + "ggg");
            intent.putExtra("radius", mResultData.get(position).getRadius() + "");
            startActivity(intent);
            LogUtils.d(TAG, "resultDataBean: " + "resultDataBean");
            LogUtils.d(TAG, "resultDataBean: " + mResultData.get(position).id);
            LogUtils.d(TAG, "getName: " + mResultData.get(position).getName());
        } else {
            toActivity(ElectronicFenceActivity.class);
            LogUtils.d(TAG, "onItemClick: " + "xinzeng");
        }

    }


    @Override
    public void onRefresh() {
        mGeoFencePresenter.loadingColumn(MyApplication.sToken, MyApplication.sDeviceId, true);
        mSwiperGeofence.setRefreshing(false);

    }


    /**
     * 定位权限打开的通知
     */
    @Override
    protected void locationSuccess() {
        addGeoFence();
    }


    /**
     * 定位权限被拒绝
     */
    @Override
    protected void locationError() {
        printn(getString(R.string.set_the_electronic_fence_please_open_this_permission));
    }



    @Override
    protected void onResume() {
        super.onResume();
        //获取围栏数据
        if (mGeoFencePresenter == null) {
            mGeoFencePresenter = new GeoFencePresenter(this, this);
        }
        mGeoFencePresenter.loadingColumn(MyApplication.sToken, MyApplication.sDeviceId, false);
    }


    /**
     * 成功拿到数据
     *
     * @param data
     */
    public void success(ResponseInfoModel data) {
        mResultData = data.getData().getFenceList();
        if (mResultData.size() <= 0) {
            mRlTips.setVisibility(View.GONE);
        } else {
            mRlTips.setVisibility(View.VISIBLE);
        }
        mGeoFenceListAdapter.setData(mResultData);
        mGeoFenceListAdapter.notifyDataSetChanged();
    }


    /**
     * 80001失败的数据
     *
     * @param s
     */
    public void error(ResponseInfoModel s) {
        printn(s.getMsg());
    }


    public void changeState(ResponseInfoModel.DataBean.FenceListBean resultData,boolean isSwitch,
                            int state) {
        mGeoFencePresenter.changes(resultData, MyApplication.sToken,isSwitch,state);
    }


    /**
     * 删除弹出dialog
     */
    public void playDialog(ResponseInfoModel.DataBean.FenceListBean resultDataBean) {
        if (mPlayDialog == null) {
            mPlayDialog = new PlayDialog(this, this);
        }
        mResultDataBean = resultDataBean;
        mPlayDialog.show();
    }


    /**
     * dialog确定
     */
    public void cancel() {
        mGeoFencePresenter.delete(mResultDataBean, MyApplication.sToken,false);
    }


    /**
     * 成功删除
     */
    public void deleteSuccess() {
        if (mDeleteGeoFence != null) {
            Log.d("GeoFenceListActivity", "deleteSuccess: " + 111);
            mDeleteGeoFence.deleteGeo();
        }
    }


    public DeleteGeoFence mDeleteGeoFence;


    public void setDeleteGeoFence(DeleteGeoFence deleteGeoFence) {
        mDeleteGeoFence = deleteGeoFence;
    }

    public void upDataSwitch() {
        if (mDeleteGeoFence != null) {
            mDeleteGeoFence.upDataGeo();
        }
    }


    public interface DeleteGeoFence {
        void deleteGeo();
        void upDataGeo();
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mGeoFencePresenter.mDeleteFenceById != null) {
            mGeoFencePresenter.mDeleteFenceById.cancel();
        }
        if (mGeoFencePresenter.mQueryFenceInfoByDeviceId != null) {
            mGeoFencePresenter.mQueryFenceInfoByDeviceId.cancel();
        }
    }

}
