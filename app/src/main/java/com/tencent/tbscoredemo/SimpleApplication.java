package com.tencent.tbscoredemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.x5library.Constants;
import com.tencent.x5library.TBSSdkManage;
import com.tencent.x5library.http.callback.TbsInstallListener;

import java.io.File;

/**
 * created by zyh
 * on 2021/1/15
 */
public class SimpleApplication extends Application {
    private static final String TAG = SimpleApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        installX5Core(this);
    }

    /**
     * 动态集成
     */
    public void installX5Core(Context context) {
        Log.e(TAG,"context is null ?:"+context);
        Log.e(TAG,"getApplication is null ?:"+context.getApplicationContext());
        TBSSdkManage tbsSdkManage = new TBSSdkManage.Builder(context)
                .connectTimeout(3 * 1000) //设置连接超时时间,选填 默认10s
                .readTimeout(3 * 1000)     //设置读取超时时间，选填 默认10s
                .coreUrl(Constants.X5CORE_URL_32)  //设置x5内核Url
                .md5Url(Constants.X5CORE_URL_32_MD5) //设置x5内核md5值url
                .localPath(this.getFilesDir().getPath() + File.separator + Constants.X5CORE_FILE_NAME)//设置内核本地存放地址
                .tbsListener(new TbsInstallListener() {
                    /**
                     * 下载成功时回调此方法
                     * @param
                     */
                    @Override
                    public void onDownloadFinish(int i) {

                        Log.d(TAG, "onDownloadFinish");
                    }

                    /**
                     * 安装成功时回调此方法
                     * @param i
                     */
                    @Override
                    public void onInstallFinish(int i) {
                        Log.d(TAG, "onInstallFinish");
                    }

                    /**
                     * 下载进度
                     * @param currentSize
                     * @param totalSize
                     */
                    @Override
                    public void onProgress(int currentSize, int totalSize) {
                        Log.d(TAG, "onProgress currentSize: " + currentSize + " totalSize:" + totalSize);
                    }

                    /**
                     * 下载或安装失败时回调此方法
                     * @param errorCode
                     * @param msg
                     */
                    @Override
                    public void onError(int errorCode, String msg) {
                        Log.d(TAG, "onError errorCode: " + errorCode + " msg:" + msg);
                    }
                }).build();
        boolean isInstallSuccess = tbsSdkManage.isInstallSuccess(); //内核是否已安装成功
        Log.w(TAG, "isInstallSuccess: " + isInstallSuccess);
        tbsSdkManage.initX5Core();
    }
}
