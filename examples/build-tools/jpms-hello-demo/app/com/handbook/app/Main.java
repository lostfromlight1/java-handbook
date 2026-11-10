package com.handbook.app;

import com.handbook.greeting.Greeting;

public final class Main {
    public static void main(String[] args) {
        System.out.println("greeting=" + Greeting.message());
        System.out.println("module=" + Main.class.getModule().getName());
    }
}
