package com.gtafe.data.center.study3;

import java.util.Observable;

public class WXAdvanceObservable extends Observable {

    private String article;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
        //更新文章
        setChanged();
        notifyObservers(article);
    }
}
