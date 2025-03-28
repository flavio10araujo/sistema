package br.com.uol.pagseguroV2.utils.collections;

import java.util.ArrayList;
import java.util.List;

public class ListMultiMap<K, V> extends MultiMap<K, V, List<V>> {

    private static final long serialVersionUID = 1L;

    public ListMultiMap() {
        super(new Factory<List<V>>() {

            public List<V> create() {
                return new ArrayList<V>();
            }

        });
    }

}
