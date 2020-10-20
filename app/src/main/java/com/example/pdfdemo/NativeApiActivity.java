package com.example.pdfdemo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import android.Manifest;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.io.IOException;

public class NativeApiActivity extends AppCompatActivity {

    private ImageView ivShowPdf;
    private PdfRenderer.Page mCurrentPage;
    private PdfRenderer mPdfRenderer;
    private ParcelFileDescriptor mParcelFileDescriptor;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_api);

        ivShowPdf = findViewById(R.id.show_pdf);

        //Android6.0申请动态权限
        RxPermissions rxPermissions = new RxPermissions(this);
        @NonNull Disposable disposable = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Throwable {
                        if (success) {
                            openPdf();
                        } else {
                            Toast.makeText(NativeApiActivity.this,"没有权限",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void openPdf() {
        try {
            file = new File(getDownloadPath(), "test.pdf");
            if (!file.exists()) {
                Toast.makeText(this,"文件不存在",Toast.LENGTH_LONG).show();
            }

//            mParcelFileDescriptor = getAssets().openFd("test.pdf").getParcelFileDescriptor();
            mParcelFileDescriptor = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(mParcelFileDescriptor);
            mCurrentPage = mPdfRenderer.openPage(0);
            //Bitmap必须是ARGB，不可以是RGB
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth()*2, mCurrentPage.getHeight()*2,
                    Bitmap.Config.ARGB_8888);
            /*
             * 调用PdfRender.Page的render方法渲染bitmap
             *
             * render的参数说明：
             * destination : 要渲染的bitmap对象
             * destClip ：传一个矩形过去 矩形的尺寸不能大于bitmap的尺寸 最后渲染的pdf会是rect的大小 可为null
             * transform : 一个Matrix bitmap根据该Matrix图像进行转换
             * renderMode ：渲染模式 可选2种 RENDER_MODE_FOR_DISPLAY 和 RENDER_MODE_FOR_PRINT
             */
            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            ivShowPdf.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 按顺序关闭资源
     * @throws IOException
     */
    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        if (null != mPdfRenderer) {
            mPdfRenderer.close();
        }
        if (null != mParcelFileDescriptor) {
            mParcelFileDescriptor.close();
        }
    }

    public static String getDownloadPath(){
        return Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator;
    }

}