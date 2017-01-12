package com.booxtown.model;

import java.io.Serializable;

/**
 * Created by Administrator on 12/01/2017.
 */

public class Faq implements Serializable {

    private int id;
    private String question;
    private String answer;
    private int category_id;
    private String category_name;

    public Faq() {
    }

    public Faq(int id, String question, String answer, int category_id, String category_name) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
