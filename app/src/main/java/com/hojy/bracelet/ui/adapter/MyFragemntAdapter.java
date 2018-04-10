package com.hojy.bracelet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.DeviceSettingActivity;
import com.hojy.bracelet.ui.fragment.MyFragemnt;
import com.hojy.bracelet.util.LogUtils;

import org.w3c.dom.Text;

import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by LoyBin on 18/3/8  14:26.
 * 描    述:
 */

public class MyFragemntAdapter extends RecyclerView.Adapter{

    private static final String TAG = "MyFragemnt";
    private Context mContext;
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_DIVISION = 1;

    public MyFragemntAdapter(Context context, List<ResponseInfoModel.DataBean.BindingListBean> bindingListBeans) {
        mContext = context;
        mBindingListBeans = bindingListBeans;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case TYPE_DIVISION:
                itemView = View.inflate(mContext, R.layout.item_device,null);
                DeviceHolder deviceHolder = new DeviceHolder(itemView);
                return deviceHolder;

            case TYPE_TITLE:
                itemView = View.inflate(mContext, R.layout.item_view_more,null);
                ViewMoreHolder viewMoreHolder = new ViewMoreHolder(itemView);
                return viewMoreHolder;

               default:
                   return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case TYPE_DIVISION:
                DeviceHolder deviceHolder = (DeviceHolder) holder;
                final ResponseInfoModel.DataBean.BindingListBean bindingListBean = mBindingListBeans.get(position);
                deviceHolder.setData(bindingListBean);
                deviceHolder.mTvQun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,DeviceSettingActivity.class);
                        intent.putExtra("deviceId",bindingListBean.getDeviceId());
                        intent.putExtra("type",bindingListBean.getType());
                        intent.putExtra("imei",bindingListBean.getImei());
                        intent.putExtra("isAdmin",bindingListBean.isAdmin());
                        mContext.startActivity(intent);
                        LogUtils.e(TAG,"当前点击的是第 "+bindingListBean.getType());
                    }
                });
                break;

            case TYPE_TITLE:
                ViewMoreHolder viewMoreHolder = (ViewMoreHolder) holder;
                viewMoreHolder.mTvQun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.e(TAG,"查看更多 ");
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (mBindingListBeans.size() > 5){
            return 6;
        }else {
            return mBindingListBeans.size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 5){
            return TYPE_TITLE;
        }else {
            return TYPE_DIVISION;
        }
    }

    public void setData(List<ResponseInfoModel.DataBean.BindingListBean> bindingListBeans) {
        mBindingListBeans = bindingListBeans;
    }


    class DeviceHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mTvQun;
        public TextView mTvType;
        public ImageView mIvIcon;


        DeviceHolder(View view) {
            super(view);
            mTvQun = view.findViewById(R.id.ll_my_device);
            mTvType = view.findViewById(R.id.tv_type);
            mIvIcon = view.findViewById(R.id.iv_icon);
        }


        public void setData(ResponseInfoModel.DataBean.BindingListBean  data) {
            int type = data.getType();
            mTvType.setText(data.getNickName()+"");
            switch (type){
                case 1:
//                    mTvType.setText(data.getNickName());
                    mIvIcon.setImageResource(R.mipmap.my_location);
                    break;

                case 2:
//                    mTvType.setText(mContext.getString(R.string.locator));
                    mIvIcon.setImageResource(R.mipmap.my_location);
                    break;

                default:
//                    mTvType.setText(mContext.getString(R.string.my_bracelet));
                    break;
            }

        }
    }


    class ViewMoreHolder extends RecyclerView.ViewHolder {

        public  LinearLayout mTvQun;

        ViewMoreHolder(View view) {
            super(view);
            mTvQun =  view.findViewById(R.id.ll_view_more);
        }

        public void setData(String data) {

        }
    }
}
