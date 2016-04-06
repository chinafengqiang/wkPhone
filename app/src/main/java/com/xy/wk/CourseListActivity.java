package com.xy.wk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.fragment.CourseListFragment;
import com.xy.view.CourseSpinnerView;
import com.xy.view.TitleBarView;
import com.xy.view.XListView;
import com.xy.vo.CategoryList;
import com.xy.vo.CategoryVO;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/8/31.
 */
public class CourseListActivity extends BaseFragmentActivity{
    private TitleBarView titleBarView;
    private int categoryId;
    private String categoryName;
    private String categorySn;
    private String rootName;
    private CourseSpinnerView mSpinnerView;
    @Override
    protected void findViewById() {
        titleBarView = (TitleBarView)findViewById(R.id.title);
        mSpinnerView = (CourseSpinnerView)findViewById(R.id.spinner);
        mSpinnerView.setSpinnerTextAppearance(R.style.TextStyle_14_4);
    }

    @Override
    protected void initTitle() {
        if(categoryId == 0)
            titleBarView.setTitleText(rootName+getString(R.string.category_all));
        else
            titleBarView.setTitleText(categoryName);
        titleBarView.setTitleComeBackVisibility(View.VISIBLE);
    }

    @Override
    protected void initSp() {
        categoryId = getIntent().getIntExtra("categoryId", 0);
        rootName = getIntent().getStringExtra("categoryName");
        categorySn = getIntent().getStringExtra("categorySn");
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_course_list);
    }

    @Override
    protected void processLogic() {
        initCategory();

        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CourseListFragment fragment = (CourseListFragment)CourseListFragment.newInstance(categoryId,categoryName,categorySn);
        ft.replace(R.id.course_content,fragment,"CourseListFragment");
        ft.commit();*/

    }

    @Override
    protected void setListener() {
        titleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void initCategory(){
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_CATEGORY_BY_SN+"?sn="+categorySn+"&id="+categoryId;

        FastJsonRequest<CategoryList> fastRequest = new FastJsonRequest<CategoryList>(Request.Method.GET, url, CategoryList.class, null, new Response.Listener<CategoryList>() {

            @Override
            public void onResponse(CategoryList resVO) {
                if (resVO != null) {
                    List<CategoryVO> categroyList = resVO.getCategoryList();
                    if(categroyList != null) {
                        CategoryVO cvo = new CategoryVO();
                        cvo.setId(0);
                        cvo.setName(getString(R.string.category_all));
                        cvo.setPid(-1);
                        cvo.setSn(categorySn);
                        categroyList.add(0, cvo);
                        //mSpinnerView.setItemAdapter(categroyList,0);
                        SpinnerCallBackListener spinnerCallBack = new SpinnerCallBackListener();
                        mSpinnerView.setItemAdapter(categroyList,0,spinnerCallBack);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    public interface SpinnerCallBack {
        void callBack();
    }

    class SpinnerCallBackListener implements SpinnerCallBack{
        @Override
        public void callBack() {
            String sn = mSpinnerView.getTextSn();
            String name = mSpinnerView.getText();
            int id = mSpinnerView.getTextInt();
            //ToastUtil.showToast(mContext,sn);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //CourseListFragment fragment = new CourseListFragment();
            //fragment.setCategoryValue(categoryId,categoryName,categorySn);
            categoryId = id;
            categorySn = sn;
            categoryName = name;
            initTitle();
            CourseListFragment fragment = (CourseListFragment)CourseListFragment.newInstance(categoryId,categoryName,categorySn);
            ft.replace(R.id.course_content,fragment,"CourseListFragment");
            ft.commit();
        }
    }



}
