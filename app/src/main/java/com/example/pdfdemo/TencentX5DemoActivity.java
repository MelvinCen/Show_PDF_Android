package com.example.pdfdemo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.tbruyelle.rxpermissions3.RxPermissions;



public class TencentX5DemoActivity extends AppCompatActivity {

    private X5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tencent_x5_demo);

        webView = findViewById(R.id.webView);


        //Android6.0申请动态权限
        RxPermissions rxPermissions = new RxPermissions(this);
        @NonNull Disposable disposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Throwable {
                        if (success) {
                            showWeb();
                        } else {
                            Toast.makeText(TencentX5DemoActivity.this,"没有权限",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void showWeb() {

        webView.loadUrl("https://www.baidu.com");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //处理返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}