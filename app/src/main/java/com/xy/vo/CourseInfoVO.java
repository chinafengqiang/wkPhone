package com.xy.vo;

import java.util.List;

/**
 * Created by FQ.CHINA on 2016/1/12.
 */
public class CourseInfoVO {
    private int courseId;
    private String courseName;
    private int courseCategory;
    private String courseDesc;
    private String courseIcon;
    private List<ChapterVO> chapterList;
    private List<ChapterUnitVO> chapterUnitList;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(int courseCategory) {
        this.courseCategory = courseCategory;
    }

    public String getCourseIcon() {
        return courseIcon;
    }

    public void setCourseIcon(String courseIcon) {
        this.courseIcon = courseIcon;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public List<ChapterVO> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChapterVO> chapterList) {
        this.chapterList = chapterList;
    }

    public List<ChapterUnitVO> getChapterUnitList() {
        return chapterUnitList;
    }

    public void setChapterUnitList(List<ChapterUnitVO> chapterUnitList) {
        this.chapterUnitList = chapterUnitList;
    }
}
