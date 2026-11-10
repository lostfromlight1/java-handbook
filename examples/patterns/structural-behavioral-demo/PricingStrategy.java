interface PricingStrategy {
    int priceInCents(int baseCents);
}

final class NoDiscount implements PricingStrategy {
    @Override
    public int priceInCents(int baseCents) {
        return baseCents;
    }
}

final class TenPercentOff implements PricingStrategy {
    @Override
    public int priceInCents(int baseCents) {
        return baseCents * 90 / 100;
    }
}

final class Checkout {
    private PricingStrategy strategy = new NoDiscount();

    void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    int total(int baseCents) {
        return strategy.priceInCents(baseCents);
    }
}
