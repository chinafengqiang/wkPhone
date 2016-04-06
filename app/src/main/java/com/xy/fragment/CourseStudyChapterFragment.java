package com.xy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.xy.view.TouchWebView;
import com.xy.wk.R;

import java.io.File;

import xy.com.utils.AppConstants;
import xy.com.utils.FileUtils;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/9/14.
 */
public class CourseStudyChapterFragment extends Fragment{
    public static final String P_URL = "U_P_URL";
    public static final String V_URL = "U_V_URL";
    private Context mContext;
    private String pUrl;
    private String vUrl;
    private View mBaseView;
    private TouchWebView webView;
    private TextView jxTextView;

    public static Fragment newInstance(String purl,String vurl){
        CourseStudyChapterFragment fragment = new CourseStudyChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(P_URL, purl);
        bundle.putString(V_URL, vurl);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_study_chapter, null);
        initData();
        findView();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initData(){
        Bundle bundle = getArguments();
        pUrl = bundle.getString(P_URL);
        vUrl = bundle.getString(V_URL);
    }

    private void findView(){
        jxTextView = (TextView)mBaseView.findViewById(R.id.v_jx);
        webView = (TouchWebView)mBaseView.findViewById(R.id.study_chapter_wv);
    }

    private void setListener(){
        jxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isNotBlank(vUrl)){
                    String url = AppConstants.SERVER_HOST+vUrl;
                    try {
                        Intent intent = getPlayIntent(url);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtil.showToast(mContext,R.string.v_jx_play_fail);
                    }
                }
            }
        });
    }

    private void processLogic(){
        if(StringUtils.isNotBlank(vUrl)){
            jxTextView.setVisibility(View.VISIBLE);
        }else{
            jxTextView.setVisibility(View.GONE);
        }
        if(StringUtils.isNotBlank(pUrl)){
            String filePath = FileUtils.getPhoneCourseFile(pUrl);
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.setHorizontalScrollBarEnabled(false);
            webView.loadUrl(AppConstants.SERVER_HOST+filePath);
        }
    }


    public static Intent getPlayIntent( String param ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri,"video/*");
        return intent;
    }
}
