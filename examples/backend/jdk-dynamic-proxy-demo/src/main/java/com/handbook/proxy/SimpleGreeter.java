package com.handbook.proxy;

public final class SimpleGreeter implements Greeter {
    @Override
    public String greet(String name) {
        return "Hello, " + name;
    }
}
