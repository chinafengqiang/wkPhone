package com.xy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.vo.CategoryVO;
import com.xy.wk.CourseListActivity;
import com.xy.wk.R;
import java.util.List;


/**
 * Created by sara on 15-8-30.
 */
public class ChildCategoryAdapter extends BaseAdapter{
    private Context mContext;
    private List<CategoryVO> categoryList;
    public ChildCategoryAdapter(Context mContext,List<CategoryVO> categoryList) {
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
            view = inflater.inflate(R.layout.item_categorys, null);
            holder.category_name = (TextView)view.findViewById(R.id.category_name);
            view.setTag(holder);
            view.setPadding(15, 15, 15, 15);
        }else {
            holder = (CategoryHolder)view.getTag();
        }
        holder.category_name.setText(category.getName());
        holder.category_name.setOnClickListener(new View.OnClickListener() {
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
        ImageView category_img;
        TextView category_name;
    }
}
