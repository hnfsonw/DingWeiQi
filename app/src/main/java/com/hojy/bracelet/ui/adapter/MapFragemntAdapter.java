package com.hojy.bracelet.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.fragment.MapFragemnt;
import com.hojy.bracelet.util.LogUtils;

import java.util.List;

/**
 * Created by LoyBin on 18/3/9  14:54.
 * 描    述:
 */

public class MapFragemntAdapter extends BaseAdapter{

    private static final String TAG = "MapFragemnt";
    private Context mContext;
    private MapFragemnt mMapFragemnt;
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;

    public MapFragemntAdapter(Context context,MapFragemnt mapFragemnt, List<ResponseInfoModel.DataBean.BindingListBean> bindingListBeans) {
        mContext = context;
        mMapFragemnt = mapFragemnt;
        mBindingListBeans = bindingListBeans;
    }

    @Override
    public int getCount() {
        if (mBindingListBeans == null){
            return 0;
        }
        return mBindingListBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_device_choose,null);
            viewHolder.mTvType = view.findViewById(R.id.tv_type);
            viewHolder.mIvCurrent = view.findViewById(R.id.iv_current);
            viewHolder.mLlDevice = view.findViewById(R.id.ll_my_device);
            viewHolder.mIvIcon = view.findViewById(R.id.iv_icon);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final ResponseInfoModel.DataBean.BindingListBean bindingListBean = mBindingListBeans.get(position);



        if (MyApplication.sDeivceNumber == position){
            viewHolder.mTvType.setText(mBindingListBeans.get(position).getNickName());
            viewHolder.mTvType.setTextColor(R.color.map_device);

        }else {
            viewHolder.mTvType.setText(mBindingListBeans.get(position).getNickName());
            viewHolder.mTvType.setTextColor(R.color.map_device2);
        }

        if (bindingListBean.getType() == 1){
            if (MyApplication.sDeivceNumber == position){
                viewHolder.mIvIcon.setImageResource(R.mipmap.my_location1);
            }else {
                viewHolder.mIvIcon.setImageResource(R.mipmap.my_location2);
            }

        }else if (bindingListBean.getType() == 2){
            if (MyApplication.sDeivceNumber == position){
                viewHolder.mIvIcon.setImageResource(R.mipmap.my_location1);
            }else {
                viewHolder.mIvIcon.setImageResource(R.mipmap.my_location2);
            }
        }

        viewHolder.mLlDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapFragemnt.setTypeOnClick(position);
            }
        });

        return view;
    }


    public void setData(List<ResponseInfoModel.DataBean.BindingListBean> bindingListBeans) {
        mBindingListBeans = bindingListBeans;
    }


    class ViewHolder{
        TextView mTvType;
        ImageView mIvCurrent;
        ImageView mIvIcon;
        LinearLayout mLlDevice;
    }
}
