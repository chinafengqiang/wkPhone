package com.xy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.CategoryIndexAdapter;
import com.xy.adapter.CoursrAdapter;
import com.xy.view.CustomListView;
import com.xy.view.FProgrssDialog;
import com.xy.view.XListView;
import com.xy.vo.CategoryList;
import com.xy.vo.CategoryVO;
import com.xy.vo.CourseList;
import com.xy.vo.CourseVo;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.CourseDetailActivity;
import com.xy.wk.R;

import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;
import com.xy.view.XListView.IXListViewListener;
/**
 * Created by FQ.CHINA on 2015/9/2.
 */
public class CourseListFragment extends Fragment implements IXListViewListener {
    private View mBaseView;
    private Context mContext;
    //private CustomListView mCustomListView;
    private XListView mXListView;
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_SN = "category_sn";
    private int categoryId;
    private String categoryName = "";
    private String categorySn = "";

    private int totals = 0;
    private int offset = 0;
    List<CourseVo> courseList = new ArrayList<CourseVo>();
    private CoursrAdapter adapter;

    private int deptId = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_course_list, null);
        findViewById();
        initData();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

   public static Fragment newInstance(int categoryId,String categoryName,String categorySn) {
       CourseListFragment fragment = new CourseListFragment();
       Bundle bundle = new Bundle();
       bundle.putInt(CATEGORY_ID, categoryId);
       bundle.putString(CATEGORY_NAME, categoryName);
       bundle.putString(CATEGORY_SN, categorySn);
       fragment.setArguments(bundle);
       return fragment;
   }

    public void setCategoryValue(int categoryId,String categoryName,String categorySn){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categorySn = categorySn;
    }

    private void initTitle() {

    }

    private void initData(){
        Bundle bundle = getArguments();
        if(bundle != null){
            categoryId = bundle.getInt(CATEGORY_ID, 0);
            categoryName = bundle.getString(CATEGORY_NAME);
            categorySn = bundle.getString(CATEGORY_SN);

        }
        deptId = AppConstants.DEPT_ID;
    }

    private void findViewById() {
        //mCustomListView = (CustomListView) mBaseView.findViewById(R.id.courseList);
        mXListView = (XListView) mBaseView.findViewById(R.id.courseList);
        mXListView.setPullLoadEnable(true);//加载更多
        mXListView.setPullRefreshEnable(true);//下拉刷新
        mXListView.setXListViewListener(this);
    }

    private void processLogic() {
        loadData(false);
    }

    private void loadData(final boolean isLoadMore){
        final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_COURSE_BY_SN+"?sn="+categorySn+"&offset="+offset+"&deptId="+deptId;

        FastJsonRequest<CourseList> fastRequest = new FastJsonRequest<CourseList>(Request.Method.GET, url, CourseList.class, null, new Response.Listener<CourseList>() {

            @Override
            public void onResponse(CourseList resVO) {
                pDialog.dismiss();
                if (resVO != null) {
                    List<CourseVo> resList = resVO.getCourseList();
                    totals = resVO.getTotals();
                    offset += resVO.getOffset();
                    if(resList != null) {
                        courseList.addAll(resList);
                        if(isLoadMore){
                            if(adapter != null){
                                adapter.updateListView(courseList);
                            }
                        }else {
                            adapter = new CoursrAdapter(mContext,courseList);
                            mXListView.setAdapter(adapter);
                        }

                    }else{
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                    }
                }else{
                    ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                }
                onLoad();
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_fail);
                        pDialog.dismiss();
                        onLoad();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private void setListener(){
        mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int postion = i - 1;
                if (courseList.size() > 0 && postion < courseList.size() && postion >= 0) {
                    CourseVo course = courseList.get(postion);
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra("course", course);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        offset = 0;
        if(courseList != null)
            courseList.clear();
        loadData(false);
    }

    @Override
    public void onLoadMore() {
        if(offset >= totals){
            ToastUtil.showToast(mContext,"已经全部加载完成");
            onLoad();
            return;
        }else{
            loadData(true);
        }
    }


    private void onLoad() {
        mXListView.stopRefresh();
        mXListView.stopLoadMore();
        mXListView.setRefreshTime(getString(R.string.xlistview_refesh_time));
    }
}


