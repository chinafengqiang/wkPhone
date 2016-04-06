package com.xy.wk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.fragment.QuestionFragment;
import com.xy.view.TitleBarView;
import com.xy.view.TouchWebView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.IntegerVO;
import com.xy.vo.LoginInfo;
import com.xy.vo.QuestionList;
import com.xy.vo.QuestionVO;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by sara on 15-9-15.
 */
public class CourseQuestionChapterActivity extends BaseFragmentActivity {
    private WebView webView;
    private WebView jxWebView;
    private View currentButton;
    private TitleBarView mTitleBarView;
    private List<QuestionVO> questionList = new ArrayList<QuestionVO>();
    private int courseId;
    private int chapterUnitId;
    private int queClass;
    private Button pre_que_btn,que_answer_btn,next_que_btn,que_answer_fk,que_answer_sc;
    private int number = 0;
    private int totals;
    private boolean isDafaultClick = false;
    private String title;
    private SharedPreferences sp;
    private int userId;
    private EditText question_resp;
    @Override
    protected void findViewById() {
        webView = (WebView) findViewById(R.id.question_chapter_wv);
        jxWebView = (WebView) findViewById(R.id.question_jx_wv);
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        pre_que_btn = (Button)findViewById(R.id.pre_que_btn);
        que_answer_btn = (Button)findViewById(R.id.que_answer_btn);
        next_que_btn = (Button)findViewById(R.id.next_que_btn);
        que_answer_sc = (Button)findViewById(R.id.que_answer_sc);
        que_answer_fk = (Button)findViewById(R.id.que_answer_fk);
    }

    @Override
    protected void initTitle() {
        mTitleBarView.setTitleText(title);
        mTitleBarView.setTitleComeBackVisibility(View.VISIBLE);
        mTitleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        userId = sp.getInt(LoginInfo.USER_ID, 0);
        courseId = getIntent().getIntExtra("courseId", 0);
        chapterUnitId = getIntent().getIntExtra("chapterUnitId", 0);
        queClass = getIntent().getIntExtra("queClass", 1);
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void loadViewLayout() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        setContentView(R.layout.course_question_chapter_res);
    }

