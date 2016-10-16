package com.booxtown.model;

/**
 * Created by Administrator on 30/09/2016.
 */
public class TransactionResult {
    int code;
    Transaction transaction;

    public TransactionResult() {
    }

    public TransactionResult(int code, Transaction transaction) {
        this.code = code;
        this.transaction = transaction;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
