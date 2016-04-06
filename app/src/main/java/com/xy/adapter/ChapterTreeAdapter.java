package com.xy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.fragment.CourseStudyChapterFragment;
import com.xy.view.FProgrssDialog;
import com.xy.view.SlidingMenu;
import com.xy.vo.ChapterTreeVO;
import com.xy.vo.TreeElementBean;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;
import com.xy.wk.CourseQuestionChapterActivity;
import com.xy.wk.CourseStudyChapterResActivity;
import com.xy.wk.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2015/9/11.
 */
public class ChapterTreeAdapter extends ArrayAdapter {
    //init node id
    private static int nodeId = 1;
    //the root element list
    private ArrayList<TreeElementBean> rootEleList = new ArrayList<TreeElementBean>();
    //layout object
    private LayoutInflater mInflater;
    //Bitmap object(collapse)
    private Bitmap collapse;
    //Bitmap object(expand)
    private Bitmap expand;
    private Context context;
    private List<TreeElementBean> chapterList = null;
    private int courseId;

    public ChapterTreeAdapter(Context context, int textViewResourceId,
                              List<TreeElementBean> list,int courseId){
        super(context, textViewResourceId, list);

        this.context = context;
        this.courseId = courseId;

        for(TreeElementBean element : list){
            int level = element.getLevel();
            if(level==0){
                rootEleList.add(element);
            }
        }
        mInflater = LayoutInflater.from(context);
        collapse = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.arrows_plus_16px);
        expand = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.arrows_square_minus_16px);
    }

    public int getCount() {
        return rootEleList.size();
    }

    public Object getItem(int position) {
        return rootEleList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = mInflater.inflate(R.layout.course_study_chapter_left_item, null);
        holder = new ViewHolder();
        holder.text = (TextView) convertView.findViewById(R.id.text);
        holder.icon = (ImageView) convertView.findViewById(R.id.icon);
        convertView.setTag(holder);
        int level = rootEleList.get(position).getLevel();
        holder.icon.setPadding(25 * (level + 1), holder.icon
                .getPaddingTop(), 0, holder.icon.getPaddingBottom());

        holder.text.setText(rootEleList.get(position).getNodeName());
        if (rootEleList.get(position).isHasChild()
                && (rootEleList.get(position).isExpanded() == false)) {
            holder.icon.setImageBitmap(collapse);
        } else if (rootEleList.get(position).isHasChild()
                && (rootEleList.get(position).isExpanded() == true)) {
            holder.icon.setImageBitmap(expand);
        } else if (!rootEleList.get(position).isHasChild()){
            holder.icon.setImageBitmap(collapse);
            holder.icon.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }




    private static void createTree(TreeElementBean parNode, File parentFile,ArrayList<TreeElementBean> nodeList) {
        if (parentFile.isDirectory()) {
            parNode.setHasChild(false);
            if (parentFile.list().length > 0) {
                parNode.setHasChild(true);
            }
            for (File subFile : parentFile.listFiles()) {
                nodeId++;
                //id,nodeName,hasParent,hasChild,upNodeId,level,expanded
                TreeElementBean subNode = new TreeElementBean(format(nodeId),
                        subFile.getName(), true, subFile.isDirectory(), parNode.getId(),
                        parNode.getLevel() + 1, false);
                nodeList.add(subNode);
                createTree(subNode, subFile,nodeList);
            }
        }
    }

    public static void getTreeNodes(ArrayList<TreeElementBean> nodeList,String rootPath) {
        nodeId = 1;
        if (nodeList.size() == 0) {
            File rootDir = new File(rootPath);
            if (rootDir.isDirectory()) {
                //id,nodeName,hasParent,hasChild,upNodeId,level,expanded
                TreeElementBean rootNode = new TreeElementBean(format(1),rootDir.getName(),false,true,format(0),0,false);
                nodeList.add(rootNode);
                createTree(rootNode, rootDir, nodeList);
            }
        }

    }

    protected static String format (int str){
        return String.format("%1$,02d", str);
    }


    public void onClick(int position,ArrayList<TreeElementBean> subNodeList,ChapterTreeAdapter treeViewAdapter,boolean isQuestionChapter){
        TreeElementBean element=rootEleList.get(position);
        if (!element.isHasChild()) {
            int chapterId = Integer.parseInt(element.getUpNodeId());
            int unitId = Integer.parseInt(element.getId());
            String chapterName = element.getUpNodeName();
            String name = element.getNodeName();
            String purl = element.getNodeUrl();
            String vurl = element.getNodeVurl();
            Intent intent = null;
            if(isQuestionChapter){
                intent = new Intent(context, CourseQuestionChapterActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("chapterUnitId",unitId);
                intent.putExtra("queClass",AppConstants.QUESTION_CLASS_ZJLX);
                intent.putExtra("title",context.getString(R.string.course_chapter_pt));
            }else{
                intent = new Intent(context, CourseStudyChapterResActivity.class);
                intent.putExtra(CourseStudyChapterResActivity.P_URL,purl);
                intent.putExtra(CourseStudyChapterResActivity.V_URL, vurl);
                intent.putExtra(CourseStudyChapterResActivity.TITLE_TEXT,name);
                intent.putExtra(CourseStudyChapterResActivity.C_RES_ID,courseId);
                intent.putExtra(CourseStudyChapterResActivity.C_UNIT_ID,unitId);
            }
            context.startActivity(intent);
        }

        if (rootEleList.get(position).isExpanded()) {
            rootEleList.get(position).setExpanded(false);

            ArrayList<TreeElementBean> temp=new ArrayList<TreeElementBean>();

            for (int i = position+1; i < rootEleList.size(); i++) {
                if (element.getLevel()>=rootEleList.get(i).getLevel()) {
                    break;
                }
                temp.add(rootEleList.get(i));
            }

            rootEleList.removeAll(temp);

            treeViewAdapter.notifyDataSetChanged();

        } else {
            loadTree(element,treeViewAdapter,position);
        }
    }

    public void onClick(int position,ArrayList<TreeElementBean> subNodeList,ChapterTreeAdapter treeViewAdapter
            ,FragmentTransaction ft,int resContent,SlidingMenu mMenu,TextView titleText){
        TreeElementBean element=rootEleList.get(position);
        if (!element.isHasChild()) {
            int chapterId = Integer.parseInt(element.getUpNodeId());
            int unitId = Integer.parseInt(element.getId());
            String chapterName = element.getUpNodeName();
            String name = element.getNodeName();
            String purl = element.getNodeUrl();
            String vurl = element.getNodeVurl();


            Fragment fg = CourseStudyChapterFragment.newInstance(purl, vurl);
            ft.replace(resContent, fg, "CourseStudyChapterFragment");
            ft.commit();


            mMenu.hide();

        }

        if (rootEleList.get(position).isExpanded()) {
            rootEleList.get(position).setExpanded(false);

            ArrayList<TreeElementBean> temp=new ArrayList<TreeElementBean>();

            for (int i = position+1; i < rootEleList.size(); i++) {
                if (element.getLevel()>=rootEleList.get(i).getLevel()) {
                    break;
                }
                temp.add(rootEleList.get(i));
            }

            rootEleList.removeAll(temp);

            treeViewAdapter.notifyDataSetChanged();

        } else {
			/*rootEleList.get(position).setExpanded(true);
			for (TreeElementBean elet : subNodeList) {
				int j=1;
				if (elet.getUpNodeId()==rootEleList.get(position).getId()) {
					elet.setExpanded(false);
					rootEleList.add(position+j, elet);
					j++;
				}
			}*/

            loadTree(element,treeViewAdapter,position);
        }
    }

    private void loadTree(final TreeElementBean element,final ChapterTreeAdapter treeViewAdapter,final int position){
        final int plevel = element.getLevel();
        final int pid = Integer.parseInt(element.getId());
        final String name = element.getNodeName();
        final int isAddRes = element.getIsAddRes();
        if(isAddRes == 1)
            return;
        final FProgrssDialog pDialog = new FProgrssDialog(context);
        pDialog.show();
        String tag_json_obj = "json_obj_req";
        final String baseUrl = AppConstants.API_GET_COURSE_TREE;
        final String url = baseUrl + "?courseId="+courseId+"&plevel="+plevel+"&pid="+pid;
        FastJsonRequest<ChapterTreeVO> fastRequest = new FastJsonRequest<ChapterTreeVO>(Request.Method.GET,url, ChapterTreeVO.class,null, new Response.Listener<ChapterTreeVO>() {

            @Override
            public void onResponse(ChapterTreeVO chapterVO) {
                pDialog.dismiss();
                if(chapterVO != null){
                    List<TreeElementBean> nodeList = chapterVO.getChapterList();
                    int n = 1;
                    for (TreeElementBean elet : nodeList) {
                        //elet.setExpanded(elet.isExpanded());
                        elet.setUpNodeName(name);
                        rootEleList.add(position+n, elet);
                        n++;
                    }
                }
                element.setExpanded(true);
                treeViewAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ToastUtil.showToast(context, R.string.get_data_server_exception);
                        pDialog.dismiss();
                    }
                }
        );

        FRestClient.getInstance(context).addToRequestQueue(fastRequest, tag_json_obj);
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
