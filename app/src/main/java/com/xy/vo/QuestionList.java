package com.xy.vo;

import java.util.List;

/**
 * Created by sara on 15-9-17.
 */
public class QuestionList {
    private List<QuestionVO> questionList;
    private int totals;

    public List<QuestionVO> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionVO> questionList) {
        this.questionList = questionList;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }
}
