package com.application.entity;

public class PurchaseNumberOnPage {

    // рэто не энтити, не тот пакет.азберись что это задрочь, мб есть другой способ обработать эту ситуацию в симлифе, и переименуй этот ебаный класс если нет


    private int count;

    public PurchaseNumberOnPage(int init) {
        count = init;
    }

    public int get() {
        return count;
    }

    public int incrementAndGet() {
        return ++count;
    }

    public String toString() {
        return "" + count;
    }
}