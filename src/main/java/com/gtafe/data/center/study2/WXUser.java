package com.gtafe.data.center.study2;

public class WXUser  implements IWXUser {
    private String name;
    public WXUser(String name){
        this.name=name;
    }
    @Override
    public void pull(PullWXPublicObservable observable) {
        PullWXAdvanceObservable pullWXAdvanceObservable= (PullWXAdvanceObservable) observable;
        System.out.println(name+"拉取了一篇文章:"+pullWXAdvanceObservable.getArticle());
    }
}
