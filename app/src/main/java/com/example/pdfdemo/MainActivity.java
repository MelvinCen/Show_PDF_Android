package com.example.pdfdemo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

//        file = new File(getDownloadPath(), "test.pdf");
        file = new File(getDownloadPath(), "易前台v1.1操作手册.pdf");
        if (!file.exists()) {
            Toast.makeText(this,"文件不存在",Toast.LENGTH_LONG).show();
        }

        initWebView();


//        webView.loadUrl("https://www.baidu.com");

        RxPermissions rxPermissions = new RxPermissions(this);
        @NonNull Disposable disposable = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Throwable {
                        if (success) {
                            //1.只使用pdf.js渲染功能，自定义预览UI界面
//                            webView.loadUrl("file:///android_asset/index.html?" + FileProviderUtils.getUriForFile(MainActivity.this, file));
                            //2.pdf.js放到本地
//                            webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + file.getAbsolutePath());
                            //使用mozilla官方demo加载在线pdf
//                            webView.loadUrl("http://mozilla.github.io/pdf.js/web/viewer.html?file=" + pdfurl);
                        } else {
                            Toast.makeText(MainActivity.this,"没有权限",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);
        settings.setAllowUniversalAccessFromFileURLs(true); //设置可以访问URL
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
    }

    public static String getDownloadPath(){
        return Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator;
    }
}