package com.handbook.di;

/** Application entry that depends on GreetingService — constructor receives the implementation. */
public final class GreetingController {
    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String handleGreet(String name) {
        return greetingService.greet(name);
    }
}
