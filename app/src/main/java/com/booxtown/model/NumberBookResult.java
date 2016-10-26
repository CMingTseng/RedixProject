package com.booxtown.model;

import java.util.List;

/**
 * Created by Administrator on 16/10/2016.
 */
public class NumberBookResult {
    int code;
    private List<NumberBook> result;

    public NumberBookResult(int code, List<NumberBook> result) {
        this.code = code;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<NumberBook> getResult() {
        return result;
    }

    public void setResult(List<NumberBook> result) {
        this.result = result;
    }
}
