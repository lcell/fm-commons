package com.github.saleson.fm.performance.analysis;

/**
 * @author saleson
 * @date 2019-11-13 13:38
 */
public class DurationCountor {

    private int value;
    private int count;


    public DurationCountor(int value) {
        this.value = value;
    }


    public DurationCountor incCount() {
        count++;
        return this;
    }

    public DurationCountor incCount(int count) {
        this.count += count;
        return this;
    }


    public DurationCountor setCount(int count) {
        this.count = count;
        return this;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DurationCountor) {
            DurationCountor _o = (DurationCountor) obj;

            if (getValue() != _o.getValue()) {
                return false;
            }

            return true;
        }

        return false;
    }

    public int getCount() {
        return count;
    }

    public int getValue() {
        return value;
    }



    @Override
    public int hashCode() {
        int hash = 0;

        hash = hash * 31 + value;

        return hash;
    }




}
