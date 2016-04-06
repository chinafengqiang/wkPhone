package com.xy.adapter;

import android.widget.BaseAdapter;
import com.android.volley.toolbox.ImageLoader;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xy.vo.CourseVo;
import java.util.List;
import com.xy.volley.FRestClient;
import com.xy.wk.R;

import xy.com.utils.AppConstants;
import xy.com.utils.StringUtils;

/**
 * Created by FQ.CHINA on 2015/9/24.
 */
public class UserCourseAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourseVo> courseList;
    private ImageLoader imageLoader;
    public UserCourseAdapter(Context mContext,List<CourseVo> courseList){
        this.mContext = mContext;
        this.courseList = courseList;
        imageLoader =  FRestClient.getInstance(mContext).getImageLoader();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (courseList != null)
            count = courseList.size();
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
        CourseHolder courseHolder;
        final CourseVo course = courseList.get(i);
        if (view == null) {
            courseHolder = new CourseHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.fragment_user_course_list_item, null);
            courseHolder.image = (ImageView) view.findViewById(R.id.image);
            courseHolder.title = (TextView) view.findViewById(R.id.title);
            courseHolder.course_type = (TextView) view.findViewById(R.id.course_type);
            view.setTag(courseHolder);
        } else {
            courseHolder = (CourseHolder) view.getTag();
        }
        courseHolder.image.setTag(course.getId());
        courseHolder.image.setImageResource(R.mipmap.classresources);
        courseHolder.title.setText(course.getName());
        int isFee = course.getCsIsFee();
        if(isFee == 1){
            courseHolder.course_type.setText(R.string.study_is_fee);
        }else {
            courseHolder.course_type.setText(R.string.study_is_free);
        }
        String icon = course.getCsIcon();
        if(StringUtils.isNotBlank(icon)){
            String fullPath = AppConstants.SERVER_HOST+icon;
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(courseHolder.image,
                    R.mipmap.classresources,R.mipmap.classresources);
            imageLoader.get(fullPath,imageListener,144,93);
        }
        return view;
    }

    class CourseHolder{
        ImageView image;
        TextView title;
        TextView course_type;
    }
}
