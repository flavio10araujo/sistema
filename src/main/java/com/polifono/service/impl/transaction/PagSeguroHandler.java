package com.polifono.service.impl.transaction;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Transaction;
import com.polifono.model.enums.TransactionStatus;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerCreditService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PagSeguroHandler {

    private final ITransactionService transactionService;
    private final PlayerCreditService playerCreditService;
    private final SendEmailService emailSendUtil;

    public boolean isTransactionPaidOrAvailable(br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction) {
        int status = pagSeguroTransaction.getStatus().getValue();
        return status == TransactionStatus.PAID.getValue() || status == TransactionStatus.AVAILABLE.getValue();
    }

    public void handleSuccess(Transaction transaction) {
        // Add credits to the player.
        playerCreditService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());

        // Register this transaction as finished (to avoid that the player receive twice or more the credits).
        transaction.setClosed(true);
        transactionService.save(transaction);

        // Send e-mail.
        emailSendUtil.sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
    }
}
