package br.com.uol.pagseguro.domain.installment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.com.uol.pagseguro.utils.collections.ListMultiMap;

/**
 * Represents the option of installment of a credit card payment returned by PagSeguro API
 */
public class Installments implements Iterable<Installment> {

    /**
     * Installments
     */
    private final ListMultiMap<String, Installment> installments;

    /**
     * Initializes a new instance of the Installments class
     * 
     * @param installments
     */
    public Installments(ListMultiMap<String, Installment> installments) {
        if (installments == null) {
            throw new IllegalArgumentException();
        }
        this.installments = installments;
    }

    public List<Installment> get(String cardBrand) {
        if (cardBrand == null) {
            throw new IllegalArgumentException();
        }

        final List<Installment> installmentList = installments.get(cardBrand);
        if (installmentList == null || installmentList.size() == 0) {
            return Collections.emptyList();
        }

        Collections.sort(installmentList);
        return Collections.unmodifiableList(installmentList);
    }

    public Iterator<Installment> iterator() {
        final ArrayList<Installment> installmentList = new ArrayList<Installment>();
        for (List<Installment> value : installments.values()) {
            installmentList.addAll(value);
        }

        Collections.sort(installmentList);
        return Collections.unmodifiableList(installmentList).iterator();
    }

}
