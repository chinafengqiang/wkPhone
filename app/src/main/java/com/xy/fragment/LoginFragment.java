package com.xy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xy.wk.R;

/**
 * Created by FQ.CHINA on 2015/9/22.
 */
public class LoginFragment  extends Fragment {
    private View mBaseView;
    private Context mContext;
    private int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.activity_login, null);
        findViewById();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void findViewById(){

    }

    private void setListener(){

    }

    private void processLogic(){

    }

    public void setType(int type){
        this.type = type;
    }
}
