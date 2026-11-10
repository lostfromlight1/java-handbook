package com.handbook.di;

public final class EnglishGreetingService implements GreetingService {
    @Override
    public String greet(String name) {
        return "Hello, " + name;
    }
}
