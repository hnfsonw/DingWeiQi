package com.hojy.bracelet.base;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hojy.bracelet.BuildConfig;
import com.hojy.bracelet.R;
import com.hojy.bracelet.config.Constants;
import com.hojy.bracelet.factory.FragmentFactory;
import com.hojy.bracelet.http.WearService;
import com.hojy.bracelet.model.ExitModel;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.ui.activity.LoginActivity;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;
import com.hojy.bracelet.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2018/02/28 上午11:14
 * 描   述: 数据请求的业务基类
 */
public abstract class BasePresenter {

    private static final String TAG = "BasePresenter";
    protected Retrofit mRetrofit;
    protected WearService mService;
    private HttpLoggingInterceptor mInterceptor;
    private Context mContext;

    public BasePresenter(Context context) {
        mContext = context;
        OkHttpClient builder = getBuilder();
        mRetrofit = new Retrofit.Builder().baseUrl(Constants.TEXTHOST).addConverterFactory
                (GsonConverterFactory.create()).client(builder).build();
        mService = mRetrofit.create(WearService.class);
        //把所有接口都封装到service里面
    }


    /**
     * 获取okhttp配置
     * @return
     */
    private OkHttpClient getBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("mobileType","Android")
                        .addHeader("appVersion", UIUtils.getVersion() ).build();

                return chain.proceed(request);
            }
        });
        // log用拦截器
        mInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, message);
            }
        });
        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        if (BuildConfig.DEBUG) {
            mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            mInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        builder.addInterceptor(mInterceptor)
                .connectTimeout(35, TimeUnit.SECONDS)//设置超时
                .readTimeout(35, TimeUnit.SECONDS)
                .writeTimeout(35, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//错误重连
        return builder.build();
    }


    protected Callback mCallback = new Callback<ResponseInfoModel>() {

        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            BaseActivity activity = (BaseActivity) mContext;
            activity.dismissLoading();
            int state = body.getCode();
            LogUtils.e(TAG, "getMsg: " + body.getMsg());
            LogUtils.e(TAG, "getResultCode: " + state);
//            成功取到数据
            if (Constants.State0 == state ) {
                BasePresenter.this.onSuccess(body);
            }else if (Constants.State20002 == state){
                //token失效
                tokenError();
            }else {
                onFaiure(body);
            }
        }


        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            LogUtils.e(TAG,"onFailure" + t.getMessage());
            BaseActivity activity = (BaseActivity) mContext;
            activity.dismissLoading();
            activity.printn(mContext.getString(R.string.Network_Error));
        }
    };



    protected Callback mCallback2 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            BaseActivity activity = (BaseActivity) mContext;
            activity.dismissLoading();
            int state = body.getCode();
            Log.e(TAG, "getMsg: " + body.getMsg());
            Log.e(TAG, "getResultCode: " + state);
//            成功取到数据
            if (Constants.State0 == state ) {
                onComplete(body);
            }else if (Constants.State20002 == state){
                //token失效
                tokenError();
            }else {
                onError(body);
            }
        }


        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            LogUtils.e(TAG,"onFailure" + t.getMessage());
            BaseActivity activity = (BaseActivity) mContext;
            activity.dismissLoading();
            activity.printn(mContext.getString(R.string.Network_Error));
        }
    };



    protected Callback mCallbackFragment = new Callback<ResponseInfoModel>() {

        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            int state = body.getCode();
            Log.e(TAG, "getMsg: " + body.getMsg());
            Log.e(TAG, "getResultCode: " + state);
//            成功取到数据
            if (Constants.State0 == state ) {
                onSuccess(body);
            }else if (Constants.State20002 == state){
                //token失效
                tokenError();
            }else {
                onFaiure(body);
            }
        }


        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            LogUtils.e(TAG,"onFailure" + t.getMessage());
            onFragmenDissme(t.getMessage());
        }
    };

    protected void onFragmenDissme(String message) {

    }


    protected  void onComplete(ResponseInfoModel data){

    }


    protected  void onError(ResponseInfoModel s){

    }


//    protected abstract void onDissms(String s);


    protected abstract void onSuccess(ResponseInfoModel data);


    protected abstract void onFaiure(ResponseInfoModel s);


    private void tokenError() {
             BaseActivity activity = (BaseActivity) mContext;
             SharedPreferencesUtils.setParam(mContext,"password","");
             activity.printn(mContext.getString(R.string.login_has_expired_please_login_again));
             EventBus.getDefault().post(new ExitModel());
    }



}
