package com.xy.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.CategoryIndexAdapter;
import com.xy.view.CustomListView;
import com.xy.view.FProgrssDialog;
import com.xy.vo.CategoryList;
import com.xy.vo.CategoryVO;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.CategoryActivity;
import com.xy.wk.R;
import com.xy.view.CustomListView.OnRefreshListener;
import com.xy.adapter.ViewPagerAdapter;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.BitmapUtils;
import xy.com.utils.ToastUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by sara on 15-8-30.
 */
public class CategoryFragment extends Fragment {
    private View mBaseView;
    private Context mContext;
    private CustomListView mCustomListView;
    private View headerView;
    private LinearLayout title_category_menu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.activity_category_list, null);
        headerView = View.inflate(mContext, R.layout.activity_category_image_head, null);//头部内容
        findViewById();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initTitle() {

    }

    private void findViewById() {
        mCustomListView = (CustomListView) mBaseView.findViewById(R.id.categoryList);
        //mCustomListView.addHeaderView(headerView);
        title_category_menu = (LinearLayout)mBaseView.findViewById(R.id.title_category_menu);
    }

    private void processLogic() {
        final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_INDEX_CATEGORY;

        FastJsonRequest<CategoryList> fastRequest = new FastJsonRequest<CategoryList>(Request.Method.GET, url, CategoryList.class, null, new Response.Listener<CategoryList>() {

            @Override
            public void onResponse(CategoryList resVO) {
                pDialog.dismiss();
                if (resVO != null) {
                    List<CategoryVO> categroyList = resVO.getCategoryList();
                    if(categroyList != null) {
                        mCustomListView.addHeaderView(headerView);
                        CategoryIndexAdapter adapter = new CategoryIndexAdapter(mContext, categroyList);
                        mCustomListView.setAdapter(adapter);
                    }else{
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                    }
                }else{
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

    private void setListener() {
        mCustomListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCustomListView.onRefreshComplete();
            }
        });
        mCustomListView.setCanLoadMore(false);

        title_category_menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in ,R.anim.push_right_out);
            }
        });
    }



}
