package com.xy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xy.vo.PaperVO;
import com.xy.wk.CourseQuestionChapterActivity;
import com.xy.wk.R;

import java.util.List;

import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/8/2.
 */
public class CoursePaperAdapter extends BaseAdapter {
    private Context mContext;
    private List<PaperVO> paperList;

    public CoursePaperAdapter(Context mContext,List<PaperVO> paperList){
        this.mContext = mContext;
        this.paperList = paperList;
    }


    @Override
    public int getCount() {
        int count = 0;
        if (paperList != null)
            count = paperList.size();
        return count;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PaperHolder paperHolder;
        final PaperVO paper = paperList.get(i);
        if(view == null){
            paperHolder = new PaperHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.activity_course_paper_item, null);
            paperHolder.paperName = (TextView)view.findViewById(R.id.paperName);
            paperHolder.paper_rl = (RelativeLayout)view.findViewById(R.id.paper_rl);
            view.setTag(paperHolder);
        }else{
            paperHolder = (PaperHolder)view.getTag();
        }
        paperHolder.paperName.setText(paper.getPaperName());
        paperHolder.paper_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseQuestionChapterActivity.class);
                intent.putExtra("courseId", paper.getCourseId());
                intent.putExtra("queClass", paper.getPaperType());
                intent.putExtra("title", paper.getPaperName());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class PaperHolder{
        TextView paperName;
        RelativeLayout paper_rl;
    }
}
