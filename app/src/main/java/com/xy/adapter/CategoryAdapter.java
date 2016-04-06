package com.xy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.xy.vo.CategoryVO;
import com.xy.wk.CourseListActivity;
import com.xy.wk.R;
import com.xy.volley.FRestClient;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.StringUtils;

/**
 * Created by FQ.CHINA on 2015/8/26.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<CategoryVO> categoryList;
    private ImageLoader imageLoader;
    public CategoryAdapter(Context mContext,List<CategoryVO> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        imageLoader =  FRestClient.getInstance(mContext).getImageLoader();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (categoryList != null)
            count = categoryList.size();
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
        CategoryHolder holder;
        final CategoryVO category = categoryList.get(i);
        if(view == null){
            holder = new CategoryHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.activity_course_index_item, null);
            holder.course_category_img = (ImageView)view.findViewById(R.id.course_category_img);
            holder.course_category_name = (TextView)view.findViewById(R.id.course_category_name);
            view.setTag(holder);
            view.setPadding(15, 15, 15, 15);
        }else {
            holder = (CategoryHolder)view.getTag();
        }

        holder.course_category_name.setText(category.getName());
        String iconPath = category.getIconUrl();
        if(StringUtils.isNotBlank(iconPath)){
            String fullPath = AppConstants.SERVER_HOST+iconPath;
            ImageListener imageListener = imageLoader.getImageListener(holder.course_category_img,
                    R.mipmap.course_classzc,R.mipmap.course_classzc);
            imageLoader.get(fullPath,imageListener,0,0);
        }
        holder.course_category_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseListActivity.class);
                intent.putExtra("categoryId",category.getId());
                intent.putExtra("categoryName",category.getName());
                intent.putExtra("categorySn",category.getSn());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    class CategoryHolder{
        ImageView course_category_img;
        TextView course_category_name;
    }
}
