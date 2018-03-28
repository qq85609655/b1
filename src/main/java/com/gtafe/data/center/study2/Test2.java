package com.gtafe.data.center.study2;

public class Test2 {

    public static void main(String[] args) {
        //被观察者
        PullWXAdvanceObservable wxAdvanceObservable = new PullWXAdvanceObservable();
        //观察者
        WXUser2 user = new WXUser2("peakmain");
        WXUser2 user1 = new WXUser2("喜洋洋");
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
