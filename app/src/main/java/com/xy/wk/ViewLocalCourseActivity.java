package com.xy.wk;

import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * Created by FQ.CHINA on 2015/11/20.
 */
public class ViewLocalCourseActivity extends  BaseFragmentActivity{
    private WebView webview;
    private String rootPath;
    @Override
    protected void findViewById() {
        webview = (WebView)findViewById(R.id.webview);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initSp() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_view_course);
    }

    @Override
    protected void processLogic() {
        rootPath = Environment.getExternalStorageDirectory().getPath()
                + "/myChapter";
        String url = "file:///"+rootPath+ File.separator+"2/1_phone.html";
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }
}
