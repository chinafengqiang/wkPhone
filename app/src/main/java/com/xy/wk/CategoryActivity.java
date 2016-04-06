package com.xy.wk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.CategoryAdapter;
import com.xy.view.FProgrssDialog;
import com.xy.view.TitleBarView;
import com.xy.vo.CategoryList;
import com.xy.vo.CategoryVO;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.R;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;
import java.util.List;

/**
 * Created by FQ.CHINA on 2015/8/31.
 */
public class CategoryActivity extends Activity {
    private Context mContext;
    private GridView gridView;
    private LinearLayout title_category_close;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_course_index);
        findViewById();
        initTitle();
        processLogic();
        setListener();
    }

    private void initTitle(){

    }
    private void findViewById(){
        gridView = (GridView)findViewById(R.id.categoryGrideGrid);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        title_category_close = (LinearLayout)findViewById(R.id.title_category_close);
    }

    private void setListener(){
        title_category_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.push_left_in ,R.anim.push_right_out);
            }
        });
    }

    private void processLogic(){
        final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_ROOT_CATEGORY;

        FastJsonRequest<CategoryList>   fastRequest = new FastJsonRequest<CategoryList>(Request.Method.GET,url, CategoryList.class,null, new Response.Listener<CategoryList>() {

            @Override
            public void onResponse(CategoryList resVO) {
                pDialog.dismiss();
                if(resVO != null){
                    List<CategoryVO> categroyList = resVO.getCategoryList();
                    CategoryAdapter adapter = new CategoryAdapter(mContext,categroyList);
                    gridView.setAdapter(adapter);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext,R.string.get_data_fail);
                        pDialog.dismiss();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }
}
