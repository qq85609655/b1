package com.gtafe.data.center.study3;

import java.util.Observable;
import java.util.Observer;

public class WXUser implements Observer {
    private String name;
    public WXUser(String name){
        this.name=name;
    }
 /*   @Override
    public void push(String article) {
        System.out.println(name + " 收到了一篇文章：" + article);
    }*/

    @Override
    public void update(Observable observable, Object o) {
        //推送文章
        System.out.println(name + " 收到了一篇推送文章：" + o);
        //拉取文章
        WXAdvanceObservable wxAdvanceObservable= (WXAdvanceObservable) observable;
        System.out.println(name + " 主动拉取了一篇文章：" +wxAdvanceObservable.getArticle());
    }

}
