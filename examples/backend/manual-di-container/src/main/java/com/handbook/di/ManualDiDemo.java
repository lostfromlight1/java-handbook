package com.handbook.di;

public final class ManualDiDemo {
    public static void main(String[] args) {
        MiniContainer container = new MiniContainer();
        container.bind(GreetingService.class, EnglishGreetingService.class);

        GreetingController controller = container.get(GreetingController.class);
        String message = controller.handleGreet("Lin");

        System.out.println("greet=" + message);
        System.out.println("diContainer=ok");
    }
}
