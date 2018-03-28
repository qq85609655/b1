package com.gtafe.data.center.dlms.b;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BankInvocationHandler implements InvocationHandler {
    /**
     * 被代理的对象
     */
    private Object mObject;

    public BankInvocationHandler(Object object){
        this.mObject = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 执行方法 ，目标接口调用的方法都会来到这里面
        System.out.println("开始受理");
        // 调用被代理对象的方法,这里其实调用的就是  man 里面的 applyBank 方法
        Object voidObject = method.invoke(mObject,args);
        System.out.println("操作完毕");
        return voidObject;
    }

}
