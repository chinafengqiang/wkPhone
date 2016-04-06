package com.xy.wk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.xy.view.FProgrssDialog;
/**
 * Created by FQ.CHINA on 2015/8/31.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener{
    protected Context mContext;
    protected FProgrssDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = getApplicationContext();
        mContext = this;
        initView();
    }


    private void initView() {
        loadViewLayout();
        findViewById();
        initSp();
        initTitle();
        setListener();
        processLogic();
    }


    protected void showProgressDialog() {
        closeProgressDialog();
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new FProgrssDialog(mContext);
        }

        this.progressDialog.show();
    }


    protected void closeProgressDialog() {
        if (this.progressDialog != null){
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        mContext = null;
    }

    protected abstract void findViewById();

    protected abstract void initTitle();

    protected abstract void initSp();

    /**
     *  setContentView()
     *
     */
    protected abstract void loadViewLayout();


    protected abstract void processLogic();


    protected abstract void setListener();
}
