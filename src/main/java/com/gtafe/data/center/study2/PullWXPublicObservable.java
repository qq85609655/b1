package com.gtafe.data.center.study2;

import java.util.ArrayList;
import java.util.List;

public class PullWXPublicObservable {
    private List<IWXUser2> mUsers;
    public  PullWXPublicObservable(){
        mUsers=new ArrayList<>();
    }
    /**
     * 注册
     */
    public void register(IWXUser2 user){
        mUsers.add(user);
    }
    /**
     * 解注册
     */
    public void unregister(IWXUser2 user){
        mUsers.remove(user);
    }
    /**
     * 更新文章
     */
    public void update(String article){
        for (IWXUser2 user : mUsers) {
            user.pull(this);
        }
    }
}
