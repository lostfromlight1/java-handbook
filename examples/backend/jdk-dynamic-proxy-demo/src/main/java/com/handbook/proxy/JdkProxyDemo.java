package com.handbook.proxy;

import java.lang.reflect.Proxy;

public final class JdkProxyDemo {
    public static void main(String[] args) {
        Greeter target = new SimpleGreeter();
        Greeter proxy = (Greeter) Proxy.newProxyInstance(
                Greeter.class.getClassLoader(),
                new Class<?>[] { Greeter.class },
                new LoggingInvocationHandler(target));

        String result = proxy.greet("Ada");
        System.out.println("result=" + result);
        System.out.println("proxyAop=ok");
    }
}
