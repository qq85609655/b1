package com.gtafe.data.center.study;

public class Test {
    public static void main(String[] args) {
        //被观察者
        WXAdvanceObservable wxAdvanceObservable = new WXAdvanceObservable();
        //观察者
        WXUser user = new WXUser("peakmain");
        WXUser user1 = new WXUser("喜洋洋");
        //注册
        wxAdvanceObservable.register(user);
        wxAdvanceObservable.register(user1);
        //设置文章
        wxAdvanceObservable.setArticle("《观察者设计模式-观察数据的插入》");
        //解注册
        wxAdvanceObservable.unregister(user);
        wxAdvanceObservable.setArticle("《rxjava的设计模式》");
    }
}
