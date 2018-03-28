package com.gtafe.data.center.study;

public class WXUser1 implements IWXUser1 {
    private String name;
    public WXUser1(String name){
        this.name=name;
    }
    @Override
    public void push(String article) {
        System.out.println(name + " 收到了一篇文章：" + article);
    }
}
