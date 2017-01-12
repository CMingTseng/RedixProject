package com.booxtown.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/01/2017.
 */

public class FaqResult {
    private int code;
    private ArrayList<Faq> faq;

    public FaqResult(int code, ArrayList<Faq> faq) {
        this.code = code;
        this.faq = faq;
    }

    public FaqResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<Faq> getFaq() {
        return faq;
    }

    public void setFaq(ArrayList<Faq> faq) {
        this.faq = faq;
    }
}
