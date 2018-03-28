package com.gtafe.data.center.dlms.b;

import java.lang.reflect.Proxy;

public class TestB {
    public static void main(String[] args) {
        Man man = new Man("Peakmain");
        IBank bank = (IBank) Proxy.newProxyInstance(
                IBank.class.getClassLoader()
                , new Class<?>[]{IBank.class}
                , new BankInvocationHandler(man));
        bank.applyBank();
        bank.lostBank();
    }
}
