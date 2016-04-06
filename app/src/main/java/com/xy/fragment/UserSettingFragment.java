package com.xy.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xy.view.TitleBarView;
import com.xy.vo.LoginInfo;
import com.xy.wk.CourseQuestionChapterActivity;
import com.xy.wk.MainActivity;
import com.xy.wk.R;
import com.xy.wk.UserInfoActivity;

import xy.com.utils.AppConstants;
import xy.com.utils.SpUtil;

/**
 * Created by FQ.CHINA on 2015/9/21.
 */
public class UserSettingFragment extends Fragment {
    private Context mContext;
    private View mBaseView;
    private TextView truenameTv,usernameTv;
    private RelativeLayout exit_rl,user_info_rl,my_question_sc,my_question_mistake;
    private SharedPreferences sp;
    private TitleBarView mTitleBarView;
    private String truename,username;
    private int userId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_user_setting, null);
        initData();
        findView();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initData(){
        sp = SpUtil.getSharePerference(mContext);
        truename = sp.getString(LoginInfo.USER_TRUENAME, "");
        username = sp.getString(LoginInfo.USER_NAME,"");
        userId = sp.getInt(LoginInfo.USER_ID,0);
    }


    private void findView(){
        exit_rl = (RelativeLayout)mBaseView.findViewById(R.id.exit_rl);
        mTitleBarView = (TitleBarView)mBaseView.findViewById(R.id.user_settting_title_bar);
        truenameTv = (TextView)mBaseView.findViewById(R.id.truename);
        usernameTv = (TextView)mBaseView.findViewById(R.id.username);
        user_info_rl = (RelativeLayout)mBaseView.findViewById(R.id.user_info_rl);
        my_question_sc = (RelativeLayout)mBaseView.findViewById(R.id.my_question_sc);
        my_question_mistake = (RelativeLayout)mBaseView.findViewById(R.id.my_question_mistake);

    }


    private void initTitle(){
        mTitleBarView.setTitleText(R.string.user_setting_title);
    }

    private void processLogic(){
        truenameTv.setText(truename);
        usernameTv.setText(getString(R.string.user_username_prix)+username);
    }

    private void setListener(){
        exit_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.setting_exit).setMessage(getString(R.string.setting_exit_cfg)).setCancelable(false)
                        .setPositiveButton(R.string.setting_exit_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeLoginInfo();
                                gotoLogout();
                            }
                        }).setNegativeButton(R.string.setting_cance,null).create().show();




            }
        });

        user_info_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        my_question_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseQuestionChapterActivity.class);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_SC);
                intent.putExtra("title", mContext.getString(R.string.my_que_sc));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        my_question_mistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseQuestionChapterActivity.class);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_ERR);
                intent.putExtra("title", mContext.getString(R.string.my_que_err));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    private void removeLoginInfo(){
        SpUtil.removeSharedPerference(sp, LoginInfo.USER_ID);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_NAME);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_PASS);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_TRUENAME);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_MPHONE);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_PHOTO);
    }

    private void gotoLogout(){
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }
}
