package com.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 01/10/2016.
 */
public class DashBoardResult {
    private int code;
    private List<DashBoard> transaction;

    public DashBoardResult(int code, List<DashBoard> transaction) {
        this.code = code;
        this.transaction = transaction;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DashBoard> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<DashBoard> transaction) {
        this.transaction = transaction;
    }
}
