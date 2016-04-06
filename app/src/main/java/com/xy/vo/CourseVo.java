package com.xy.vo;

import java.io.Serializable;

/**
 * Created by sara on 15-9-3.
 */
public class CourseVo implements Serializable {
    private int id;
    private String name;
    private String csUrl;
    private String csIcon;
    private String csDesc;
    private String ccontentlist;
    private long csSize;
    private int csDownCount;
    private int csIsFee;
    private int csPrice;
    private int csLevel;

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

    public String getCsUrl() {
        return csUrl;
    }

    public void setCsUrl(String csUrl) {
        this.csUrl = csUrl;
    }

    public String getCsIcon() {
        return csIcon;
    }

    public void setCsIcon(String csIcon) {
        this.csIcon = csIcon;
    }

    public String getCsDesc() {
        return csDesc;
    }

    public void setCsDesc(String csDesc) {
        this.csDesc = csDesc;
    }

    public String getCcontentlist() {
        return ccontentlist;
    }

    public void setCcontentlist(String ccontentlist) {
        this.ccontentlist = ccontentlist;
    }

    public long getCsSize() {
        return csSize;
    }

    public void setCsSize(long csSize) {
        this.csSize = csSize;
    }

    public int getCsDownCount() {
        return csDownCount;
    }

    public void setCsDownCount(int csDownCount) {
        this.csDownCount = csDownCount;
    }

    public int getCsIsFee() {
        return csIsFee;
    }

    public void setCsIsFee(int csIsFee) {
        this.csIsFee = csIsFee;
    }

    public int getCsPrice() {
        return csPrice;
    }

    public void setCsPrice(int csPrice) {
        this.csPrice = csPrice;
    }

    public int getCsLevel() {
        return csLevel;
    }

    public void setCsLevel(int csLevel) {
        this.csLevel = csLevel;
    }
}
