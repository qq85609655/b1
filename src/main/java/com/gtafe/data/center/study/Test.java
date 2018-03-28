package com.gtafe.data.center.study;

public class Test {
    public static void main(String[] args) {
        //被观察者
        WXAdvanceObservable1 wxAdvanceObservable1 = new WXAdvanceObservable1();
        //观察者
        WXUser1 user = new WXUser1("peakmain");
        WXUser1 user1 = new WXUser1("喜洋洋");
        //注册
        wxAdvanceObservable1.register(user);
        wxAdvanceObservable1.register(user1);
        //设置文章
        wxAdvanceObservable1.setArticle("《观察者设计模式-观察数据的插入》");
        //解注册
        wxAdvanceObservable1.unregister(user);
        wxAdvanceObservable1.setArticle("《rxjava的设计模式》");
    }
}
