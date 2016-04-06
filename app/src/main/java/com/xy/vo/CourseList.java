package com.xy.vo;

import java.util.List;

/**
 * Created by sara on 15-9-3.
 */
public class CourseList {
    private int totals;
    private int offset;
    private List<CourseVo> courseList;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public List<CourseVo> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseVo> courseList) {
        this.courseList = courseList;
    }
}
