package com.handbook.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before=" + method.getName());
        Object result = method.invoke(target, args);
        return result;
    }
}
