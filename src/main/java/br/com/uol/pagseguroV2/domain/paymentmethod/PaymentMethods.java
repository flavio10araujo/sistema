package br.com.uol.pagseguroV2.domain.paymentmethod;

import br.com.uol.pagseguroV2.enums.PaymentMethodCode;
import br.com.uol.pagseguroV2.enums.PaymentMethodStatus;
import br.com.uol.pagseguroV2.enums.PaymentMethodType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents the payment methods
 */
public class PaymentMethods {

    /**
     * Payment Methods
     */
    private final Map<PaymentMethodType, Map<Integer, PaymentMethod>> paymentMethods;

    /**
     * Initializes a new instance of the PaymentMethods class
     *
     * @param paymentMethods
     */
    public PaymentMethods(Map<PaymentMethodType, Map<Integer, PaymentMethod>> paymentMethods) {
        if (paymentMethods == null) {
            throw new IllegalArgumentException();
        }
        this.paymentMethods = paymentMethods;
    }

    /**
     * Verify if the paymentMethodCode is available
     *
     * @param paymentMethodCode
     * @return
     */
    public boolean isAvailable(PaymentMethodCode paymentMethodCode) {
        if (paymentMethodCode == null) {
            throw new IllegalArgumentException();
        }

        final Map<Integer, PaymentMethod> paymentMethodsByType = paymentMethods.get(paymentMethodCode.getType());
        if (paymentMethodsByType != null) {
            final PaymentMethod paymentMethod = paymentMethodsByType.get(paymentMethodCode.getValue());
            if (paymentMethod != null && PaymentMethodStatus.AVAILABLE.equals(paymentMethod.getStatus())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a list of payment methods of a type
     *
     * @param paymentMethodType
     * @return
     */
    public List<PaymentMethod> getPaymentMethodsByType(PaymentMethodType paymentMethodType) {
        if (paymentMethodType == null) {
            throw new IllegalArgumentException();
        }

        final Map<Integer, PaymentMethod> paymentMethodsByType = paymentMethods.get(paymentMethodType);
        if (paymentMethodsByType == null) {
            return Collections.emptyList();
        }

        final List<PaymentMethod> paymentMethodList = new ArrayList<PaymentMethod>(paymentMethodsByType.values());

        Collections.sort(paymentMethodList);
        return Collections.unmodifiableList(paymentMethodList);
    }

}
