package com.xy.wk;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xy.fragment.CategoryFragment;
import com.xy.fragment.CourseFragment;
import android.view.KeyEvent;

import com.xy.fragment.DownloadFragment;
import com.xy.fragment.LoginFragment;
import com.xy.fragment.UserSettingFragment;
import com.xy.view.TitleBarView;
import com.xy.vo.LoginInfo;

import android.view.View.OnClickListener;

import xy.com.utils.FileUtils;
import xy.com.utils.SpUtil;
import xy.com.utils.ToastUtil;

/*

 */
public class MainActivity extends FragmentActivity {
    private View currentButton;
    private SharedPreferences sp;
    private Context mContext;
    private ImageButton buttom_course,buttom_study,buttom_user_setting,buttom_download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initSp();
        initView();
        initTitile();
        setListener();
    }



    private void initSp(){
        sp = SpUtil.getSharePerference(mContext);
    }

    private void initView(){
        buttom_course = (ImageButton)findViewById(R.id.buttom_course);
        buttom_study = (ImageButton)findViewById(R.id.buttom_study);
        buttom_user_setting = (ImageButton)findViewById(R.id.buttom_user_setting);
        buttom_download = (ImageButton)findViewById(R.id.buttom_download);
    }

    private void initTitile(){

    }

    private void setListener(){
        buttom_course.setOnClickListener(courseListener);
        buttom_course.performClick();
        buttom_study.setOnClickListener(studyListener);
        buttom_user_setting.setOnClickListener(userListener);
        buttom_download.setOnClickListener(downloadListener);
    }

    private OnClickListener courseListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //CourseFragment courseFragment = new CourseFragment();
            CategoryFragment categoryFragment = new CategoryFragment();
            ft.replace(R.id.fl_content,categoryFragment,"CategoryFragment");
            ft.addToBackStack(null);
            ft.commit();
            setButton(view);
        }
    };

    private OnClickListener studyListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int userId = sp.getInt(LoginInfo.USER_ID, 0);
            if(userId <= 0){
                gotoLogin(1);
                return;
            }
            setButton(view);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            CourseFragment courseFragment = new CourseFragment();
            ft.replace(R.id.fl_content, courseFragment, "CourseFragment");
            ft.addToBackStack(null);
            //ft.commit();
            ft.commitAllowingStateLoss();
        }
    };

    private OnClickListener userListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            int userId = sp.getInt(LoginInfo.USER_ID, 0);
            if(userId <= 0){
                gotoLogin(2);
                return;
            }
            setButton(view);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            UserSettingFragment userSettingFragment = new UserSettingFragment();
            ft.replace(R.id.fl_content, userSettingFragment, "UserSettingFragment");
            ft.addToBackStack(null);
            //ft.commit();
            ft.commitAllowingStateLoss();
        }
    };

    private OnClickListener downloadListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            setButton(view);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            DownloadFragment downloadFragment = new DownloadFragment();
            ft.replace(R.id.fl_content, downloadFragment, "DownloadFragment");
            ft.addToBackStack(null);
            //ft.commit();
            ft.commitAllowingStateLoss();
        }
    };

    private void setButton(View v){
        if(currentButton!=null&&currentButton.getId()!=v.getId()){
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton=v;
    }

    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(this,R.string.exist_app_ts);
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return true;
    }

    private void gotoLogin(int type){
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setType(type);//设置点击按钮类型
        ft.replace(R.id.fl_content,loginFragment,"LoginFragment");
        //ft.addToBackStack(null);
        ft.commit();*/
        Intent intent = new Intent(mContext,LoginActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent,0);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 1:
                buttom_study.performClick();
                break;
            case 2:
                buttom_user_setting.performClick();
                break;

        }
    }
}
