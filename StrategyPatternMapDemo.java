import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// Strategy Interface defining map operations
interface MapStrategy<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    boolean containsKey(K key);
    int size();
}

// Concrete strategy using HashMap
class HashMapStrategy<K, V> implements MapStrategy<K, V> {
    private final Map<K, V> map;

    public HashMapStrategy() {
        map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return "HashMapStrategy with data: " + map;
    }
}

// Concrete strategy using TreeMap
class TreeMapStrategy<K, V> implements MapStrategy<K, V> {
    private final Map<K, V> map;

    public TreeMapStrategy() {
        map = new TreeMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return "TreeMapStrategy with data: " + map;
    }
}

// Context class to use the map strategies
class MapContext<K, V> {
    private MapStrategy<K, V> strategy;

    // Constructor to initialize the strategy
    public MapContext(MapStrategy<K, V> strategy) {
        this.strategy = strategy;
    }

    // Method to switch strategy at runtime
    public void setStrategy(MapStrategy<K, V> strategy) {
        this.strategy = strategy;
    }

    public void put(K key, V value) {
        strategy.put(key, value);
    }

    public V get(K key) {
        return strategy.get(key);
    }

    public void remove(K key) {
        strategy.remove(key);
    }

    public boolean containsKey(K key) {
        return strategy.containsKey(key);
    }

    public int size() {
        return strategy.size();
    }

    @Override
    public String toString() {
        return strategy.toString();
    }
}

// Testing the Strategy Pattern implementation
public class StrategyPatternMapDemo {
    public static void main(String[] args) {
        // Initialize MapContext with HashMapStrategy
        MapContext<String, Integer> mapContext = new MapContext<>(new HashMapStrategy<>());

        // Use the HashMap strategy
        mapContext.put("A", 1);
        mapContext.put("B", 2);
        System.out.println("HashMapStrategy: " + mapContext);

        // Switch to TreeMap strategy at runtime
        mapContext.setStrategy(new TreeMapStrategy<>());
        mapContext.put("C", 3);
        mapContext.put("A", 4);  // TreeMap will maintain sorted order
        System.out.println("TreeMapStrategy: " + mapContext);

        // Access elements through MapContext
        System.out.println("Contains key A: " + mapContext.containsKey("A"));
        System.out.println("Size of map: " + mapContext.size());
    }
}
