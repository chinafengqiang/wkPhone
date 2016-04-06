package com.xy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xy.vo.CourseList;
import com.xy.vo.CourseVo;
import com.xy.volley.FRestClient;
import com.xy.wk.R;

import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.StringUtils;

/**
 * Created by sara on 15-9-3.
 */
public class CoursrAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourseVo> courseList;
    private ImageLoader imageLoader;
    public CoursrAdapter(Context mContext,List<CourseVo> courseList){
        this.mContext = mContext;
        this.courseList = courseList;
        imageLoader =  FRestClient.getInstance(mContext).getImageLoader();
    }

    public void updateListView(List<CourseVo> courseList){
        this.courseList = courseList;
        notifyDataSetChanged();
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
        if(view == null) {
            courseHolder = new CourseHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.fragment_course_list_item, null);
            courseHolder.image = (ImageView)view.findViewById(R.id.image);
            courseHolder.title = (TextView)view.findViewById(R.id.title);
            courseHolder.course_like_count = (TextView)view.findViewById(R.id.course_like_count);
            courseHolder.course_price = (TextView)view.findViewById(R.id.course_price);
            view.setTag(courseHolder);
        }else{
            courseHolder = (CourseHolder)view.getTag();
        }

        courseHolder.image.setTag(course.getId());
        courseHolder.image.setImageResource(R.mipmap.classresources);

        courseHolder.title.setText(course.getName());
        String icon = course.getCsIcon();
        if(StringUtils.isNotBlank(icon)){
            String fullPath = AppConstants.SERVER_HOST+icon;
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(courseHolder.image,
                    R.mipmap.classresources,R.mipmap.classresources);
            imageLoader.get(fullPath,imageListener,144,93);
        }
        int isFee = course.getCsIsFee();
        int price = course.getCsPrice();
        if(isFee == 1){
            courseHolder.course_price.setText(mContext.getString(R.string.course_price_type)+price);
        }else {
            courseHolder.course_price.setText(R.string.course_is_free);
        }
        return view;
    }

    class CourseHolder{
        ImageView image;
        TextView title;
        TextView course_like_count;
        TextView course_price;
    }
}
