package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_BUY_CREDITS;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.model.entity.Transaction;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerCreditService;

import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PagSeguroController {

    private final MessageSource messagesResource;
    private final PlayerCreditService playerCreditService;
    private final SendEmailService emailSendUtil;
    private final ITransactionService transactionService;

    @GetMapping("/pagseguroreturn")
    public String returnPagSeguro(final Model model, @RequestParam("tid") String transactionCode, Locale locale) {

        log.debug("/pagseguroreturn tid=", transactionCode);
        System.out.println("/pagseguroreturn tid=" + transactionCode);

        if (transactionCode == null || transactionCode.isEmpty())
            return REDIRECT_HOME;

        List<Transaction> transactions = transactionService.findByCode(transactionCode);

        model.addAttribute("codRegister", "1");
        model.addAttribute("msg", messagesResource.getMessage("msg.credits.thanks", null, locale));

        // If the transaction is already registered.
        if (transactions != null && !transactions.isEmpty())
            return URL_BUY_CREDITS;

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
            pagSeguroTransaction = TransactionSearchService.searchByCode(PagSeguroConfig.getAccountCredentials(), transactionCode);
        } catch (PagSeguroServiceException e) {
            log.debug("/pagseguroreturn ERROR with tid= {} ", transactionCode);

            // If the transactionCode is not valid.
            if (pagSeguroTransaction == null) {
                log.debug("/pagseguroreturn TID not valid tid {}", transactionCode);
                return REDIRECT_HOME;
            }
        }

        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));

        if (transactionOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Transaction transaction = transactionOpt.get();

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

        transactionService.save(transaction);

        // If the status is:
        // PAID (3) OR AVAILABLE (4)
        if (pagSeguroTransaction.getStatus().getValue() == 3 || pagSeguroTransaction.getStatus().getValue() == 4) {
            // Add credits to the player.
            playerCreditService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());

            // Register this transaction as finished (to avoid that the player receive twice or more the credits).
            transaction.setClosed(true);
            transactionService.save(transaction);

            // Send e-mail.
            emailSendUtil.sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
        }

        return URL_BUY_CREDITS;
    }

    @PostMapping("/pagseguronotification")
    public @ResponseBody String pagseguronotification(@RequestParam("notificationCode") String notificationCode) {

        log.debug("/pagseguronotification notificationCode = {}", notificationCode);

        if (notificationCode == null || notificationCode.isEmpty())
            return null;

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
            pagSeguroTransaction = NotificationService.checkTransaction(PagSeguroConfig.getAccountCredentials(), notificationCode);
        } catch (PagSeguroServiceException e) {
            log.debug("/pagseguronotification ERROR with notificationCode = {}", notificationCode);
            log.debug("error", e);

            // If the notificationCode is not valid.
            if (pagSeguroTransaction == null) {
                log.debug("/pagseguronotification TID not valid notificationCode = {}", notificationCode);
                return null;
            }
        }

        // TODO - preciso entender melhor como funcionam os status da transação.
        // TODO - posso considerar o status PAID ou CANCELLED como sendo o último?
        // pagSeguroTransaction.getReference() == T012.C012_ID
        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));

        if (transactionOpt.isEmpty()) {
            return null;
        }

        Transaction transaction = transactionOpt.get();

        // If the player has already received this credits.
        if (transaction.isClosed())
            return null;

        Transaction tNew = new Transaction();
        tNew.setPlayer(transaction.getPlayer());
        tNew.setQuantity(transaction.getQuantity());
        tNew.setDtInc(transaction.getDtInc());
        tNew.setClosed(false);
        tNew.setCode(pagSeguroTransaction.getCode());
        tNew.setReference(pagSeguroTransaction.getReference());
        tNew.setDate(pagSeguroTransaction.getDate());
        tNew.setLastEventDate(pagSeguroTransaction.getLastEventDate());
        tNew.setType(pagSeguroTransaction.getType().getValue());
        tNew.setStatus(pagSeguroTransaction.getStatus().getValue());
        tNew.setPaymentMethodType(pagSeguroTransaction.getPaymentMethod().getType().getValue());
        tNew.setPaymentMethodCode(pagSeguroTransaction.getPaymentMethod().getCode().getValue());
        tNew.setGrossAmount(pagSeguroTransaction.getGrossAmount());
        tNew.setDiscountAmount(pagSeguroTransaction.getDiscountAmount());
        tNew.setFeeAmount(pagSeguroTransaction.getFeeAmount());
        tNew.setNetAmount(pagSeguroTransaction.getNetAmount());
        tNew.setExtraAmount(pagSeguroTransaction.getExtraAmount());
        tNew.setInstallmentCount(pagSeguroTransaction.getInstallmentCount());
        tNew.setItemCount(pagSeguroTransaction.getItemCount());
        tNew.setEscrowEndDate(pagSeguroTransaction.getEscrowEndDate());
        tNew.setCancellationSource(pagSeguroTransaction.getCancellationSource());
        tNew.setPaymentLink(pagSeguroTransaction.getPaymentLink());

        transactionService.save(tNew);

        // If the status is:
        // PAID (3) OR AVAILABLE (4)
        if (pagSeguroTransaction.getStatus().getValue() == 3 || pagSeguroTransaction.getStatus().getValue() == 4) {
            // Add credits to the player.
            playerCreditService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());

            // Register this transaction as finished (to avoid that the player receive twice or more the credits).
            transaction.setClosed(true);
            transactionService.save(transaction);

            // Send e-mail.
            emailSendUtil.sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
        }

        return URL_BUY_CREDITS;
    }
}
