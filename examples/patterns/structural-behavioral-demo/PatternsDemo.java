public final class PatternsDemo {
    public static void main(String[] args) {
        ModernClient client = new LegacyAdapter(new LegacyApi());
        System.out.println("adapter=" + client.get("data"));

        Checkout checkout = new Checkout();
        checkout.setStrategy(new TenPercentOff());
        System.out.println("strategy=discount" + (1000 - checkout.total(1000)) / 10);

        EventBus bus = new EventBus();
        bus.subscribe(e -> {});
        bus.subscribe(e -> {});
        bus.publish("order.created");
        System.out.println("observer=events:" + bus.listenerCount());

        System.out.println("patterns=ok");
    }
}
