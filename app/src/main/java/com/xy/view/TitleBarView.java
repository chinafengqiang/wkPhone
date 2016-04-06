package com.xy.view;

import com.xy.wk.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBarView extends RelativeLayout {

	private static final String TAG = "TitleBarView";
	private Context mContext;

	private TextView title_txt;
	
	private LinearLayout title_come_back;

	public TitleBarView(Context context){
		super(context);
		mContext=context;
		initView();
	}
	
	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}

	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		title_txt =(TextView) findViewById(R.id.title_txt);
		title_come_back = (LinearLayout)findViewById(R.id.title_come_back);

	}
	
	public void setTitleTextVisibility(int centerVisibility){
		title_txt.setVisibility(centerVisibility);
	}
	
	public void setTitleComeBackVisibility(int visibility){
		title_come_back.setVisibility(visibility);
	}

	
	public void setTitleText(int txtRes){
		title_txt.setText(txtRes);
	}

	public void setTitleText(String text){
		title_txt.setText(text);
	}

	public void setTitleComeBackListener(OnClickListener listener){
		title_come_back.setOnClickListener(listener);
	}


}
