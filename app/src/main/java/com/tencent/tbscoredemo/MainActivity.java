package com.tencent.tbscoredemo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TEST_X5Core";
    private static final int REQUEST_CODE = 1;
    private Button loadBtn;
    private FrameLayout webLayout;
    private WebView mWebView;
    private boolean isX5CoreInstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  checkPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBtn = (Button) findViewById(R.id.btn_load);
        loadBtn.setOnClickListener(this);
        webLayout = (FrameLayout) findViewById(R.id.layout_web);

        installX5Core();
    }

    /**
     * 静态集成
     */
    private void installX5Core() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                isX5CoreInstall = QbSdk.preinstallStaticTbs(MainActivity.this);
                int version = QbSdk.getTbsVersion(MainActivity.this);
                Log.d(TAG, "isX5CoreInstall: " + isX5CoreInstall + " tbsVersion:" + version);
            }
        }).start();*/
    }

    protected void checkPermissions(final String[] permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        List<String> requestPermission = new ArrayList<>(Arrays.asList(permissions));

        for (String p : permissions) {
            if (checkPermissionGranted(p)) {
                requestPermission.remove(p);
            }
        }

        Log.d(TAG, "checkPermissions: " + requestPermission);

        if (requestPermission.size() < 1) {
            return;
        }

        String[] realRequest = requestPermission.toArray(new String[0]);
        ActivityCompat.requestPermissions(this, realRequest, REQUEST_CODE);
    }

    private boolean checkPermissionGranted(String permission) {
        return (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission =
                            ActivityCompat
                                    .shouldShowRequestPermissionRationale(MainActivity.this,
                                            permissions[i]);
                    if (showRequestPermission) {
                        postToast(permissions[i] + " 权限未申请");
                    }
                }

            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void postToast(String msg) {
        postToast(msg, false);
    }

    protected void postToast(final String msg, final boolean toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG,"release WebView");
        if (mWebView != null && mWebView.getContext() != null) {
            if (mWebView.getParent() != null) {
                ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            }
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.clearCache(true);
            mWebView.destroy();
        }
    }

    private void initWebView() {
        /*if(!isX5CoreInstall){
            return;
        }*/
        if (mWebView == null ) {
            mWebView = new WebView(this);
        } else {
            return;
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);        //缓存
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setTextZoom(100);
        final String url = "https://www.baidu.com";
        mWebView.loadUrl(url);

        Log.d(TAG, "getUserAgentString:" + settings.getUserAgentString());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webLayout.addView(mWebView, layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load:
                initWebView();
                break;
        }
    }
}