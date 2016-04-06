package com.xy.wk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.adapter.ChapterTreeAdapter;
import com.xy.view.FProgrssDialog;
import com.xy.view.SlidingMenu;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import java.util.ArrayList;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/9/9.
 */
public class CourseStudyChapterActivity extends BaseFragmentActivity {

    private LinearLayout title_menu;
    private SlidingMenu mMenu;
    private int courseId;
    private String courseName = "";
    private TextView menuTitleText;
    private ListView listView;
    private ChapterTreeAdapter treeViewAdapter = null;
    private TextView title_txt;
    @Override
    protected void findViewById() {
        mMenu = (SlidingMenu)findViewById(R.id.id_menu);
        title_menu = (LinearLayout)findViewById(R.id.title_menu);
        menuTitleText = (TextView)findViewById(R.id.menuTitleText);
        listView = (ListView)findViewById(R.id.treeList);
        title_txt = (TextView)findViewById(R.id.title_txt);
    }

    @Override
    protected void initTitle() {
        menuTitleText.setText(courseName);
    }

    @Override
    protected void initSp() {
        courseId = getIntent().getIntExtra("courseId",0);
        courseName = getIntent().getStringExtra("courseName");
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.course_study_chapter);

    }

    @Override
    protected void processLogic() {
        toggleMenu();
        loadTree();
    }

    @Override
    protected void setListener() {
        title_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    public void toggleMenu()
    {
        mMenu.toggle();
    }

    public void loadTree(){
        //final FProgrssDialog pDialog = new FProgrssDialog(mContext);
        //pDialog.show();
        showProgressDialog();

        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_COURSE_TREE;
        final String url = baseUrl + "?courseId="+courseId;
        FastJsonRequest<ChapterTreeVO>   fastRequest = new FastJsonRequest<ChapterTreeVO>(Request.Method.GET,url, ChapterTreeVO.class,null, new Response.Listener<ChapterTreeVO>() {

            @Override
            public void onResponse(ChapterTreeVO chapterVO) {
                //pDialog.dismiss();
                closeProgressDialog();
                if(chapterVO != null){
                    final ArrayList<TreeElementBean> nodeList = (ArrayList<TreeElementBean>) chapterVO.getChapterList();
                    treeViewAdapter = new ChapterTreeAdapter(mContext, R.layout.course_study_chapter_left_item,nodeList,courseId);
                    listView.setAdapter(treeViewAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?> adapter, View view, int position,long id) {
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            treeViewAdapter.onClick(position, nodeList, treeViewAdapter,ft,R.id.study_content
                                    ,mMenu,title_txt);
                        }
                    });
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                        //pDialog.dismiss();
                        closeProgressDialog();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest,tag_json_obj);
    }
}
