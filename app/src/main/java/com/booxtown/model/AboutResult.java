package com.booxtown.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/01/2017.
 */

public class AboutResult {
    private int code;
    private ArrayList<About> infor;

    public AboutResult(int code, ArrayList<About> infor) {
        this.code = code;
        this.infor = infor;
    }

    public AboutResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<About> getInfor() {
        return infor;
    }

    public void setInfor(ArrayList<About> infor) {
        this.infor = infor;
    }
}
