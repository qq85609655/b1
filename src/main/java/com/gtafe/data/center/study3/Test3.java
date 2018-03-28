package com.gtafe.data.center.study3;

public class Test3 {
    public static void main(String [] args){
        //被观察者
        WXAdvanceObservable wxAdvanceObservable=new WXAdvanceObservable();
        //观察者
        WXUser user=new WXUser("peakmain");
        WXUser user1=new WXUser("喜洋洋");
        //注册
        wxAdvanceObservable.addObserver(user);
        wxAdvanceObservable.addObserver(user1);
        //设置文章
        wxAdvanceObservable.setArticle("《观察者设计模式-观察数据的插入》");
        //解注册
        wxAdvanceObservable.deleteObserver(user1);
        wxAdvanceObservable.setArticle("《rxjava的设计模式》");
    }
}
