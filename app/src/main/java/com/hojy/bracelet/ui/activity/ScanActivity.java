package com.hojy.bracelet.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.hojy.bracelet.R;
import com.hojy.bracelet.base.BaseActivity;
import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.model.ResponseInfoModel;
import com.hojy.bracelet.presenter.ScanPresenter;
import com.hojy.bracelet.ui.view.LastInputEditText;
import com.hojy.bracelet.util.BitmapUtils;
import com.hojy.bracelet.util.LogUtils;
import com.hojy.bracelet.util.SharedPreferencesUtils;
import com.hojy.bracelet.util.UIUtils;
import com.hojy.bracelet.widget.decode.BitmapDecoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.InactivityTimer;
import zxing.decoding.Intents;
import zxing.view.ViewfinderView;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 扫描二维码的view
 */
public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "ScanActivity";
    private static final int REQUEST_CODE = 101;
    private static final String INTENT_EXTRA_KEY_QR_SCAN = "102";
    private CaptureActivityHandler handler;
    private ViewfinderView mViewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private boolean vibrate;
    private LinearLayout mBackImageView;
    private TextView mTitleText;
    private ImageView mTvRight;
    private String scanString = "";
    private SurfaceView mSurfaceView;
    private ScanPresenter mScanPresenter;
    private LastInputEditText mEtImei;
    private String mToken;
    public String mNewBaby;
    private String mScan;
    private Button mTvBindingEquipment;
    private String mImei;
    private String mIsCamera;
    private TextView mScanTips;
    private TextView mTvCamera;
    private TextView mTvText3;
    private boolean mIsView;
    private String photo_path;
    private Bitmap scanBitmap;
    private ProgressDialog mProgress;
    private String photoPath;
    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;
    public static final String SCAN_QRCODE_RESULT = "qrcode_result";//扫码返回结果（字符串）
    public static final String SCAN_QRCODE_BITMAP = "qrcode_bitmap";//扫码结果bitmap
    private boolean mIsPhotoAlbum;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_scan;
    }


    @Override
    protected void init() {
        if(mScanPresenter == null)
        mScanPresenter = new ScanPresenter(this, this);
        mToken = (String) SharedPreferencesUtils.getParam(this,"token","");
        mNewBaby = getIntent().getStringExtra(STRING);
        mScan = getIntent().getStringExtra("string");
        mIsCamera = getIntent().getStringExtra("isCamera");
        initView();
        initListener();
        toCanmeraPermissions();
    }


    private void initView() {
        mSurfaceView =  findViewById(R.id.preview_view);
        mBackImageView =  findViewById(R.id.iv_back);
        mTitleText =  findViewById(R.id.tv_title);
        mTvRight =  findViewById(R.id.iv_confirm);
        mEtImei =  findViewById(R.id.et_imei);
        mTvBindingEquipment =  findViewById(R.id.tv_binding_equipment);
        mScanTips =  findViewById(R.id.scan_Tips);
        mTvCamera =  findViewById(R.id.tv_camera);
        mTvText3 = findViewById(R.id.textView3);
        mEtImei.addTextChangedListener(watcher);

        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setImageResource(R.mipmap.photo_album);
        mTvBindingEquipment.setVisibility(View.GONE);
        mTitleText.setText(getResources().getString(R.string.Scan_Title));
        //照相机权限未开启,提示用户
        if (mIsCamera != null) {
            LogUtils.e(TAG, "照相机权限未开启");
            mScanTips.setVisibility(View.GONE);
            mTvText3.setVisibility(View.INVISIBLE);
            mTvCamera.setVisibility(View.VISIBLE);
        }

    }


    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mTvBindingEquipment.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mImei = mEtImei.getText().toString().trim();
        switch (v.getId()) {
            case R.id.iv_back:
                if (mScan != null) {
                    toActivity(LoginActivity.class);
                    finishActivityByAnimation(this);
                }
                mIsCamera = null;
                finishActivityByAnimation(this);
                break;

            case R.id.iv_confirm:
                LogUtils.e(TAG,"打开本地相册");
                mIsPhotoAlbum = true;
                toCanmeraPermissions();
                break;

            case R.id.tv_binding_equipment:
                    mScanPresenter.queryDeviceAdmin(mImei,mToken, MyApplication.sAcountId);
                break;
        }

    }


    private void openPhotoAlbum() {
        // 打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_PICK); // "android.intent.action.GET_CONTENT"
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent,
                "选择二维码图片");
        startActivityForResult(wrapperIntent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (resultCode == RESULT_OK) {
            final ProgressDialog progressDialog;
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(
                            intent.getData(), null, null, null, null);
                    String temp_path = intent.getData().getPath();
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            int columnIndex = cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA);
                            photoPath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                        showLoading("",ScanActivity.this);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap img = BitmapUtils.getCompressedBitmap(photoPath);

                                BitmapDecoder decoder = new BitmapDecoder(ScanActivity.this);
                                Result result = decoder.getRawResult(img);
                                if (result != null) {
                                    Message m = mHandler.obtainMessage();
                                    m.what = PARSE_BARCODE_SUC;
                                    String resultStr = ResultParser.parseResult(result)
                                            .toString();
                                    m.obj = resultStr;
                                    LogUtils.e(TAG,"resultStr = " +resultStr);
                                    mHandler.sendMessage(m);
                                    mScanPresenter.queryDeviceAdmin(resultStr,mToken, MyApplication.sAcountId);
                                } else {
                                    Message m = mHandler.obtainMessage();
                                    m.what = PARSE_BARCODE_FAIL;
                                    mHandler.sendMessage(m);
                                }

                                dismissLoading();

                            }
                        }).start();
                        cursor.close();
                    }
                    break;
            }
        }

    }


    private Handler mHandler = new MyHandler(this);


    static class MyHandler extends Handler {

        private WeakReference<Activity> activityReference;

        public MyHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }



        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PARSE_BARCODE_SUC: // 解析图片成功

                    break;

                case PARSE_BARCODE_FAIL:// 解析图片失败
                    Toast.makeText(activityReference.get(), "解析图片失败",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    }


    /**
     * 解码处理回调
     *
     * @param result  结果
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        try {
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            mImei = result.getText();
            LogUtils.e(TAG, "handleDecode: " + mImei);
            if (mImei.isEmpty()) {
                printn(ScanActivity.this.getResources()
                        .getString(R.string.Scan_Failure));
                return;
            } else {
                mEtImei.setText(mImei);
                if (handler != null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (handler != null) {
                                handler.restartPreviewAndDecode();
                            }
                        }
                    }, 2000);

                }
                if (mImei.length() == 15) {
                    mScanPresenter.queryDeviceAdmin(mImei, mToken,MyApplication.sAcountId);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "扫描异常" + e.getMessage());
        }

    }



    /**
     * EditText监听
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 15) {
                mTvBindingEquipment.setVisibility(View.VISIBLE);
                mTvBindingEquipment.setEnabled(true);
                mTvBindingEquipment.setTextColor(Color.WHITE);
            } else {
                mTvBindingEquipment.setVisibility(View.GONE);
                mTvBindingEquipment.setEnabled(false);
                mTvBindingEquipment.setTextColor(Color.GRAY);
            }
        }
    };


    /**
     * 初始化二维码扫描器
     */
    public void initScan() {

        CameraManager.init(getApplication());
        mViewfinderView =  findViewById(R.id.viewfinder_view);
        mViewfinderView.setCameraManager(CameraManager.get());
        mViewfinderView.setVisibility(View.VISIBLE);

        inactivityTimer = new InactivityTimer(this);

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }


    /**
     * 初始化Camera设备
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            if (!mIsView){
                mIsView = true;
                if (handler == null) {
                    handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
                }
            }

        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }


    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }


    public Handler getHandler() {
        return handler;
    }


    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }


    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }


    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void cameraSuccess() {
//        if (mTextView != null)
//        mTextView.setText("");
        if (mIsPhotoAlbum){
            openPhotoAlbum();
            mIsPhotoAlbum = false;
        }

        if (mTvCamera != null)
            mTvCamera.setVisibility(View.GONE);
    }


    @Override
    protected void cameraError() {
        LogUtils.e(TAG,"权限被拒绝");
        printn(getString(R.string.cameraError));
        if (mTvCamera != null)
            mTvCamera.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsView = false;
        initScan();
    }


    @Override
    protected void onPause() {
        super.onPause();
        toCanmeraPermissions();
        LogUtils.e(TAG, "onPause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
            mIsView = true;
        }
        //mActivity Pause时关闭扫描设备
        CameraManager.get().closeDriver();
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + 22222);
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    public void error(String resultMsg) {
        printn(resultMsg);
        if (handler != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.restartPreviewAndDecode();
                    }
                }
            }, 2000);

        }
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
                finishActivityByAnimation(this);
        }
        return true;
    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mScanPresenter.mQueryBindInfoByImei != null) {
            mScanPresenter.mQueryBindInfoByImei.cancel();
        }
    }


    /**
     * 验证成功
     */
    public void AuthenticationSuccess() {
        toActivity(DeviceDataActivity.class,mImei);
        finishActivityByAnimation(this);
    }


    @Override
    public void cancel() {
        LogUtils.e(TAG,"申请绑定");
        mScanPresenter.applyBindDevice(MyApplication.sToken,MyApplication.sAcountId,MyApplication.sAcountName,mImei);
    }

}