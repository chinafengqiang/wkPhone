package com.xy.wk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.view.TextURLView;
import com.xy.vo.CodeVO;
import com.xy.vo.LoginInfo;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import xy.com.utils.AppConstants;
import xy.com.utils.CommonUtil;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/6/2.
 */
public class RegisterActivity extends BaseFragmentActivity {
    public static final String verifycodeExtra = "xywk.user.verifycode";
    public static final String regMphoneExtra = "xywk.user.reg.mphone";
    private int deptId = AppConstants.DEPT_ID;
    private ImageView title_come_back;
    private Button button_reg;
    private TextURLView mTextViewURL;
    private EditText mphone;
    private TextView login;
    private String phoneNum;
    @Override
    protected void findViewById() {
        mTextViewURL = (TextURLView)findViewById(R.id.xy_url);
        mContext = this;
        title_come_back = (ImageView) findViewById(R.id.title_come_back);
        button_reg = (Button) findViewById(R.id.button_reg);
        mphone = (EditText) findViewById(R.id.mphone);
        login = (TextView) findViewById(R.id.login);
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

        initTvUrl();
    }

    @Override
    protected void initSp() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNum = mphone.getText().toString().trim();
                boolean isValid = CommonUtil.isValidMobiNumber(phoneNum);
                if (!isValid) {
                    ToastUtil.showToast(mContext,R.string.mphone_error);
                    return;
                }
                sendCode();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    private void sendCode(){
        showProgressDialog();
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_SEND_CODE + "?username=" + phoneNum;

        FastJsonRequest<CodeVO> fastRequest = new FastJsonRequest<CodeVO>(Request.Method.GET, url, CodeVO.class, null, new Response.Listener<CodeVO>() {

            @Override
            public void onResponse(CodeVO resVO) {
                closeProgressDialog();
                if (resVO != null) {
                    int ret = resVO.getRet();
                    if(ret == 200){
                        String code = resVO.getCode();
                        Intent intent = new Intent(mContext,RegisterVerifyActivity.class);
                        intent.putExtra(verifycodeExtra,code);
                        intent.putExtra(regMphoneExtra,phoneNum);
                        startActivity(intent);
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

    @Override
    public void onClick(View view) {

    }

    private void initTvUrl() {
        mTextViewURL.setText(R.string.xieyi_url);
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
