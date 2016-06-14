package com.xy.wk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.view.TitleBarView;
import com.xy.vo.CodeVO;
import com.xy.vo.IntegerVO;
import com.xy.vo.LoginInfo;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import xy.com.utils.AppConstants;
import xy.com.utils.SmsContent;
import xy.com.utils.SpUtil;
import xy.com.utils.StringUtils;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/6/14.
 */
public class RegisterVerifyActivity extends BaseFragmentActivity{
    private Button verify_get_next,reg_btn;
    private EditText et_verifycode,et_loginpass;
    private TitleBarView mTitleBarView;
    private String verifycode;
    private Context mContext;
    private String mphone,pass;
    private SharedPreferences sp;
    private SmsContent content;
    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView)findViewById(R.id.title_bar);
        verify_get_next = (Button)findViewById(R.id.verify_get_next);
        et_verifycode = (EditText)findViewById(R.id.verifycode);
        et_loginpass = (EditText)findViewById(R.id.loginpass);
        reg_btn = (Button)findViewById(R.id.button_reg);
    }

    @Override
    protected void initTitle() {
        mTitleBarView.setTitleTextVisibility(View.VISIBLE);
        mTitleBarView.setTitleText(R.string.reg_verify_title);
        mTitleBarView.setTitleComeBackVisibility(View.VISIBLE);
        mTitleBarView.setTitleComeBackListener(comeBackOnclickListener);
    }

    private View.OnClickListener comeBackOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent regIntent = new Intent(mContext,RegisterActivity.class);
            startActivity(regIntent);
            finish();
        }
    };

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_register_verify);
        mContext = this;
    }

    @Override
    protected void processLogic() {
        verifycode = getIntent().getStringExtra(RegisterActivity.verifycodeExtra);
        mphone = getIntent().getStringExtra(RegisterActivity.regMphoneExtra);
        MyCount count = new MyCount(60000,1000);
        count.start();

        content = new SmsContent(this,new Handler(),et_verifycode); //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
    }

    @Override
    protected void setListener() {
        verify_get_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_get_next.setText(R.string.get_verify_after_sixth);
                verify_get_next.setEnabled(false);
                MyCount count = new MyCount(60000,1000);
                count.start();

                getVerifycode();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_verifycode.getText().toString().trim();
                pass = et_loginpass.getText().toString().trim();
                if(StringUtils.isBlank(code)){
                    ToastUtil.showToast(mContext,R.string.verify_code_isnull);
                    return;
                }
                if(StringUtils.isBlank(pass)){
                    ToastUtil.showToast(mContext,R.string.user_login_pass_isnull);
                    return;
                }
                if(!code.equals(verifycode)){
                    ToastUtil.showToast(mContext,R.string.verify_code_error);
                    return;
                }
                register();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private void getVerifycode(){
        showProgressDialog();
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_SEND_CODE + "?username=" + mphone;
        FastJsonRequest<CodeVO> fastRequest = new FastJsonRequest<CodeVO>(Request.Method.GET, url, CodeVO.class, null, new Response.Listener<CodeVO>() {

            @Override
            public void onResponse(CodeVO resVO) {
                closeProgressDialog();
                if (resVO != null) {
                    int ret = resVO.getRet();
                    if(ret == 200){
                        verifycode = resVO.getCode();
                    }else if(ret == 400){
                        ToastUtil.showToast(mContext,R.string.user_is_reg);
                    }else {
                        ToastUtil.showToast(mContext,R.string.get_data_fail);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        ToastUtil.showToast(mContext,R.string.get_data_fail);
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private void register(){
        showProgressDialog();
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_USER_REGISTER + "?mphone=" + mphone+"&password="+pass+"&deptId="+AppConstants.DEPT_ID;
        FastJsonRequest<LoginInfo> fastRequest = new FastJsonRequest<LoginInfo>(Request.Method.GET, url, LoginInfo.class, null, new Response.Listener<LoginInfo>() {

            @Override
            public void onResponse(LoginInfo resVO) {
                closeProgressDialog();
                if (resVO != null) {
                    int ret = resVO.getRet();
                    if(ret == 200){
                        ToastUtil.showToast(mContext,R.string.reg_ok);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putInt(LoginInfo.USER_ID,resVO.getId());
                        ed.putString(LoginInfo.USER_NAME, resVO.getUsername());
                        ed.putString(LoginInfo.USER_PASS,resVO.getUserpass());
                        ed.putString(LoginInfo.USER_TRUENAME,resVO.getTruename());
                        ed.putString(LoginInfo.USER_MPHONE,resVO.getMphone());
                        ed.putString(LoginInfo.USER_PHOTO,resVO.getPhoto());
                        ed.commit();

                        Intent intent = new Intent(mContext,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                        finish();
                    }else if(ret == 400){
                        ToastUtil.showToast(mContext,R.string.user_is_reg);
                    }else {
                        ToastUtil.showToast(mContext,R.string.reg_faild);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        ToastUtil.showToast(mContext,R.string.reg_faild);
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            verify_get_next.setText(R.string.get_verify_agin);
            verify_get_next.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            verify_get_next.setText(millisUntilFinished / 1000+""+getString(R.string.get_verify_after_puffix));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(content);
    }
}
