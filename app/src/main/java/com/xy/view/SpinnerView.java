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

import com.xy.wk.R;

public class SpinnerView extends LinearLayout implements OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener{
	private Context mContext;
	private RelativeLayout rlSpinner;
	private SpinerPopWindow mSpinerPopWindow; 
	private TextView spinner_value;
	private ImageButton spinnerDropDown;
	private List<String> itemList = new ArrayList<String>();
	public SpinnerView(Context context){
		super(context);
		this.mContext = context;
		initView();
	}
	
	public SpinnerView(Context context, AttributeSet attrs) {
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
	}
	
	private void setItem(int pos){
		if (pos >= 0 && pos <= itemList.size()){
			String value = itemList.get(pos);
			spinner_value.setText(value);
		}
	}
	
	public String getText(){
		CharSequence text = spinner_value.getText();
		if(text != null){
			return text.toString().trim();
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
}
