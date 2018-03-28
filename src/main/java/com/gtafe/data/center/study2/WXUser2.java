package com.gtafe.data.center.study2;

public class WXUser2 implements IWXUser2 {
    private String name;
    public WXUser2(String name){
        this.name=name;
    }
    @Override
    public void pull(PullWXPublicObservable observable) {
        PullWXAdvanceObservable pullWXAdvanceObservable= (PullWXAdvanceObservable) observable;
        System.out.println(name+"拉取了一篇文章:"+pullWXAdvanceObservable.getArticle());
    }
}
