package com.hojy.bracelet.download;

/**
 * 创建时间：2018/3/24
 */

public interface DownloadCallBack {

    void onProgress(int progress);

    void onCompleted();

    void onError(String msg);

}
