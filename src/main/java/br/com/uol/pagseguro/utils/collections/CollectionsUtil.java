package br.com.uol.pagseguro.utils.collections;

import java.util.Collection;

public class CollectionsUtil {

    /**
     * Agrupa uma coleção em listas com chaves iguais.
     */
    public static <K, V> ListMultiMap<K, V> createMultiMap(Iterable<? extends V> collection, //
            Translator<V, K> keyTranslator) {
        if (collection == null || keyTranslator == null) {
            throw new IllegalArgumentException();
        }
        final ListMultiMap<K, V> map = new ListMultiMap<K, V>();
        copy(collection, keyTranslator, map);
        return map;
    }

    private static <K, V, L extends Collection<? super V>> void copy(Iterable<? extends V> fromCollection, //
            Translator<V, K> keyTranslator, //
            MultiMap<K, V, L> toMap) {
        if (fromCollection == null || keyTranslator == null || toMap == null) {
            throw new IllegalArgumentException();
        }
        for (V object : fromCollection) {
            final K key = keyTranslator.translate(object);
            toMap.add(key, object);
        }
    }

}
