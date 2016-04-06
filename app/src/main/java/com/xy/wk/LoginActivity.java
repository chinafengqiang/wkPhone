package com.xy.wk;


import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.xy.fragment.CourseFragment;
import com.xy.fragment.UserSettingFragment;
import com.xy.vo.LoginInfo;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import xy.com.utils.AppConstants;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/9/23.
 */
public class LoginActivity extends BaseFragmentActivity {
    private ImageView title_come_back;
    private EditText username, password;
    private Button button_login;
    private SharedPreferences sp;
    private int type;
    @Override
    protected void findViewById() {
        title_come_back = (ImageView) findViewById(R.id.title_come_back);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.button_login);
    }

    @Override
    protected void initTitle() {
        title_come_back.setOnClickListener(new View.OnClickListener() {
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
        sp = SpUtil.getSharePerference(mContext);
        String loginname = sp.getString(LoginInfo.USER_NAME,"");
        if(StringUtils.isNotBlank(loginname)){
            username.setText(loginname);
        }
        type = getIntent().getIntExtra("type",0);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String uname = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (StringUtils.isBlank(uname)) {
            ToastUtil.showToast(mContext, R.string.login_username_null);
            return;
        }
        if (StringUtils.isBlank(pass)) {
            ToastUtil.showToast(mContext, R.string.login_password_null);
            return;
        }
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_USER_LOGIN + "?username=" + uname + "&password=" + pass;

        FastJsonRequest<LoginInfo> fastRequest = new FastJsonRequest<LoginInfo>(Request.Method.GET, url, LoginInfo.class, null, new Response.Listener<LoginInfo>() {

            @Override
            public void onResponse(LoginInfo resVO) {
                if (resVO != null) {
                    int ret = resVO.getRet();
                    if(ret == 200){
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putInt(LoginInfo.USER_ID,resVO.getId());
                        ed.putString(LoginInfo.USER_NAME, resVO.getUsername());
                        ed.putString(LoginInfo.USER_PASS,resVO.getUserpass());
                        ed.putString(LoginInfo.USER_TRUENAME,resVO.getTruename());
                        ed.putString(LoginInfo.USER_MPHONE,resVO.getMphone());
                        ed.putString(LoginInfo.USER_PHOTO,resVO.getPhoto());
                        ed.commit();

                        setResult(type);
                        finish();
                    }else if(ret == 400){
                        ToastUtil.showToast(mContext,R.string.login_user_notexist);
                    }else if(ret == 401){
                        ToastUtil.showToast(mContext,R.string.login_password_error);
                    }else if(ret == 402){
                        ToastUtil.showToast(mContext,R.string.login_user_novalid);
                    }else if(ret == 403){
                        ToastUtil.showToast(mContext,R.string.login_user_novalid);
                    }else {
                        ToastUtil.showToast(mContext,R.string.login_fail);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext,R.string.login_fail);
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
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
}
