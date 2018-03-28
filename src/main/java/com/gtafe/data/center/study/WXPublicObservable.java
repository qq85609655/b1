package com.gtafe.data.center.study;

import java.util.ArrayList;
import java.util.List;

public class WXPublicObservable {
    private List<IWXUser1> mUsers;
    public  WXPublicObservable(){
        mUsers=new ArrayList<>();
    }
    /**
     * 注册
     */
    public void register(IWXUser1 user){
        mUsers.add(user);
    }
    /**
     * 解注册
     */
    public void unregister(IWXUser1 user){
        mUsers.remove(user);
    }
    /**
     * 更新文章
     */
    public void update(String article){
        for (IWXUser1 user : mUsers) {
            user.push(article);
        }
    }
}
