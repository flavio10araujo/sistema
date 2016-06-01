package br.com.uol.pagseguro.utils.collections;

public interface Translator<D, P> {

    P translate(D d);

}
