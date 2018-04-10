package com.hojy.bracelet.recevier;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.ui.activity.HomeActivity;
import com.hojy.bracelet.ui.activity.TestActivity;
import com.hojy.bracelet.util.AppManagerUtils;
import com.hojy.bracelet.util.ExampleUtil;
import com.hojy.bracelet.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "HomeActivity";
	private static int mJpshNumber = 0;
	private static String mMessage = "";
	private Intent mStartIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				LogUtils.e(TAG, "[MyReceiver] 用户注册SDK的intent Id : " + regId);
                //send the Registration Id to your server...

				//自定义消息
			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				LogUtils.e(TAG, "[MyReceiver] 自定义消息 - " + intent.getAction() + ", extras: " + printBundle(bundle));
				JPushInterface.reportNotificationOpened(context,bundle.getString(JPushInterface.EXTRA_MSG_ID));
				processCustomMessage(context, bundle);

				//通知栏显示
			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//				Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION_ID);
//				msgIntent.putExtra("id",notifactionId);
//				LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");

				Activity activity = AppManagerUtils.getAppManager().currentActivity();
				if (mStartIntent == null)
				mStartIntent = new Intent(Intent.ACTION_MAIN);
				mStartIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				mStartIntent.setComponent(new ComponentName(context, activity.getClass()));//用ComponentName得到class对象
				mStartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
				context.startActivity(mStartIntent);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					LogUtils.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					LogUtils.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}


	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		LogUtils.e(TAG,"HomeActivity.isForeground = " +HomeActivity.isForeground);
		LogUtils.d(TAG,"HomeActivity.isForeground = " +bundle.getString(JPushInterface.EXTRA_EXTRA));
		if (HomeActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			LogUtils.e(TAG,"notifactionId =  " +notifactionId);
			Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
			msgIntent.putExtra("id",notifactionId);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}else {
			if (!mMessage.equals(bundle.getString(JPushInterface.EXTRA_MESSAGE))){
				LogUtils.e(TAG,"不是同样的消息");
				MyApplication.getCacheMap().put("JPush"+ mJpshNumber,bundle);
				mJpshNumber ++;
			}else {
				LogUtils.d(TAG,"同样的消息不存储");
			}
			mMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);

			LogUtils.e(TAG,"cacheMap.size = " +MyApplication.getCacheMap().size());
		}
	}
}
