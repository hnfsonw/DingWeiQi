package com.hojy.bracelet.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.bracelet.R;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.SelectUserActivity;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:27
 * 描   述: 选择用户的适配器
 */
public class SelectUserAdapter extends BaseAdapter {

    private Context mContext;

    private List<ResponseInfoModel.DataBean.AcountListBean> mMemberList;

//    public String[] mTitles = {
//            "爸爸", "妈妈", "爷爷", "奶奶",
//            "外公", "外婆", "伯父", "伯母",
//            "叔叔", "阿姨", "哥哥", "姐姐",
//            "自定义"
//    };
//
//    public int[] mIcons = {
//            R.mipmap.fatcher, R.mipmap.mother, R.mipmap.grandfather, R.mipmap.grandma,
//            R.mipmap.grandpa, R.mipmap.grandmother, R.mipmap.nuncle, R.mipmap.aunt,
//            R.mipmap.uncle, R.mipmap.auntie, R.mipmap.elder_brother, R.mipmap.elder_sister,
//            R.mipmap.other3x
//    };
    private SelectUserActivity mSelectUserActivity;


    public SelectUserAdapter(Context context, List<ResponseInfoModel.DataBean.AcountListBean> list) {
        mContext = context;
        mMemberList = list;
        mSelectUserActivity = (SelectUserActivity) mContext;
    }


    @Override
    public int getCount() {
        return mMemberList.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_user, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvRelationship = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvAdmin = (TextView) convertView.findViewById(R.id.tv_admin);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ResponseInfoModel.DataBean.AcountListBean acountListBean = mMemberList.get(position);

        if (acountListBean.getIsAdmin() == 1){
            viewHolder.tvAdmin.setText(mContext.getString(R.string.administrator));
        }else {
            viewHolder.tvAdmin.setText(mContext.getString(R.string.user));
        }

        viewHolder.tvRelationship.setText("电话 :"+acountListBean.getAcountName());



        return convertView;
    }

    public void setData(List<ResponseInfoModel.DataBean.AcountListBean> memberList) {
        mMemberList.clear();
        mMemberList.addAll(memberList);
        notifyDataSetChanged();
    }


    class ViewHolder {
        ImageView ivIcon;
        TextView tvRelationship;
        TextView tvAdmin;
    }
}
