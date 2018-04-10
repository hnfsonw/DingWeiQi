package com.hojy.bracelet.recevier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.ui.activity.LoginActivity;
import com.hojy.bracelet.util.LogUtils;

import java.io.File;


import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Teprinciple on 2017/11/3.
 */

 public class UpdateAppReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

//        int notifyId = 1;
//        int progress = intent.getIntExtra("progress", 0);
//        String title = intent.getStringExtra("title");
//
//
//        NotificationManager nm = null;
//        if (UpdateAppUtils.showNotification){
//            Intent intent2 = new Intent(context, LoginActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
//
//            Notification build = new NotificationCompat.Builder(context)
//                    .setContentTitle("正在下载 " + title)
//                    .setSmallIcon(android.R.mipmap.sym_def_app_icon)
//                    .setProgress(100, progress, false)
//                    .setContentIntent(pendingIntent).build();
//
//
//            nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            nm.notify(notifyId,build);
//        }
//
//
//        if (progress == 100){
//            if (nm != null)nm.cancel(notifyId);
//
//            if (LoginActivity.sDownloadUpdateApkFilePath != null) {
//                LogUtils.e("apk下载完成  准备安装");
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                File apkFile = new File(LoginActivity.sDownloadUpdateApkFilePath);
//                if ( UpdateAppUtils.needFitAndroidN &&  Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    Uri contentUri = FileProvider.getUriForFile(
//                            context, context.getPackageName() + ".fileprovider", apkFile);
//                    i.setDataAndType(contentUri, "application/vnd.android.package-archive");
//                } else {
//                    i.setDataAndType(Uri.fromFile(apkFile),
//                            "application/vnd.android.package-archive");
//                }
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//            }
//        }
    }
}
