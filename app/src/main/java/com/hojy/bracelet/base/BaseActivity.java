package com.hojy.bracelet.base;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hojy.bracelet.R;
import com.hojy.bracelet.recevier.NetBroadcastReceiver;
import com.hojy.bracelet.util.AppManagerUtils;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.NetEvent;
import com.hojy.bracelet.widget.progress.HojyProgress;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by LoyBin on 18/2/27  10:40.
 * 描    述:
 */

public abstract class BaseActivity extends AppCompatActivity implements NetEvent {


    private static final String TAG = "BaseActivity";
    private static final String BABY = "baby";
    private Context mContext;
    private Toast mToast;
    public static final String STRING = "String";
    public static final String MODE = "Mode";
    protected long time = 0;
    private long exitTime = 0;
    private HojyProgress mProfress;
    private MyBaseActiviy_Broad oBaseActiviy_Broad;
    private IntentFilter mIntentFilter;
    protected static final int RELATION = 101;
    /**
     * 网络状态
     */
    private int netMobile;
    /**
     * 这里保存一个值用来判断网络是否经历了由断开到连接
     */
    private boolean isNetChanges;
    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(getLayoutRes());
        AppManagerUtils.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        init();

        //动态注册广播
        if (oBaseActiviy_Broad == null){
            oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        }

        if (mIntentFilter == null){
            mIntentFilter = new IntentFilter("drc.xxx.yyy.baseActivity");
        }
        registerReceiver(oBaseActiviy_Broad, mIntentFilter);
        mContext = this;
    }

    /**
     * 设置状态栏透明
     */
    private void setWindow() {
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.TRANSPARENT);

//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * 触发loading
     * @param message
     */
    public void showLoading(String message,Context context) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return ;
        }
        try {
            if (mProfress == null){
                mProfress = HojyProgress.create(this,mOnKeyListener)
                        .setStyle(HojyProgress.Style.SPIN_INDETERMINATE);
            }
            mProfress.show();
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }


    /**
     * 取消loading
     */
    public void dismissLoading() {
        try {
            if (mProfress != null && mProfress.isShowing()) {
                LogUtils.e(TAG,"取消loading");
                mProfress.dismiss();
            }
        }catch (Exception e){
            LogUtils.e(TAG,"取消loading异常");
        }

    }




    /**
     * dialog监听
     */
    DialogInterface.OnKeyListener mOnKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 3000) {
                    printn(getString(R.string.dismiss));
                    exitTime = System.currentTimeMillis();
                } else {
                    dismissLoading();
                    dismissNewok();
                }
                return true;
            }
            return true;
        }
    };



    /**
     * 吐司show
     * @param message
     */
    public void printn(String message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            // mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    public void toActivity(Class<?> toClass) {
        toActivity(0,toClass);
    }

    public void toActivity(int code,Class<?> toClass) {
        toActivity(code,toClass,null);
    }


    public void toActivity(int code,Class<?> toClass,String string){
        toActivity(code,toClass,string,null);
    }


    public void toActivity(int code ,Class<?> toClass, String string,String baby) {
        toActivity(code,toClass, string,baby, 0);
    }


    public void toActivity(Class<?> toClass, String string) {
        toActivity(toClass, string,null, 0,0);
    }


    protected void toActivity(Class<?> toClass,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    public void toActivity(Class<?> toClass, String string,String baby) {
        toActivity(toClass, string,baby, 0,0);
    }


    protected void toActivity(Class<?> toClass, String string, String baby,int mode,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);
        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    protected void toActivity(int REQUEST_CODE, Class<?> toClass,
                              String string,String baby, int mode) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);

        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    //退出方法
    protected void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            printn(getString(R.string.exit_the_program));
        } else {
            Intent intent = new Intent("drc.xxx.yyy.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播
        }
    }

    //定义一个退出
    public class MyBaseActiviy_Broad extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                //销毁BaseActivity
                finish();
            }
        }
    }



    protected void sendBroadcast(){
        Intent intent = new Intent("drc.xxx.yyy.baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);//发送广播
    }

    //申请存储权限
    public void toSDmissions() {
        int checkSad= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //拒绝
        if (checkSad == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},103);
        }else if (checkSad == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启存储权限");
        }
    }



    //申请相机权限
    public void toCanmeraPermissions(){
        int checkCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //拒绝
        if (checkCameraPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
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


    @Override
    protected void onStart() {
        super.onStart();
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            /**
             * 设置监听
             */
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    /**
     * 监听网络状态的回调
     * @param netMobile
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }


    /**
     * 当前的网络状态
     */
    private void isNetConnect() {
        switch (netMobile) {
            case 1://wifi
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"wifi");
                netWiFi();
                theNetwork();
                break;

            case 0://移动数据
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"移动数据");
                netWork4G();
                theNetwork();
                break;

            case -1://没有网络
                isNetChanges = false;
                Log.d(TAG, "isNetConnect: " +"没有网络");
                noNetwork();
                break;
        }

    }

    protected void netWiFi() {

    }


    /**
     * 当前是移动网的通知
     */
    protected void netWork4G() {


    }


    /**
     * 有网络的通知
     */
    protected void theNetwork() {

    }


    /**
     * 没有网络的通知
     */
    protected void noNetwork() {

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


    /**
     * 更新
     */
    public void upDate() {

    }


    /**
     * 取消更新
     */
    public void dismissVesion() {

    }

    /**
     * 初始化layout
     * @return
     */
    protected abstract int getLayoutRes();


    /**
     * 初始化方法
     */
    protected abstract void init();


    /**
     * 强制取消网络加载,把网络队列清除
     */
    protected abstract void dismissNewok();


    /**
     * finish 动画
     * @param activity
     */
    public void finishActivityByAnimation(Activity activity){
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    public void dismiss() {

    }


    public void cancel() {

    }

    /**
     * 设置底部dialog
     * @param i
     */
    public void setOptions(int i) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }

        if (oBaseActiviy_Broad != null){
            unregisterReceiver(oBaseActiviy_Broad);//注销广播
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
