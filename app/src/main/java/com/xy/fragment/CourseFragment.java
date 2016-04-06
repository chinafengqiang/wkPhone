package com.xy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.CategoryAdapter;
import com.xy.adapter.CoursrAdapter;
import com.xy.adapter.UserCourseAdapter;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.CategoryList;
import com.xy.vo.CategoryVO;
import com.xy.vo.CourseList;
import com.xy.vo.CourseVo;
import com.xy.vo.LoginInfo;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.CourseStudyActivity;
import com.xy.wk.R;

import xy.com.utils.AppConstants;
import xy.com.utils.SpUtil;
import xy.com.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FQ.CHINA on 2015/8/26.
 */

public class CourseFragment extends Fragment {
    private View mBaseView;
    private Context mContext;
    private TitleBarView mTitleBarView;
    private ListView courseListView;
    List<CourseVo> courseList = new ArrayList<CourseVo>();
    private int userId;
    private SharedPreferences sp;
    private UserCourseAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_user_course_list, null);
        initSp();
        findViewById();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initSp(){
        sp = SpUtil.getSharePerference(mContext);
        userId = sp.getInt(LoginInfo.USER_ID, 0);
    }

    private void initTitle() {
        mTitleBarView.setTitleText(R.string.study_title);
    }

    private void findViewById() {
        mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
        courseListView = (ListView) mBaseView.findViewById(R.id.courseList);
    }

    private void setListener() {
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                if (courseList.size() > 0 && postion < courseList.size() && postion >= 0) {
                    CourseVo course = courseList.get(postion);
                    gotoCourse(course);
                }
            }
        });
    }

    private void processLogic() {
        final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_USER_COURSE + "?userId=" + userId;

        FastJsonRequest<CourseList> fastRequest = new FastJsonRequest<CourseList>(Request.Method.GET, url, CourseList.class, null, new Response.Listener<CourseList>() {

            @Override
            public void onResponse(CourseList resVO) {
                pDialog.dismiss();
                if (resVO != null) {
                    List<CourseVo> resList = resVO.getCourseList();
                    if (resList != null) {
                        courseList.addAll(resList);
                        adapter = new UserCourseAdapter(mContext,courseList);
                        courseListView.setAdapter(adapter);
                    } else {
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                    }
                } else {
                    ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_fail);
                        pDialog.dismiss();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private void gotoCourse(CourseVo course) {
        Intent intent = new Intent(mContext, CourseStudyActivity.class);
        intent.putExtra("courseId", course.getId());
        intent.putExtra("courseName", course.getName());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }
}
