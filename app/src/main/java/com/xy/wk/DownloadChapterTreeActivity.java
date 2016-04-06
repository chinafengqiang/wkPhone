package com.xy.wk;

import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.adapter.DownloadChapterTreeAdapter;
import com.xy.adapter.DownloadCourseAdapter;
import com.xy.dao.CourseService;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.CourseVo;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/1/27.
 */
public class DownloadChapterTreeActivity extends BaseFragmentActivity{
    private int courseId;
    private String courseName = "";
    private ListView listView;
    private DownloadChapterTreeAdapter treeViewAdapter = null;
    private TitleBarView titleBarView;
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
        courseId = getIntent().getIntExtra("courseId", 0);
        courseName = getIntent().getStringExtra("courseName");
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.course_study_chapter_tree);
    }

    @Override
    protected void processLogic() {
       new LoadTree().execute(1);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

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

    private class LoadTree extends AsyncTask<Integer,Void,Integer> {
        FProgrssDialog pDialog = null;
        List<TreeElementBean> treeList;
        @Override
        protected void onPreExecute() {
            pDialog = new FProgrssDialog(mContext);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            CourseService courseService = new CourseService(mContext);
            treeList = courseService.getDownloadChapter(courseId,0,-1);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();
            if(treeList != null){
                treeViewAdapter = new DownloadChapterTreeAdapter(mContext, R.layout.course_study_chapter_left_item,treeList,courseId);
                listView.setAdapter(treeViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapter, View view, int position,long id) {
                        treeViewAdapter.onClick(position, treeList, treeViewAdapter);
                    }
                });
            }
        }
    }
}
