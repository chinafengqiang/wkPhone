package com.xy.wk;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xy.view.TextURLView;

import xy.com.utils.AppConstants;

/**
 * Created by FQ.CHINA on 2016/6/2.
 */
public class RegisterActivity extends BaseFragmentActivity {

    private int deptId = AppConstants.DEPT_ID;
    private ImageView title_come_back;
    private Button button_login;
    private TextURLView mTextViewURL;
    @Override
    protected void findViewById() {
        mTextViewURL = (TextURLView)findViewById(R.id.xy_url);
    }

    @Override
    protected void initTitle() {

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
    protected void setListener()
    {

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
