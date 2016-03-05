package com.tangovideos.models;

public class ValueWithCount<T> {
    public ValueWithCount(T value, long count) {
        this.value = value;
        this.count = count;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    T value;
    long count;

}
