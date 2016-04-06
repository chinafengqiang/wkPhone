package com.xy.wk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.view.ViewPagerIndicatorView;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.CourseVo;
import com.xy.vo.IntegerVO;
import com.xy.vo.LoginInfo;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xy.com.utils.AppConstants;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by sara on 15-9-5.
 */
public class CourseDetailActivity extends BaseFragmentActivity {
    private ViewPagerIndicatorView viewPagerIndicatorView;
    private CourseVo course;
    private WebView chapterMv;
    private View chapterListView;
    private View profileView;
    private TextView courseProfile;
    private TextView course_name, course_intro_price_now;
    private ImageView course_image;
    private ImageView btn_back, btn_share;
    private LinearLayout enroll_button;
    private ImageLoader imageLoader;
    private SharedPreferences sp;

    @Override
    protected void findViewById() {
        course = (CourseVo) getIntent().getSerializableExtra("course");

        this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);

        final Map<String, View> map = new HashMap<String, View>();
        chapterMv = (WebView) chapterListView.findViewById(R.id.chapter_wv);
        String chapterUrl = course.getCcontentlist();
        if (StringUtils.isNotBlank(chapterUrl)) {
            chapterMv.getSettings().setDefaultTextEncodingName("utf-8");
            chapterMv.loadUrl(AppConstants.SERVER_HOST + chapterUrl);
        }

        courseProfile = (TextView) profileView.findViewById(R.id.course_profile);
        courseProfile.setText(course.getCsDesc());

        map.put(getString(R.string.course_chapter), chapterListView);
        map.put(getString(R.string.course_profile), profileView);
        this.viewPagerIndicatorView.setupLayout(map);

        course_name = (TextView) findViewById(R.id.course_name);
        course_intro_price_now = (TextView) findViewById(R.id.course_intro_price_now);
        course_image = (ImageView) findViewById(R.id.course_image);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_share = (ImageView) findViewById(R.id.btn_share);
        enroll_button = (LinearLayout) findViewById(R.id.enroll_button);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
    }

    @Override
    protected void loadViewLayout() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        setContentView(R.layout.view_course_detail);
        chapterListView = LayoutInflater.from(this).inflate(R.layout.course_chpater_list, null);
        profileView = LayoutInflater.from(this).inflate(R.layout.course_profile, null);
    }

    @Override
    protected void processLogic() {
        String name = course.getName();
        int fee = course.getCsIsFee();
        int price = course.getCsPrice();
        course_name.setText(name);
        if (fee == 0) {
            course_intro_price_now.setText(R.string.course_is_free);
        } else {
            course_intro_price_now.setText("￥" + price);
        }

        imageLoader = FRestClient.getInstance(mContext).getImageLoader();
        String icon = course.getCsIcon();
        if (StringUtils.isNotBlank(icon)) {
            String fullPath = AppConstants.SERVER_HOST + icon;
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(course_image,
                    R.mipmap.classresources_big, R.mipmap.classresources_big);
            imageLoader.get(fullPath, imageListener, 0, 200);
        }
    }

    @Override
    protected void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(mContext, "sorry！尚未开通此功能");
            }
        });

        enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = sp.getInt(LoginInfo.USER_ID, 0);
                if (userId <= 0) {
                    gotoLogin(10);
                    return;
                }
                sychCourseToServer();
            }
        });
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

    private void sychCourseToServer() {
        showProgressDialog();
        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_SAVE_COURSE;
        int userId = sp.getInt(LoginInfo.USER_ID, 0);
        final String url = baseUrl + "?courseId=" + course.getId() + "&userId=" + userId + "&mode=" + course.getCsIsFee();
        FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.GET, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

            @Override
            public void onResponse(IntegerVO resVO) {
                closeProgressDialog();
                if(resVO != null && resVO.getRet() == 200){
                    gotoCourse();
                }else {
                    ToastUtil.showToast(mContext, R.string.study_sych_fail);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private void gotoCourse() {
        Intent intent = new Intent(mContext, CourseStudyActivity.class);
        intent.putExtra("courseId", course.getId());
        intent.putExtra("courseName", course.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    private void gotoLogin(int type) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, 10);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 10:
                sychCourseToServer();
                break;
        }

    }
}
