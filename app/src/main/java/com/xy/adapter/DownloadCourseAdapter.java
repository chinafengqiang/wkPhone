package com.xy.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.vo.CourseVo;
import com.xy.wk.R;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.BitmapUtils;
import xy.com.utils.FileService;
import xy.com.utils.StringUtils;

/**
 * Created by FQ.CHINA on 2016/1/26.
 */
public class DownloadCourseAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourseVo> courseList;
    private String imageBasePath;
    public DownloadCourseAdapter(Context mContext,List<CourseVo> courseList){
        this.mContext = mContext;
        this.courseList = courseList;
        FileService fileService = new FileService(mContext);
        imageBasePath = fileService.getSdCardDirectory()+"/"+ AppConstants.COURSE_DOWNLOAD_URL+"/";
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
            view = inflater.inflate(R.layout.fragment_download_course_list_item, null);
            courseHolder.image = (ImageView) view.findViewById(R.id.image);
            courseHolder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(courseHolder);
        } else {
            courseHolder = (CourseHolder) view.getTag();
        }
        courseHolder.image.setTag(course.getId());
        courseHolder.title.setText(course.getName());
        String icon = course.getCsIcon();
        courseHolder.image.setImageResource(R.mipmap.classresources);
        if(StringUtils.isNotBlank(icon)){
            String fullPath = imageBasePath + course.getId() + "/"+icon;
            Bitmap bitmap = null;
            try{
                bitmap = BitmapUtils.decodeSampledBitmapFromFile(fullPath, 144, 93);
            }catch (Exception e){

            }
            if(bitmap != null)
                courseHolder.image.setImageBitmap(bitmap);
        }
        return view;
    }

    class CourseHolder{
        ImageView image;
        TextView title;
    }
}
