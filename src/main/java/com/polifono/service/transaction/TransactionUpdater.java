package com.polifono.service.transaction;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Transaction;

@Service
public class TransactionUpdater {

    public void updateTransaction(Transaction transaction, br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction) {
        transaction.setCode(pagSeguroTransaction.getCode());
        transaction.setReference(pagSeguroTransaction.getReference());
        transaction.setDate(pagSeguroTransaction.getDate());
        transaction.setLastEventDate(pagSeguroTransaction.getLastEventDate());
        transaction.setType(pagSeguroTransaction.getType().getValue());
        transaction.setStatus(pagSeguroTransaction.getStatus().getValue());
        transaction.setPaymentMethodType(pagSeguroTransaction.getPaymentMethod().getType().getValue());
        transaction.setPaymentMethodCode(pagSeguroTransaction.getPaymentMethod().getCode().getValue());
        transaction.setGrossAmount(pagSeguroTransaction.getGrossAmount());
        transaction.setDiscountAmount(pagSeguroTransaction.getDiscountAmount());
        transaction.setFeeAmount(pagSeguroTransaction.getFeeAmount());
        transaction.setNetAmount(pagSeguroTransaction.getNetAmount());
        transaction.setExtraAmount(pagSeguroTransaction.getExtraAmount());
        transaction.setInstallmentCount(pagSeguroTransaction.getInstallmentCount());
        transaction.setItemCount(pagSeguroTransaction.getItemCount());
        transaction.setEscrowEndDate(pagSeguroTransaction.getEscrowEndDate());
        transaction.setCancellationSource(pagSeguroTransaction.getCancellationSource());
        transaction.setPaymentLink(pagSeguroTransaction.getPaymentLink());
    }

    public Transaction createNewTransactionFromExisting(Transaction existingTransaction, br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction) {
        Transaction newTransaction = new Transaction();
        newTransaction.setPlayer(existingTransaction.getPlayer());
        newTransaction.setQuantity(existingTransaction.getQuantity());
        newTransaction.setDtInc(existingTransaction.getDtInc());
        newTransaction.setClosed(false);
        updateTransaction(newTransaction, pagSeguroTransaction);
        return newTransaction;
    }
}
