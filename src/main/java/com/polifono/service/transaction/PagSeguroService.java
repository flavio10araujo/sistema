package com.polifono.service.transaction;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;

@Service
public class PagSeguroService {

    public Optional<br.com.uol.pagseguro.domain.Transaction> getTransactionByCode(String transactionCode) {
        try {
            return Optional.of(TransactionSearchService.searchByCode(PagSeguroConfig.getAccountCredentials(), transactionCode));
        } catch (PagSeguroServiceException e) {
            return Optional.empty();
        }
    }

    public Optional<br.com.uol.pagseguro.domain.Transaction> getTransactionByNotificationCode(String notificationCode) {
        try {
            return Optional.of(NotificationService.checkTransaction(PagSeguroConfig.getAccountCredentials(), notificationCode));
        } catch (PagSeguroServiceException e) {
            return Optional.empty();
        }
    }
}
