package com.hojy.bracelet.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.util.LogUtils;

import java.util.List;

/**
 * Created by LoyBin on 18/3/7  15:52.
 * 描    述:
 */

public abstract class BaseFragment extends Fragment{


    private static final String TAG = "Fragment";
    private List<ResponseInfoModel.DataBean.BindingListBean> mBindingListBeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getLayoutRes(inflater,savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    //申请相机权限
    protected void toCanmeraPermissions(){
        int checkCameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        //拒绝
        if (checkCameraPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String []{Manifest.permission.CAMERA},102);

        }else if (checkCameraPermission == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启相机权限");
            cameraSuccess();
        }
    }


    /**
     * 申请权限返回值
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e(TAG,grantResults.length + "~~~~~~~");
        try {
            switch (requestCode){
                case 100:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"定位权限设置完毕");
                        locationSuccess();
                    }else {
                        LogUtils.e(TAG,"用户拒绝了定位权限");
                        locationError();
                    }
                    break;

                case 101:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"语音权限设置完毕");
                        voiceSuccess();
                    }else {
                        LogUtils.e(TAG,"语音权限被拒绝");
                        voiceError();
                    }
                    break;

                case 102:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"相机权限设置完毕");
                        cameraSuccess();
                    }else {
                        LogUtils.e(TAG,"相机权限被拒绝");
                        cameraError();
                    }
                    break;

                case 103:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"存储权限设置完毕");
                    }else {
                        LogUtils.e(TAG,"存储权限被拒绝");
                        grantedError();
                    }
                    break;
            }
        }catch (Exception e){
            LogUtils.d(TAG,"权限打开异常");
        }

    }



    /**
     * 定位权限被拒绝的通知
     */
    protected void locationError() {

    }


    /**
     * SD存储权限被拒绝
     */
    protected void grantedError() {

    }


    /**
     * 语音权限拒绝的通知
     */
    protected void voiceError() {

    }


    /**
     * 语音权限打开的通知
     */
    protected void voiceSuccess() {

    }


    /**
     * 定位权限打开
     */
    protected void locationSuccess() {

    }

    /**
     * 相机权限被拒绝
     */
    protected void cameraError() {

    }


    /**
     * 相机权限打开
     */
    protected void cameraSuccess() {

    }

    protected abstract View getLayoutRes(LayoutInflater inflater,@Nullable Bundle savedInstanceState);


    protected abstract void init();


    public abstract void setData(List<ResponseInfoModel.DataBean.BindingListBean> data);
}
