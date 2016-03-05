package com.tangovideos.models;

public class ResultWithCount<T> {
    public ResultWithCount(T result, long count) {
        this.result = result;
        this.count = count;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    T result;
    long count;

}
