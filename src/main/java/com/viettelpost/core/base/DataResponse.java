package com.viettelpost.core.base;

import java.io.Serializable;

public class DataResponse implements Serializable {
    int total;

    Object data;

    public DataResponse() {
    }

    public DataResponse(int total, Object data) {
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
