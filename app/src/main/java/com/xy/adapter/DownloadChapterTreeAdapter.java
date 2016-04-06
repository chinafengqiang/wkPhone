package com.xy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.dao.CourseService;
import com.xy.view.FProgrssDialog;
import com.xy.vo.TreeElementBean;
import com.xy.wk.DownloadStudyChapterResActivity;
import com.xy.wk.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by FQ.CHINA on 2016/1/27.
 */
public class DownloadChapterTreeAdapter extends ArrayAdapter {
    private static int nodeId = 1;
    private ArrayList<TreeElementBean> rootEleList = new ArrayList<TreeElementBean>();
    private LayoutInflater mInflater;
    private Bitmap collapse;
    private Bitmap expand;
    private Context context;
    private List<TreeElementBean> chapterList = null;
    private int courseId;
    private TreeElementBean currentElement;
    private DownloadChapterTreeAdapter adapter;
    private int position;


    public DownloadChapterTreeAdapter(Context context, int textViewResourceId,
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


    public void onClick(int position,List<TreeElementBean> subNodeList,DownloadChapterTreeAdapter treeViewAdapter){
        TreeElementBean element=rootEleList.get(position);
        if (!element.isHasChild()) {
            int chapterId = Integer.parseInt(element.getUpNodeId());
            int unitId = Integer.parseInt(element.getId());
            String chapterName = element.getUpNodeName();
            String name = element.getNodeName();
            String purl = element.getNodeUrl();
            String vurl = element.getNodeVurl();
            Intent intent = null;

            intent = new Intent(context, DownloadStudyChapterResActivity.class);
            intent.putExtra(DownloadStudyChapterResActivity.P_URL,purl);
            intent.putExtra(DownloadStudyChapterResActivity.V_URL,vurl);
            intent.putExtra(DownloadStudyChapterResActivity.TITLE_TEXT, name);
            intent.putExtra(DownloadStudyChapterResActivity.C_RES_ID,courseId);
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
            this.currentElement = element;
            this.adapter = treeViewAdapter;
            this.position = position;
            new LoadTree().execute(1);
        }
    }


    private class LoadTree extends AsyncTask<Integer,Void,Integer> {
        FProgrssDialog pDialog = null;
        List<TreeElementBean> treeList;
        int plevel = 0;
        int pid = 0;
        String name = "";
        int isAddRes = 0;

        @Override
        protected void onPreExecute() {
            pDialog = new FProgrssDialog(context);
            pDialog.show();
            plevel = currentElement.getLevel();
            pid = Integer.parseInt(currentElement.getId());
            name = currentElement.getNodeName();
            isAddRes = currentElement.getIsAddRes();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            if(isAddRes == 1)
                return 1;
            CourseService courseService = new CourseService(context);
            treeList = courseService.getDownloadChapter(courseId,pid,plevel);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();
            if(treeList != null){
                int n = 1;
                for (TreeElementBean elet : treeList) {
                    //elet.setExpanded(elet.isExpanded());
                    elet.setUpNodeName(name);
                    rootEleList.add(position+n, elet);
                    n++;
                }
            }
            currentElement.setExpanded(true);
            adapter.notifyDataSetChanged();
        }
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
