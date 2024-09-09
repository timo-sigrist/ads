package ch.zhaw.ads;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashtable<K, V> implements Map<K, V> {
    private K[] keys = (K[]) new Object[10];
    private V[] values = (V[]) new Object[10];

    private int hash(Object k) {
        int h = Math.abs(k.hashCode());
        return h % keys.length;
    }

    public MyHashtable(int size) {
        keys = (K[]) new Object[size];
        values = (V[]) new Object[size];
    }

    // Removes all mappings from this map (optional operation).
    public void clear() {
        keys = (K[]) new Object[keys.length];
        values = (V[]) new Object[values.length];
    }

    // Associates the specified value with the specified key in this map (optional operation).
    public V put(K key, V value) {
        int index = find(key);

        keys[index] = key;
        values[index] = value;

        if (getUsage() >= 51) {
            resize();
        }

        return value;
    }


    private void resize() {
        K[] keysOld = Arrays.copyOf(keys, keys.length);
        V[] valuesOld = Arrays.copyOf(values, keys.length);

        keys = (K[]) new Object[keysOld.length * 2];
        values = (V[]) new Object[valuesOld.length * 2];
        try {
            for (int i = 0; i < keysOld.length; i++) {
                K key = keysOld[i];
                if (key != null)
                    put(key, valuesOld[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getUsage() {
        return size() / keys.length * 100;
    }

    int find(Object x) {
        int currentPos = hash(x);
        while (keys[currentPos] != null && !keys[currentPos].equals(x)) {
            currentPos = (currentPos + 1) % keys.length;
        }
        return currentPos;
    }

    // Returns the value to which this map maps the specified key.
    public V get(Object key) {
        int index = find(key);
        if (keys[index] == null) return null;
        if (keys[index].equals(key)) return values[index];
        return null;
    }

    // Removes the mapping for this key from this map if present (optional operation).
    public V remove(Object key) {
        int index = find(key);
        if (keys[index] == null) return null;
        V value = null;
        if (keys[index].equals(key)) {
            value = values[index];
            values[index] = null;
            keys[index] = null;
        }

        reorganize(index);

        return value;
    }

    private void reorganize(int index) {
        int replace = index;
        index = (index + 1) % keys.length;
        while (keys[index] != null) {

            int newIndex = hash(keys[index]);
            if (keys[newIndex] != null) { // find a better index and not occupied
                while (newIndex <= index) {
                    if (keys[newIndex] == null) {
                        break;
                    }
                    newIndex++;
                }

            }
            if (newIndex >= index) { // no better index => skip
                index = (index + 1) % keys.length;
                continue;
            }

            keys[replace] = keys[index];
            values[replace] = values[index];
            keys[index] = null;
            values[index] = null;
            replace = index;

            index = (index + 1) % keys.length;

        }
    }

    // Returns the number of key-value mappings in this map.
    public int size() {
        // to be done
        int size = 0;
        for (K key : keys) {
            if (key != null) size++;
        }
        return size;
    }

    // UnsupportedOperationException ===================================================================
    // Returns a collection view of the values contained in this map.
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map contains a mapping for the specified key.
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map maps one or more keys to the specified value.
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    // Returns a set view of the mappings contained in this map.
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    // Compares the specified object with this map for equality.
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    // Copies all of the mappings from the specified map to this map (optional operation).
    public void putAll(Map<? extends K, ? extends V> t) {
        throw new UnsupportedOperationException();
    }

    // Returns the hash code value for this map.
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map contains no key-value mappings.
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    // Returns a set view of the keys contained in this map.
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
}
