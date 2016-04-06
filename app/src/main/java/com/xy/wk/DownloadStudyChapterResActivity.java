package com.xy.wk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xy.dao.CourseService;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.ChapterUnitVO;
import com.xy.vo.UserCourseingSp;

import java.io.File;

import xy.com.utils.AppConstants;
import xy.com.utils.FileService;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/1/27.
 */
public class DownloadStudyChapterResActivity extends BaseFragmentActivity{
    final String mimeType = "text/html";
    final String encoding = "utf-8";
    public static final String P_URL = "DOWNLOAD_P_URL";
    public static final String V_URL = "DOWNLOAD_V_URL";
    public static final String TITLE_TEXT = "DOWNLOAD_RES_TITLE_TEXT";
    public static final String C_RES_ID = "DOWNLOAD_RES_ID";
    public static final String C_LOAD_FROM = "DOWNLOAD_LOAD_FROM";
    public static final String C_RES_NAME = "DOWNLOAD_RES_NAME";
    public static final String C_UNIT_ID = "DOWNLOAD_UNIT_ID";
    private String pUrl;
    private String vUrl;
    private WebView webView;
    private TitleBarView titleBarView;
    private String titleText;
    private WebSettings mWebSettings;
    private TextView v_jx;
    private int courseId;
    private SharedPreferences sp;
    private int loadFrom = 0;
    private String courseName="";
    private int unitId = 0;
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;

    @Override
    protected void findViewById() {
        webView = (WebView)findViewById(R.id.study_chapter_wv);
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
        v_jx = (TextView)findViewById(R.id.v_jx);
    }

    @Override
    protected void initTitle() {
        titleBarView.setTitleText(titleText);
        titleBarView.setTitleComeBackVisibility(View.VISIBLE);
        titleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comeBack();
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        pUrl = getIntent().getStringExtra(P_URL);
        vUrl = getIntent().getStringExtra(V_URL);
        courseId = getIntent().getIntExtra(C_RES_ID, 0);
        titleText = getIntent().getStringExtra(TITLE_TEXT);
        loadFrom = getIntent().getIntExtra(C_LOAD_FROM, 0);
        courseName = getIntent().getStringExtra(C_RES_NAME);
        unitId = getIntent().getIntExtra(C_UNIT_ID, 0);
    }

    @Override
    protected void loadViewLayout() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        setContentView(R.layout.course_study_chapter_res);
        gestureDetector = new GestureDetector(mContext,onGestureListener);
    }

    @Override
    protected void processLogic() {
        RelativeLayout.LayoutParams layoutParams
                =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        titleBarView.measure(w, h);
        int titleHeight = titleBarView.getMeasuredHeight();
        if(StringUtils.isNotBlank(vUrl)){
            v_jx.setVisibility(View.VISIBLE);
            layoutParams.setMargins(0, titleHeight, 0, 80);
        }else{
            v_jx.setVisibility(View.GONE);
            layoutParams.setMargins(0,titleHeight,0,0);
        }

        webView.setLayoutParams(layoutParams);
        if(StringUtils.isNotBlank(pUrl)){
            FileService fileService = new FileService(mContext);
            String html = fileService.getSdCardDirectory()+"/"+pUrl;

            mWebSettings = webView.getSettings();
            mWebSettings.setJavaScriptEnabled(true);
            mWebSettings.setBuiltInZoomControls(true);
            mWebSettings.setLightTouchEnabled(true);
            mWebSettings.setSupportZoom(true);
            mWebSettings.setDefaultTextEncodingName("utf-8");
            webView.setHapticFeedbackEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putInt(UserCourseingSp.C_ING_ID+"_L_"+courseId,courseId);
                    editor.putString(UserCourseingSp.C_ING_P_URL + "_L_" + courseId, pUrl);
                    editor.putString(UserCourseingSp.C_ING_V_URL+"_L_"+courseId,vUrl);
                    editor.putString(UserCourseingSp.C_ING_TITLE+"_L_"+courseId,titleText);
                    editor.putInt(UserCourseingSp.C_ING_UNIT_ID + "_L_" + courseId, unitId);
                    editor.commit();
                }
            });
            webView.loadUrl("file:///" + html);
        }
    }

    @Override
    protected void setListener() {
        v_jx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotBlank(vUrl)) {
                    FileService fileService = new FileService(mContext);
                    String url = fileService.getSdCardDirectory() + "/" + vUrl;
                    try {
                        Intent intent = getPlayIntent(url);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtil.showToast(mContext, R.string.v_jx_play_fail);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    public static Intent getPlayIntent( String param ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));//此为播放本地文件
        //Uri uri = Uri.parse(param);//此为播放Http网络文件
        intent.setDataAndType(uri,"video/*");
        return intent;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
           comeBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void comeBack(){
        if(loadFrom == 1){
            Intent intent = new Intent(mContext, DownloadChapterTreeActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("courseName",courseName);
            startActivity(intent);
        }else{
            onBackPressed();
        }
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ToastUtil.showToast(mContext, "event");
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT://pre
                //ToastUtil.showToast(mContext, "go right");
                loadUnit(-1);
                break;

            case LEFT://next
                //ToastUtil.showToast(mContext, "go left");
                loadUnit(1);
                break;

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {    //注意这里不能用ONTOUCHEVENT方法，不然无效的
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }



    public void loadUnit(int pos){
        new LoadData().execute(pos);
    }

    private class LoadData extends AsyncTask<Integer,Void,Integer> {
        FProgrssDialog pDialog = null;
        ChapterUnitVO unitVO = null;
        @Override
        protected void onPreExecute() {
            //pDialog = new FProgrssDialog(mContext);
            //pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            CourseService courseService = new CourseService(mContext);
            unitVO = courseService.getOrderUnit(courseId,unitId,integers[0]);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //pDialog.dismiss();
            if(unitVO!=null){
                unitId = unitVO.getChapterUnitId();
                titleText = unitVO.getChapterUnitName();
                String videoUrl = unitVO.getChapterUnitVideoUrl();
                if(StringUtils.isNotBlank(videoUrl)){
                    vUrl = AppConstants.COURSE_DOWNLOAD_URL+"/"+courseId+"/"+AppConstants.COURSE_DOWNLOAD_VIDEO_DIR+"/"+videoUrl;
                }else{
                    vUrl = "";
                }
                pUrl = AppConstants.COURSE_DOWNLOAD_URL+"/"+courseId+"/"+AppConstants.COURSE_DOWNLOAD_HTML_DIR+"/"+unitId+".html";
                initTitle();
                processLogic();
            }
        }
    }
}
