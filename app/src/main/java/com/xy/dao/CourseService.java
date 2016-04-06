package com.xy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xy.db.DB;
import com.xy.db.DBHelper;
import com.xy.vo.ChapterUnitVO;
import com.xy.vo.ChapterVO;
import com.xy.vo.CourseInfoVO;
import com.xy.vo.CourseVo;
import com.xy.vo.TreeElementBean;

import java.util.ArrayList;
import java.util.List;

import xy.com.utils.AppConstants;
import xy.com.utils.FileUtils;
import xy.com.utils.StringUtils;

/**
 * Created by FQ.CHINA on 2016/1/12.
 */
public class CourseService {
    private DBHelper helper = null;

    public CourseService(Context context){
        helper = new DBHelper(context);
    }

    public boolean saveCourse(CourseInfoVO courseInfo){
        try{
            int courseId = courseInfo.getCourseId();

            /*String condition = DB.TABLES.COURSE.FIELDS.COURSE_ID+" = "+courseId;
            String sql = String.format(DB.TABLES.COURSE.SQL.SELECT, condition);
            Cursor cursor = helper.SELECT(sql);
            if(cursor.moveToNext()){
                int cId = cursor.getInt(cursor.getColumnIndex(DB.TABLES.COURSE.FIELDS.COURSE_ID));
                if(cId > 0){
                    return true;
                }
            }*/

            String condition = DB.TABLES.COURSE.FIELDS.COURSE_ID+" = "+courseId;
            String sql = String.format(DB.TABLES.COURSE.SQL.DELETE, condition);
            String sql_unit = String.format(DB.TABLES.CHAPTER.SQL.DELETE, condition);
            helper.ExecuteSQL(sql);
            helper.ExecuteSQL(sql_unit);

            ContentValues course = new ContentValues();
            course.put(DB.TABLES.COURSE.FIELDS.COURSE_ID,courseInfo.getCourseId());
            course.put(DB.TABLES.COURSE.FIELDS.COURSE_NAME,courseInfo.getCourseName());
            String courseIcon = courseInfo.getCourseIcon();
            if(StringUtils.isNotBlank(courseIcon)){
                courseIcon = courseIcon.replace(AppConstants.COURSE_ICON_PRE,"");
                course.put(DB.TABLES.COURSE.FIELDS.COURSE_ICON,courseIcon);
            }
            helper.insert(DB.TABLES.COURSE.TABLENAME,course);

            List<ChapterVO> chapterList = courseInfo.getChapterList();
            if(chapterList != null && chapterList.size() > 0){
                ContentValues chapter = null;
                List<ContentValues> chapterCList = new ArrayList<ContentValues>();
                for(ChapterVO chapterVO : chapterList){
                    chapter = new ContentValues();
                    chapter.put(DB.TABLES.CHAPTER.FIELDS.CHAPTER_ID,chapterVO.getChapterId());
                    chapter.put(DB.TABLES.CHAPTER.FIELDS.CHAPTER_NAME,chapterVO.getChapterName());
                    chapter.put(DB.TABLES.CHAPTER.FIELDS.PID,chapterVO.getChapterPid());
                    chapter.put(DB.TABLES.CHAPTER.FIELDS.ISLEAF,0);
                    chapter.put(DB.TABLES.CHAPTER.FIELDS.COURSE_ID,courseId);
                    chapterCList.add(chapter);
                }

                helper.insertBacth(DB.TABLES.CHAPTER.TABLENAME,chapterCList);
            }

            List<ChapterUnitVO> chapterUnitVOList = courseInfo.getChapterUnitList();
            if(chapterUnitVOList != null && chapterUnitVOList.size() > 0) {
                ContentValues chapterUnit = null;
                List<ContentValues> chapterUnitList = new ArrayList<ContentValues>();
                for (ChapterUnitVO chapterUnitVO : chapterUnitVOList) {
                    chapterUnit = new ContentValues();
                    chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.CHAPTER_ID,chapterUnitVO.getChapterUnitId());
                    chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.CHAPTER_NAME,chapterUnitVO.getChapterUnitName());
                    chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.PID, chapterUnitVO.getChapterUnitPid());
                    chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.ISLEAF, 1);
                    chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.COURSE_ID, courseId);
                    String videoUrl = chapterUnitVO.getChapterUnitVideoUrl();
                    if(StringUtils.isNotBlank(videoUrl)){
                        String videoName = FileUtils.getFileName(videoUrl);
                        chapterUnit.put(DB.TABLES.CHAPTER.FIELDS.VIDEO_URL, videoName);
                    }
                    chapterUnitList.add(chapterUnit);
                }
                helper.insertBacth(DB.TABLES.CHAPTER.TABLENAME,chapterUnitList);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public List<CourseVo> getDownloadCourse(){
        List<CourseVo> resList = new ArrayList<CourseVo>();
        String sql = DB.TABLES.COURSE.SQL.SELECT_ALL;
        try{
            Cursor cursor = helper.SELECT(sql);
            CourseVo course = null;
            while (cursor.moveToNext()) {
                course = new CourseVo();
                course.setId(cursor.getInt(cursor.getColumnIndex(DB.TABLES.COURSE.FIELDS.COURSE_ID)));
                course.setName(cursor.getString(cursor.getColumnIndex(DB.TABLES.COURSE.FIELDS.COURSE_NAME)));
                course.setCsIcon(cursor.getString(cursor.getColumnIndex(DB.TABLES.COURSE.FIELDS.COURSE_ICON)));
                resList.add(course);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resList;
    }

    public List<TreeElementBean> getDownloadChapter(int courseId,int pid,int plevel){
        String condition = DB.TABLES.CHAPTER.FIELDS.COURSE_ID+" = "+courseId+" and "+DB.TABLES.CHAPTER.FIELDS.PID+" = "+pid;
        String sql = String.format(DB.TABLES.CHAPTER.SQL.SELECT, condition);
        List<TreeElementBean> resList = new ArrayList<TreeElementBean>();
        boolean hasParent = true;
        if(pid == 0)
            hasParent = false;
        try{
            Cursor cursor = helper.SELECT(sql);
            TreeElementBean tree = null;
            while (cursor.moveToNext()) {
                tree = new TreeElementBean();
                int id = cursor.getInt(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_ID));
                String name = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_NAME));
                int ppid = cursor.getInt(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.PID));
                int isLeaf = cursor.getInt(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.ISLEAF));
                String vurl = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.VIDEO_URL));
                tree.setId(id + "");
                tree.setNodeName(name);
                tree.setHasParent(hasParent);
                tree.setUpNodeId(pid + "");
                tree.setLevel(plevel + 1);
                if(isLeaf == 1){
                    tree.setExpanded(true);
                    tree.setHasChild(false);
                    tree.setIsAddRes(1);
                    String url = AppConstants.COURSE_DOWNLOAD_URL+"/"+courseId+"/"+AppConstants.COURSE_DOWNLOAD_HTML_DIR+"/"+id+".html";
                    tree.setNodeUrl(url);
                    if(StringUtils.isNotBlank(vurl)){
                        vurl = AppConstants.COURSE_DOWNLOAD_URL+"/"+courseId+"/"+AppConstants.COURSE_DOWNLOAD_VIDEO_DIR+"/"+vurl;
                        tree.setNodeVurl(vurl);
                    }

                }else {
                    tree.setExpanded(false);
                    tree.setHasChild(true);
                    tree.setIsAddRes(0);
                }
                resList.add(tree);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resList;
    }

    public ChapterUnitVO getOrderUnit(int courseId,int unitId,int pos){
        ChapterUnitVO unitVO = null;
        /*List<ChapterUnitVO> unitList = getOrderUnitList(courseId);
        if(unitList != null){
            int i = 0;
            for(ChapterUnitVO unit : unitList){
                int id = unit.getChapterUnitId();
                if(id == unitId){
                    break;
                }
                i++;
            }
            int size = unitList.size()-1;
            int index = i+pos;
            if(index < 0)
                index = 0;
            if(index > size)
                index = size;
            unitVO = unitList.get(index);
        }*/
        String sql = String.format(DB.TABLES.CHAPTER.SQL.SELECT_ORDER_UNIT, courseId);
        Cursor cursor = helper.SELECT(sql);
        List<ChapterUnitVO> unitList = new ArrayList<ChapterUnitVO>();
        ChapterUnitVO res = null;
        int i = 0;
        boolean isFinded = false;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_ID));
            String name = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_NAME));
            String vurl = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.VIDEO_URL));
            res = new ChapterUnitVO();
            res.setChapterUnitId(id);
            res.setChapterUnitName(name);
            res.setChapterUnitVideoUrl(vurl);
            unitList.add(res);
            if(unitId == id){
                isFinded = true;
            }
            if(!isFinded){
                i++;
            }
        }
        int size = unitList.size()-1;
        int index = i+pos;
        if(index < 0)
            index = 0;
        if(index > size)
            index = size;
        unitVO = unitList.get(index);
        return unitVO;
    }


    private List<ChapterUnitVO> getOrderUnitList(int courseId){
        String sql = String.format(DB.TABLES.CHAPTER.SQL.SELECT_ORDER_UNIT, courseId);
        Cursor cursor = helper.SELECT(sql);
        List<ChapterUnitVO> resList = new ArrayList<ChapterUnitVO>();
        ChapterUnitVO res = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_ID));
            String name = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.CHAPTER_NAME));
            String vurl = cursor.getString(cursor.getColumnIndex(DB.TABLES.CHAPTER.FIELDS.VIDEO_URL));
            res = new ChapterUnitVO();
            res.setChapterUnitId(id);
            res.setChapterUnitName(name);
            res.setChapterUnitVideoUrl(vurl);
            resList.add(res);
        }
        return resList;
    }


}
