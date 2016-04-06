package com.xy.vo;

import java.io.Serializable;

/**
 * Created by sara on 15-9-17.
 */
public class QuestionVO implements Serializable{
    private int id;
    private int quetype;
    private int diffcult;
    private int queclass;
    private int quescore;
    private int conttype;
    private String conturl;
    private String queexpl;
    private String vurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuetype() {
        return quetype;
    }

    public void setQuetype(int quetype) {
        this.quetype = quetype;
    }

    public int getDiffcult() {
        return diffcult;
    }

    public void setDiffcult(int diffcult) {
        this.diffcult = diffcult;
    }

    public int getQueclass() {
        return queclass;
    }

    public void setQueclass(int queclass) {
        this.queclass = queclass;
    }

    public int getQuescore() {
        return quescore;
    }

    public void setQuescore(int quescore) {
        this.quescore = quescore;
    }

    public int getConttype() {
        return conttype;
    }

    public void setConttype(int conttype) {
        this.conttype = conttype;
    }

    public String getConturl() {
        return conturl;
    }

    public void setConturl(String conturl) {
        this.conturl = conturl;
    }

    public String getQueexpl() {
        return queexpl;
    }

    public void setQueexpl(String queexpl) {
        this.queexpl = queexpl;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }
}
