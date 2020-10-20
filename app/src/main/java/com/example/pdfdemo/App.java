package com.example.pdfdemo;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

/**
 * Created by censheng on 2020/10/20.
 *
 * @description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
    }

    private void initX5() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。

                Log.e("app", "x5初始化结果====" + arg0);
//                if (arg0 != true) {
//                    //设置自带webView属性
//                    WebView webView = new WebView(getApplicationContext());
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.getSettings().setBlockNetworkImage(false);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//                    }
//                }
            }

            @Override
            public void onCoreInitFinished() {
                Log.e("app", "========onCoreInitFinished===");
            }
        };
        //流量下载内核
        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
