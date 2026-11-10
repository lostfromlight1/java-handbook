public final class InMemoryCacheDemo {
    public static void main(String[] args) {
        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("user:1", "alice");
        cache.put("user:2", "bob");
        cache.get("user:1"); // touch for LRU
        cache.put("user:3", "carol"); // evicts user:2
        System.out.println("size=" + cache.size());
        System.out.println("user:1=" + cache.get("user:1").orElse("miss"));
        System.out.println("user:2=" + cache.get("user:2").orElse("miss"));
        System.out.println("in-memory-cache=ok");
    }
}