    @Override
    protected void processLogic() {
        showProgressDialog();

        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_QUETION_CHAPTERUNIT;
        final String userQueUrl = AppConstants.API_GET_USER_TYPE_QUESTION;
        String url = "";
        if(queClass >= 100){
            que_answer_sc.setText(R.string.my_que_remove);
            int type = (queClass==200)?1:0;
            url = userQueUrl + "?userId="+userId+"&type="+type;
        }else{
            url = baseUrl + "?courseId=" + courseId+"&chapterUnit="+chapterUnitId+"&queClass="+queClass;
        }
        FastJsonRequest<QuestionList> fastRequest = new FastJsonRequest<QuestionList>(Request.Method.GET, url, QuestionList.class, null, new Response.Listener<QuestionList>() {

            @Override
            public void onResponse(QuestionList resVO) {
                //pDialog.dismiss();
                closeProgressDialog();
                if (resVO != null) {
                    totals = resVO.getQuestionList().size();
                    if(totals > 0) {
                        mTitleBarView.setTitleText(title+"(共"+totals+"题)");
                        questionList.addAll(resVO.getQuestionList());
                        isDafaultClick = true;
                        pre_que_btn.performClick();
                    }else {
                        ToastUtil.showToast(mContext,R.string.question_not_exist);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    @Override
    protected void setListener() {
        pre_que_btn.setOnClickListener(preBtnClick);
        next_que_btn.setOnClickListener(nextBtnClick);
        que_answer_btn.setOnClickListener(answerBtnClick);
        if(queClass >= 100){
            que_answer_sc.setOnClickListener(removeBtnClick);
        }else {
            que_answer_sc.setOnClickListener(scBtnClick);
        }
        que_answer_fk.setOnClickListener(fkBtnClick);
    }

    @Override
    public void onClick(View view) {

    }


    QuestionFragment fragment = null;
    private View.OnClickListener preBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           if(totals > 0){
               fragment = new QuestionFragment();
               if(--number < 0 && !isDafaultClick){
                   number = 0;
                   ToastUtil.showToast(mContext,R.string.question_is_maxpre);
               }
               isDafaultClick = false;
               number = number < 0 ? 0 : number;
               QuestionVO question = questionList.get(number);
               fragment.setQuestion(question);
               FragmentManager fm = getSupportFragmentManager();
               FragmentTransaction ft = fm.beginTransaction();
               ft.replace(R.id.que_content, fragment, "QuestionFragment");
               ft.commit();
           }else {
               ToastUtil.showToast(mContext, R.string.question_not_exist);
           }
        }
    };

    private View.OnClickListener answerBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(fragment != null){
                fragment.setAnswer();
            }
        }
    };

    private View.OnClickListener nextBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(totals > 0){
                fragment = new QuestionFragment();
                if(++number > (totals-1)){
                    number = totals-1;
                    ToastUtil.showToast(mContext,R.string.question_is_maxnext);
                }
                //number = number >= totals ? totals-1 : number;
                QuestionVO question = questionList.get(number);
                fragment.setQuestion(question);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.que_content, fragment, "QuestionFragment");
                ft.commit();
            }else{
                ToastUtil.showToast(mContext, R.string.question_not_exist);
            }

        }
    };

    private View.OnClickListener scBtnClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            showProgressDialog();
            QuestionVO question = getCurrentQuestion();
            if(question == null) {
                closeProgressDialog();
                ToastUtil.showToast(mContext,R.string.que_answer_sc_fail);
                return;
            }
            int questionId = question.getId();
            String tag_json_obj = "json_obj_req";
            final String baseUrl = AppConstants.API_SAVE_USER_QUESTION;
            final String url = baseUrl + "?type=0&userId="+userId+"&courseId="+courseId+"&questionId="+questionId;
            FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.GET, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

                @Override
                public void onResponse(IntegerVO resVO) {
                    closeProgressDialog();
                    if (resVO != null && resVO.getRet() == 200) {
                        ToastUtil.showToast(mContext,R.string.que_answer_sc_ok);
                    }else if(resVO.getRet() == 401){
                        ToastUtil.showToast(mContext,R.string.que_answer_sced);
                    }
                    else{
                        ToastUtil.showToast(mContext,R.string.que_answer_sc_fail);
                    }
                }
            },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                            closeProgressDialog();
                        }
                    }
            );

            FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
        }
    };

    private View.OnClickListener fkBtnClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater factory = LayoutInflater.from(mContext);
            final View textEntryView = factory.inflate(R.layout.activity_question_resp, null);
            builder.setView(textEntryView);
            builder.setPositiveButton(getString(R.string.btn_cfm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    question_resp = (EditText)textEntryView.findViewById(R.id.question_resp);
                    String resp = question_resp.getText().toString().trim();
                    if(StringUtils.isBlank(resp)){
                        ToastUtil.showToast(mContext,R.string.que_answer_resp_content_isnull);
                        dismisDialog(dialog, false);
                    }else{
                        try {
                            resp = URLEncoder.encode(resp, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        submitResp(resp);
                        dismisDialog(dialog, true);
                    }

                }
            });
            builder.setNegativeButton(getString(R.string.btn_cancle), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dismisDialog(dialog, true);
                }
            });
            builder.create().show();
        }
    };


    private View.OnClickListener removeBtnClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            showProgressDialog();
            final QuestionVO question = getCurrentQuestion();
            if(question == null) {
                closeProgressDialog();
                ToastUtil.showToast(mContext,R.string.my_que_remove_fail);
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.my_que_remove_title).setMessage(getString(R.string.my_que_remove_cfm)).setCancelable(false)
                    .setPositiveButton(R.string.btn_cfm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int questionId = question.getId();
                            int type = (queClass==200)?1:0;
                            String tag_json_obj = "json_obj_req";
                            final String baseUrl = AppConstants.API_REMOVE_USER_TYPE_QUESTION;
                            final String url = baseUrl + "?type="+type+"&userId="+userId+"&questionId="+questionId;
                            FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.GET, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

                                @Override
                                public void onResponse(IntegerVO resVO) {
                                    closeProgressDialog();
                                    if (resVO != null && resVO.getRet() == 200) {
                                        ToastUtil.showToast(mContext,R.string.my_que_remove_ok);
                                    }
                                    else{
                                        ToastUtil.showToast(mContext,R.string.my_que_remove_fail);
                                    }
                                }
                            },
                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                                            closeProgressDialog();
                                        }
                                    }
                            );

                            FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
                        }
                    }).setNegativeButton(R.string.btn_cancle,null).create().show();

        }
    };

    private QuestionVO getCurrentQuestion(){
        QuestionVO question = null;
        if(questionList != null && questionList.size() > 0)
            question = questionList.get(number);
        return question;
    }

    private void setButton(View v){
        if(currentButton!=null&&currentButton.getId()!=v.getId()){
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton=v;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dismisDialog(DialogInterface dialog,boolean dismis){
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, dismis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitResp(final String resp){
        showProgressDialog();
        QuestionVO question = getCurrentQuestion();
        if(question == null) {
            ToastUtil.showToast(mContext,R.string.que_answer_sc_fail);
            return;
        }
        int questionId = question.getId();
        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_SAVE_USER_QUESTION_RESP;
        final String url = baseUrl + "?userId="+userId+"&resp="+resp+"&questionId="+questionId;
        FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.GET, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

            @Override
            public void onResponse(IntegerVO resVO) {
                closeProgressDialog();
                if (resVO != null && resVO.getRet() == 200) {
                    ToastUtil.showToast(mContext,R.string.que_answer_resp_ok);
                }
                else{
                    ToastUtil.showToast(mContext,R.string.que_answer_resp_fail);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }
}

