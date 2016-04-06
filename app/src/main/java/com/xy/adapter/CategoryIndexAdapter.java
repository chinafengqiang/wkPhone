package com.xy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.xy.view.CategoryGridView;
import com.xy.vo.CategoryVO;
import com.xy.volley.FRestClient;
import com.xy.wk.R;

import java.util.List;

import xy.com.utils.ObjUtils;


/**
 * Created by sara on 15-8-30.
 */
public class CategoryIndexAdapter extends BaseAdapter {
    private Context mContext;
    private List<CategoryVO> categoryList;

    public CategoryIndexAdapter(Context mContext,List<CategoryVO> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
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
            view = inflater.inflate(R.layout.activity_category_list_item, null);
            view.setTag(holder);
        }else {
            holder = (CategoryHolder)view.getTag();
        }
        holder.cate_name = (TextView)view.findViewById(R.id.cate_name);
        holder.categoryGrideView = (CategoryGridView)view.findViewById(R.id.categoryGrideView);
        holder.list_divide_line = view.findViewById(R.id.list_divide_line);
        holder.cate_name.setText(category.getName());
        if(i == categoryList.size()-1){
            holder.list_divide_line.setVisibility(View.GONE);
        }
        List<CategoryVO> childList = category.getChildList();
        if (childList != null && childList.size() > 0){
            ChildCategoryAdapter cAdapter = new ChildCategoryAdapter(mContext,childList);
            holder.categoryGrideView.setAdapter(cAdapter);
        }
        return view;
    }

    class CategoryHolder{
        TextView cate_name;
        CategoryGridView categoryGrideView;
        View list_divide_line;
    }
}
