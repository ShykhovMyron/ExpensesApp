package com.application.entity;

public class Counter {
    private int count;

    public Counter(int init)
    {
        count = init;
    }

    public int get()
    {
        return count;
    }

    public int incrementAndGet()
    {
        return ++count;
    }

    public String toString()
    {
        return ""+count;
    }
}
