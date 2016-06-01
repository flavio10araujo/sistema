package br.com.uol.pagseguro.utils.collections;

import java.util.Collection;
import java.util.HashMap;

public class MultiMap<K, V, C extends Collection<? super V>> extends HashMap<K, C> {

    private static final long serialVersionUID = 1L;

    private final Factory<C> collectionFactory;

    public MultiMap(Factory<C> collectionFactory) {
        this.collectionFactory = collectionFactory;
    }

    public MultiMap<K, V, C> add(K key, V value) {
        C collection = get(key);
        if (collection == null) {
            collection = collectionFactory.create();
        }
        collection.add(value);
        put(key, collection);
        return this;
    }

}
