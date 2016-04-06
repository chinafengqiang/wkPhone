package com.xy.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.xy.wk.R;

/**
 * Created by FQ.CHINA on 2015/8/26.
 */
public class FProgrssDialog extends Dialog {

    public FProgrssDialog(Context context, int theme) {
        super(context, theme);
    }

    public FProgrssDialog(Context context) {
        this(context,R.style.Theme_Dialog_Loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }


}
