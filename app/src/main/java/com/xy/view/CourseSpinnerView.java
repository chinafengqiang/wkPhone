package com.xy.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.xy.vo.CategoryVO;
import com.xy.wk.CourseListActivity;
import com.xy.wk.R;

public class CourseSpinnerView extends LinearLayout implements OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener{
    private Context mContext;
    private RelativeLayout rlSpinner;
    private SpinerPopWindow mSpinerPopWindow;
    private TextView spinner_value;
    private ImageButton spinnerDropDown;
    private List<String> itemList = new ArrayList<String>();
    private int[] categoryId = null;
    private String[] categorySn = null;
    private int pos = -1;
    private CourseListActivity.SpinnerCallBack callBack;

    public CourseSpinnerView(Context context){
        super(context);
        this.mContext = context;
        initView();
    }

    public CourseSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }

    public void setItemAdapter(int arrId,int pos){
        String[] items = getResources().getStringArray(arrId);
        for(int i = 0; i < items.length; i++){
            itemList.add(items[i]);
        }
        setItem(pos);
    }

    public void setItemAdapter(List<CategoryVO> categoryList,int pos){
        if(categoryList != null && categoryList.size() > 0){
            int size = categoryList.size();
            categoryId = new int[size];
            categorySn = new String[size];
            for (int i=0 ; i < size ; i++){
                CategoryVO ct = categoryList.get(i);
                itemList.add(ct.getName());
                categoryId[i] = ct.getId();
                categorySn[i] = ct.getSn();
            }

            setItem(pos);
        }
    }


    public void setItemAdapter(List<CategoryVO> categoryList,int pos,CourseListActivity.SpinnerCallBack callBack){
        this.callBack = callBack;
        setItemAdapter(categoryList,pos);
    }


    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.common_spinner, this);
        rlSpinner = (RelativeLayout)findViewById(R.id.rl_spinner);
        spinner_value = (TextView)findViewById(R.id.spinner_value);
        spinnerDropDown = (ImageButton)findViewById(R.id.spinner_dropdown);
        rlSpinner.setOnClickListener(this);
        spinnerDropDown.setOnClickListener(this);

        mSpinerPopWindow = new SpinerPopWindow(mContext);
        mSpinerPopWindow.refreshData(itemList, 0);
        mSpinerPopWindow.setItemListener(this);

        this.pos = -1;
    }

    private void setItem(int pos){
        if (itemList.size()>0 && pos >= 0 && pos <= itemList.size()){
            String value = itemList.get(pos);
            spinner_value.setText(value);
            this.pos = pos;
            this.callBack.callBack();
        }
    }

    public String getText(){
        CharSequence text = spinner_value.getText();
        if(text != null){
            return text.toString().trim();
        }
        return "";
    }

    public int getTextInt(){
        if(this.pos >= 0 && categoryId != null && this.pos < categoryId.length){
            return categoryId[this.pos];
        }
        return 0;
    }

    public String getTextSn(){
        if(this.pos >= 0 && categorySn != null && this.pos < categorySn.length){
            return categorySn[this.pos];
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rl_spinner:
                showSpinWindow();
                break;
            case R.id.spinner_dropdown:
                showSpinWindow();
                break;
        }
    }

    @Override
    public void onItemClick(int pos) {
        setItem(pos);
    }

    private void showSpinWindow(){
        mSpinerPopWindow.setWidth(spinner_value.getWidth());
        mSpinerPopWindow.showAsDropDown(spinner_value);
    }

    public void setTextViewBackgroud(int resId){
        spinner_value.setBackgroundResource(resId);
    }

    public void setSpinnerTextAppearance(int styleId){
        spinner_value.setTextAppearance(mContext,styleId);
    }
}

