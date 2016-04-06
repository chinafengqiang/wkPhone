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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.view.FProgrssDialog;
import com.xy.vo.AnswerVO;
import com.xy.vo.QuestionVO;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.R;


import xy.com.utils.AppConstants;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/9/18.
 */
public class QuestionFragment extends Fragment {
    private Context mContext;
    private View mBaseView;
    private QuestionVO question;
    private WebView question_chapter_wv,question_jx_wv,quesion_answer_wv;
    private RelativeLayout quesion_answer_rl,jx_rl;
    private TextView quesion_answer;
    private ImageView v_jx;

    public void  setQuestion(QuestionVO question){
        this.question = question;
    }

    public void setAnswer(){
        quesion_answer_rl.setVisibility(View.VISIBLE);
        jx_rl.setVisibility(View.VISIBLE);
        int queType = question.getQuetype();
        int questionId = question.getId();
        getAnswer(questionId,queType);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_chapterunit_question, null);
        initData();
        findView();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initData(){

    }

    private void findView(){
        question_chapter_wv = (WebView)mBaseView.findViewById(R.id.question_chapter_wv);
        question_jx_wv = (WebView)mBaseView.findViewById(R.id.question_jx_wv);
        quesion_answer_wv = (WebView)mBaseView.findViewById(R.id.quesion_answer_wv);
        quesion_answer_rl = (RelativeLayout)mBaseView.findViewById(R.id.quesion_answer_rl);
        jx_rl = (RelativeLayout)mBaseView.findViewById(R.id.jx_rl);
        quesion_answer = (TextView)mBaseView.findViewById(R.id.quesion_answer);
        v_jx = (ImageView)mBaseView.findViewById(R.id.v_jx);
    }

    private void setListener(){

    }

    private void processLogic(){
        if(question != null){
            String filePath = question.getConturl();
            if(StringUtils.isNotBlank(filePath)){
                question_chapter_wv.getSettings().setDefaultTextEncodingName("utf-8");
                question_chapter_wv.setHorizontalScrollBarEnabled(false);
                question_chapter_wv.loadUrl(AppConstants.SERVER_HOST + filePath);
            }
            String explPath = question.getQueexpl();
            if(StringUtils.isNotBlank(explPath)){
                question_jx_wv.getSettings().setDefaultTextEncodingName("utf-8");
                question_jx_wv.setHorizontalScrollBarEnabled(false);
                question_jx_wv.loadUrl(AppConstants.SERVER_HOST + explPath);
            }
            final String vUrl = question.getVurl();
            if(StringUtils.isNotBlank(vUrl)){
                v_jx.setVisibility(View.VISIBLE);
                v_jx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = AppConstants.SERVER_HOST+vUrl;
                        try {
                            Intent intent = getPlayIntent(url);
                            startActivity(intent);
                        } catch (Exception e) {
                            ToastUtil.showToast(mContext, R.string.v_jx_play_fail);
                        }
                    }
                });
            }else{
                v_jx.setVisibility(View.GONE);
            }
        }
    }

    public static Intent getPlayIntent( String param ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        //Uri uri = Uri.fromFile(new File(param));//此为播放本地文件
        Uri uri = Uri.parse(param);//此为播放Http网络文件
        intent.setDataAndType(uri,"video/*");
        return intent;
    }

    private void getAnswer(int questionId,final int queType){
        final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        pDialog.show();
        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_RIGHT_ANSWER;
        final String url = baseUrl + "?questionId=" + questionId;
        FastJsonRequest<AnswerVO> fastRequest = new FastJsonRequest<AnswerVO>(Request.Method.GET, url, AnswerVO.class, null, new Response.Listener<AnswerVO>() {

            @Override
            public void onResponse(AnswerVO resVO) {
                pDialog.dismiss();
                if (resVO != null) {
                    String answer = resVO.getRightAswer();
                    if(StringUtils.isNotBlank(answer)){
                        if(queType == AppConstants.QUESTION_TYPE_TK || queType == AppConstants.QUESTION_TYPE_JD){
                            quesion_answer.setVisibility(View.GONE);
                            quesion_answer_wv.setVisibility(View.VISIBLE);
                            quesion_answer_wv.getSettings().setDefaultTextEncodingName("utf-8");
                            quesion_answer_wv.setHorizontalScrollBarEnabled(false);
                            quesion_answer_wv.loadUrl(AppConstants.SERVER_HOST + answer);
                        }else {
                            quesion_answer.setVisibility(View.VISIBLE);
                            quesion_answer_wv.setVisibility(View.GONE);
                            quesion_answer.setText(answer);
                        }

                    }
                }
            }
        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        pDialog.dismiss();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }
}
