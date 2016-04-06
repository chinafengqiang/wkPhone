package com.xy.wk;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import xy.com.utils.AppConstants;

/**
 * Created by FQ.CHINA on 2015/9/14.
 */
public class QuestionChapterActivity extends BaseFragmentActivity{
    private WebView webView;
    private TextView jxTextView;
    @Override
    protected void findViewById() {
        jxTextView = (TextView)findViewById(R.id.v_jx);
        webView = (WebView)findViewById(R.id.study_chapter_wv);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initSp() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.fragment_study_chapter);
    }

    @Override
    protected void processLogic() {
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(AppConstants.SERVER_HOST+"/upload/1/5/7_phone.html");
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }
}
