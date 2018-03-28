package com.gtafe.data.center.study;

public class WXUser implements IWXUser {
    private String name;
    public WXUser(String name){
        this.name=name;
    }
    @Override
    public void push(String article) {
        System.out.println(name + " 收到了一篇文章：" + article);
    }
}
