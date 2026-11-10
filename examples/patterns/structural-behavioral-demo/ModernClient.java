interface ModernClient {
    String get(String key);
}

final class LegacyAdapter implements ModernClient {
    private final LegacyApi legacy;

    LegacyAdapter(LegacyApi legacy) {
        this.legacy = legacy;
    }

    @Override
    public String get(String key) {
        return legacy.fetchV2(key);
    }
}
