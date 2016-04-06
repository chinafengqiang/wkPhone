package com.xy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xy.adapter.DownloadCourseAdapter;
import com.xy.dao.CourseService;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.CourseVo;
import com.xy.vo.UserCourseingSp;
import com.xy.wk.CourseStudyActivity;
import com.xy.wk.CourseStudyChapterTreeActivity;
import com.xy.wk.DownloadChapterTreeActivity;
import com.xy.wk.DownloadStudyChapterResActivity;
import com.xy.wk.R;
import com.xy.wk.ViewLocalCourseActivity;

import java.util.ArrayList;
import java.util.List;

import xy.com.utils.SpUtil;

/**
 * Created by FQ.CHINA on 2015/11/20.
 */
public class DownloadFragment extends Fragment {
    private Context mContext;
    private View mBaseView;
    private TitleBarView mTitleBarView;
    private DownloadCourseAdapter adapter;
    private ListView courseListView;
    private  List<CourseVo> courseList = null;
    private SharedPreferences sp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_user_course_list, null);
        initData();
        findView();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initData(){
        sp = SpUtil.getSharePerference(mContext);
    }

    private void findView(){
        mTitleBarView = (TitleBarView)mBaseView.findViewById(R.id.title_bar);
        courseListView = (ListView)mBaseView.findViewById(R.id.courseList);
    }

    private void initTitle(){
        mTitleBarView.setTitleText(R.string.download_title);
    }

    private void processLogic(){
        new LoadData().execute(1);
    }

    private void setListener(){
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                if (courseList != null && courseList.size() > 0 && postion < courseList.size() && postion >= 0) {
                    CourseVo course = courseList.get(postion);
                    gotoCourse(course);
                }
            }
        });
    }

    private class LoadData extends AsyncTask<Integer,Void,Integer>{
        FProgrssDialog pDialog = null;
        @Override
        protected void onPreExecute() {
            pDialog = new FProgrssDialog(mContext);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            CourseService courseService = new CourseService(mContext);
            courseList = courseService.getDownloadCourse();
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();
            if(courseList != null){
                adapter = new DownloadCourseAdapter(mContext,courseList);
                courseListView.setAdapter(adapter);
            }
        }
    }

    private void gotoCourse(CourseVo course) {
        int courseId = course.getId();
        int uingCourse = sp.getInt(UserCourseingSp.C_ING_ID+"_L_"+courseId,0);
        Intent intent = null;
        if(uingCourse > 0){
            intent = new Intent(mContext, DownloadStudyChapterResActivity.class);
            String purl = sp.getString(UserCourseingSp.C_ING_P_URL+"_L_"+courseId,"");
            String vurl = sp.getString(UserCourseingSp.C_ING_V_URL+"_L_"+courseId,"");
            String title = sp.getString(UserCourseingSp.C_ING_TITLE+"_L_"+courseId,"");
            int unitId = sp.getInt(UserCourseingSp.C_ING_UNIT_ID+"_L_"+courseId,0);
            intent.putExtra(DownloadStudyChapterResActivity.P_URL,purl);
            intent.putExtra(DownloadStudyChapterResActivity.V_URL,vurl);
            intent.putExtra(DownloadStudyChapterResActivity.TITLE_TEXT, title);
            intent.putExtra(DownloadStudyChapterResActivity.C_RES_ID,courseId);
            intent.putExtra(DownloadStudyChapterResActivity.C_LOAD_FROM,1);
            intent.putExtra(DownloadStudyChapterResActivity.C_RES_NAME,course.getName());
            intent.putExtra(DownloadStudyChapterResActivity.C_UNIT_ID,unitId);
        }else{
            intent = new Intent(mContext, DownloadChapterTreeActivity.class);
            intent.putExtra("courseId", course.getId());
            intent.putExtra("courseName", course.getName());
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }
}
