package com.gtafe.data.center.study2;

import java.util.ArrayList;
import java.util.List;

public class PullWXPublicObservable {
    private List<IWXUser> mUsers;
    public  PullWXPublicObservable(){
        mUsers=new ArrayList<>();
    }
    /**
     * 注册
     */
    public void register(IWXUser user){
        mUsers.add(user);
    }
    /**
     * 解注册
     */
    public void unregister(IWXUser user){
        mUsers.remove(user);
    }
    /**
     * 更新文章
     */
    public void update(String article){
        for (IWXUser user : mUsers) {
            user.pull(this);
        }
    }
}
