import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Access-order LRU cache backed by LinkedHashMap (demo only, not thread-safe). */
public final class LruCache<K, V> {
    private final int maxEntries;
    private final Map<K, V> map;

    public LruCache(int maxEntries) {
        this.maxEntries = maxEntries;
        this.map = new LinkedHashMap<>(maxEntries, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LruCache.this.maxEntries;
            }
        };
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    public synchronized int size() {
        return map.size();
    }
}
