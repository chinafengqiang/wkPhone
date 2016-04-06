package com.xy.wk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.view.FWebView;
import com.xy.view.TitleBarView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.ChapterUnitVO;
import com.xy.vo.TreeElementBean;
import com.xy.vo.UserCourseingSp;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import xy.com.utils.AppConstants;
import xy.com.utils.FileUtils;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by sara on 15-9-14.
 */
public class CourseStudyChapterResActivity extends BaseFragmentActivity {
    public static final String P_URL = "U_P_URL";
    public static final String V_URL = "U_V_URL";
    public static final String C_RES_ID = "C_RES_ID";
    public static final String C_UNIT_ID = "C_UNIT_ID";
    public static final String TITLE_TEXT = "RES_TITLE_TEXT";
    public static final String C_LOAD_FROM = "C_LOAD_FROM";
    public static final String C_RES_NAME = "C_RES_NAME";
    private String pUrl;
    private String vUrl;
    private TextView jxTextView;
    private WebView webView;
    private TitleBarView titleBarView;
    private String titleText;
    private SharedPreferences sp;
    private int courseId;
    private int loadFrom = 0;
    private String courseName="";
    private int unitId = 0;

    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;

    @Override
    protected void findViewById() {
        jxTextView = (TextView)findViewById(R.id.v_jx);
        webView = (WebView)findViewById(R.id.study_chapter_wv);
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
    }

    @Override
    protected void initTitle() {
        titleBarView.setTitleText(titleText);
        titleBarView.setTitleComeBackVisibility(View.VISIBLE);
        titleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
               // finish();
               // overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                comeBack();
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        pUrl = getIntent().getStringExtra(P_URL);
        vUrl = getIntent().getStringExtra(V_URL);
        titleText = getIntent().getStringExtra(TITLE_TEXT);
        courseId = getIntent().getIntExtra(C_RES_ID, 0);
        loadFrom = getIntent().getIntExtra(C_LOAD_FROM,0);
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
            jxTextView.setVisibility(View.VISIBLE);
            layoutParams.setMargins(0, titleHeight, 0, 80);
        }else{
            jxTextView.setVisibility(View.GONE);
            layoutParams.setMargins(0,titleHeight,0,0);
        }
        webView.setLayoutParams(layoutParams);
        if(StringUtils.isNotBlank(pUrl)){
            String filePath = FileUtils.getPhoneCourseFile(pUrl);
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.getSettings().setJavaScriptEnabled(true);
            /*webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);*/
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putInt(UserCourseingSp.C_ING_ID + "_" + courseId, courseId);
                    editor.putString(UserCourseingSp.C_ING_P_URL + "_" + courseId, pUrl);
                    editor.putString(UserCourseingSp.C_ING_V_URL + "_" + courseId, vUrl);
                    editor.putString(UserCourseingSp.C_ING_TITLE + "_" + courseId, titleText);
                    editor.putInt(UserCourseingSp.C_ING_UNIT_ID + "_" + courseId, unitId);

                    editor.commit();
                }
            });
            webView.loadUrl(AppConstants.SERVER_HOST+filePath);
        }

    }

    @Override
    protected void setListener() {
        jxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotBlank(vUrl)) {
                    String url = AppConstants.SERVER_HOST + vUrl;
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
        //Uri uri = Uri.fromFile(new File(param));//此为播放本地文件
        Uri uri = Uri.parse(param);//此为播放Http网络文件
        intent.setDataAndType(uri, "video/*");
        return intent;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            comeBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void comeBack(){
        if(loadFrom == 1){
            Intent intent = new Intent(mContext, CourseStudyChapterTreeActivity.class);
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

        showProgressDialog();

        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_CHAPTER_ORDER_UNIT;
        final String url = baseUrl + "?courseId="+courseId+"&id="+unitId+"&pos="+pos;
        FastJsonRequest<ChapterUnitVO> fastRequest = new FastJsonRequest<ChapterUnitVO>(Request.Method.GET,url, ChapterUnitVO.class,null, new Response.Listener<ChapterUnitVO>() {

            @Override
            public void onResponse(ChapterUnitVO chapterVO) {
                //pDialog.dismiss();
                closeProgressDialog();
                if(chapterVO != null){
                    unitId = chapterVO.getChapterUnitId();
                    pUrl = chapterVO.getChapterUnitUrl();
                    vUrl = chapterVO.getChapterUnitVideoUrl();
                    titleText = chapterVO.getChapterUnitName();
                    initTitle();
                    processLogic();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        //pDialog.dismiss();
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }
}
