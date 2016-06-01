package br.com.uol.pagseguro.domain.paymentrequest;

import java.math.BigDecimal;

public class PaymentRequestReceiverFees {

    /** Installment fee */
    private BigDecimal installmentFeeAmount;

    /** Operational fee */
    private BigDecimal operationalFeeAmount;

    /** Intermediation rate */
    private BigDecimal intermediationRateAmount;

    /** Intermediation fee */
    private BigDecimal intermediationFeeAmount;

    /** Commission fee */
    private BigDecimal commissionFeeAmount;

    /** Efrete value */
    private BigDecimal efrete;

    /**
     * Initializes a new instance of the PaymentRequestReceiverFees class
     */
    public PaymentRequestReceiverFees() {

    }

    /**
     * Initializes a new instance of the PaymentRequestReceiverFees class with the specified arguments
     * 
     * @param installmentFeeAmount
     * @param operationalFeeAmount
     * @param intermediationRateAmount
     * @param intermediationFeeAmount
     * @param commissionFeeAmount
     * @param efrete
     */
    public PaymentRequestReceiverFees(BigDecimal installmentFeeAmount, BigDecimal operationalFeeAmount,
            BigDecimal intermediationRateAmount, BigDecimal intermediationFeeAmount, BigDecimal commissionFeeAmount,
            BigDecimal efrete) {
        super();
        this.installmentFeeAmount = installmentFeeAmount;
        this.operationalFeeAmount = operationalFeeAmount;
        this.intermediationRateAmount = intermediationRateAmount;
        this.intermediationFeeAmount = intermediationFeeAmount;
        this.commissionFeeAmount = commissionFeeAmount;
        this.efrete = efrete;
    }

    /**
     * @return the installmentFeeAmount
     */
    public BigDecimal getInstallmentFeeAmount() {
        return installmentFeeAmount;
    }

    /**
     * Sets the installment fee
     * 
     * @param installmentFeeAmount
     */
    public void setInstallmentFeeAmount(BigDecimal installmentFeeAmount) {
        this.installmentFeeAmount = installmentFeeAmount;
    }

    /**
     * @return the operationalFeeAmount
     */
    public BigDecimal getOperationalFeeAmount() {
        return operationalFeeAmount;
    }

    /**
     * Sets the operational fee
     * 
     * @param operationalFeeAmount
     */
    public void setOperationalFeeAmount(BigDecimal operationalFeeAmount) {
        this.operationalFeeAmount = operationalFeeAmount;
    }

    /**
     * @return the intermediationRateAmount
     */
    public BigDecimal getIntermediationRateAmount() {
        return intermediationRateAmount;
    }

    /**
     * Sets the intermediation rate
     * 
     * @param intermediationRateAmount
     */
    public void setIntermediationRateAmount(BigDecimal intermediationRateAmount) {
        this.intermediationRateAmount = intermediationRateAmount;
    }

    /**
     * @return the intermediationFeeAmount
     */
    public BigDecimal getIntermediationFeeAmount() {
        return intermediationFeeAmount;
    }

    /**
     * Sets the intermediation fee
     * 
     * @param intermediationFeeAmount
     */
    public void setIntermediationFeeAmount(BigDecimal intermediationFeeAmount) {
        this.intermediationFeeAmount = intermediationFeeAmount;
    }

    /**
     * @return the commissionFeeAmount
     */
    public BigDecimal getCommissionFeeAmount() {
        return commissionFeeAmount;
    }

    /**
     * Sets the commission fee
     * 
     * @param commissionFeeAmount
     */
    public void setCommissionFeeAmount(BigDecimal commissionFeeAmount) {
        this.commissionFeeAmount = commissionFeeAmount;
    }

    /**
     * @return the efrete
     */
    public BigDecimal getEfrete() {
        return efrete;
    }

    /**
     * Sets the efrete value
     * 
     * @param efrete
     */
    public void setEfrete(BigDecimal efrete) {
        this.efrete = efrete;
    }
}
