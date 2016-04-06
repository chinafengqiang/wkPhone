package com.xy.wk;

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
import com.xy.view.TitleBarView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.ArrayList;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by sara on 15-9-14.
 */
public class CourseStudyChapterTreeActivity extends BaseFragmentActivity {

    private int courseId;
    private String courseName = "";
    private ListView listView;
    private ChapterTreeAdapter treeViewAdapter = null;
    private TitleBarView titleBarView;
    private boolean isQuestionChapter = false;
    @Override
    protected void findViewById() {
        listView = (ListView)findViewById(R.id.treeList);
        titleBarView = (TitleBarView)findViewById(R.id.titleBar);
    }

    @Override
    protected void initTitle() {
        titleBarView.setTitleText(courseName);
        titleBarView.setTitleComeBackVisibility(View.VISIBLE);
        titleBarView.setTitleComeBackListener(new View.OnClickListener() {
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
        courseId = getIntent().getIntExtra("courseId",0);
        courseName = getIntent().getStringExtra("courseName");
        isQuestionChapter = getIntent().getBooleanExtra("isQuestionChapter",false);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.course_study_chapter_tree);
    }

    @Override
    protected void processLogic() {
        loadTree();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }

    public void loadTree(){
        //final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        //pDialog.show();
        showProgressDialog();

        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_COURSE_TREE;
        final String url = baseUrl + "?courseId="+courseId;
        FastJsonRequest<ChapterTreeVO> fastRequest = new FastJsonRequest<ChapterTreeVO>(Request.Method.GET,url, ChapterTreeVO.class,null, new Response.Listener<ChapterTreeVO>() {

            @Override
            public void onResponse(ChapterTreeVO chapterVO) {
                //pDialog.dismiss();
                closeProgressDialog();
                if(chapterVO != null){
                    final ArrayList<TreeElementBean> nodeList = (ArrayList<TreeElementBean>) chapterVO.getChapterList();
                    treeViewAdapter = new ChapterTreeAdapter(mContext, R.layout.course_study_chapter_left_item,nodeList,courseId);
                    listView.setAdapter(treeViewAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?> adapter, View view, int position,long id) {
                            treeViewAdapter.onClick(position, nodeList, treeViewAdapter,isQuestionChapter);
                        }
                    });
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

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest,tag_json_obj);
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
