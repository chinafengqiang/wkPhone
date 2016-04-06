package com.xy.vo;

import java.util.List;

/**
 * Created by FQ.CHINA on 2015/8/26.
 */
public class CategoryVO {

    private int id;
    private String name;
    private String iconUrl;
    private int pid;
    private String sn;
    private List<CategoryVO> childList;

    public List<CategoryVO> getChildList() {
        return childList;
    }

    public void setChildList(List<CategoryVO> childList) {
        this.childList = childList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
