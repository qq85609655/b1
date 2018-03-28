package com.gtafe.data.center.study;

public class WXAdvanceObservable1 extends WXPublicObservable {
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
