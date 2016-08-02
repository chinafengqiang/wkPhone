package com.xy.wk;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.adapter.CoursePaperAdapter;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.PaperList;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.ArrayList;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/8/2.
 */
public class CoursePaperActivity extends BaseFragmentActivity{
    private Context mContext;
    private TitleBarView titleBarView;
    private int courseId;
    private int queClass;
    private ListView paperList;
    private CoursePaperAdapter paperAdapter;

    @Override
    protected void findViewById() {
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
        paperList = (ListView)findViewById(R.id.paperList);
    }

    @Override
    protected void initTitle() {
        titleBarView.setTitleComeBackVisibility(View.VISIBLE);
        titleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        if(AppConstants.QUESTION_CLASS_LNZT == queClass){
            titleBarView.setTitleText(R.string.zt_paper_title);
        }
        if(AppConstants.QUESTION_CLASS_MNLX == queClass){
            titleBarView.setTitleText(R.string.moni_paper_title);
        }
    }

    @Override
    protected void initSp() {
        courseId = getIntent().getIntExtra("courseId", 0);
        queClass = getIntent().getIntExtra("queClass", 0);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_course_paper);
        mContext = this;
}

    @Override
    protected void processLogic() {
        showProgressDialog();

        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_COURSE_PAPER;
        final String url = baseUrl + "?courseId="+courseId+"&queClass="+queClass;
        FastJsonRequest<PaperList> fastRequest = new FastJsonRequest<PaperList>(Request.Method.GET,url, PaperList.class,null, new Response.Listener<PaperList>() {

            @Override
            public void onResponse(PaperList paperListVO) {
                //pDialog.dismiss();
                closeProgressDialog();
                if(paperListVO != null){
                    paperAdapter = new CoursePaperAdapter(mContext,paperListVO.getPaperList());
                    paperList.setAdapter(paperAdapter);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest,tag_json_obj);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }

    protected void showProgressDialog() {
        closeProgressDialog();
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new FProgrssDialog(mContext);
        }

        this.progressDialog.show();
    }


    protected void closeProgressDialog() {
        if (this.progressDialog != null){
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
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
}
