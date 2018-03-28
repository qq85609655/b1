package com.gtafe.data.center.study2;

public class PullWXAdvanceObservable extends PullWXPublicObservable {

    private String article;


    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
        //更新文章
        update(article);
    }
}
