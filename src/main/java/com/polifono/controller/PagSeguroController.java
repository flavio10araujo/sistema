package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_BUY_CREDITS;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.model.entity.Transaction;
import com.polifono.service.impl.transaction.PagSeguroHandler;
import com.polifono.service.impl.transaction.PagSeguroService;
import com.polifono.service.impl.transaction.TransactionService;
import com.polifono.service.impl.transaction.TransactionUpdater;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PagSeguroController {

    private final MessageSource messagesResource;
    private final TransactionService transactionService;
    private final PagSeguroService pagSeguroService;
    private final TransactionUpdater transactionUpdater;
    private final PagSeguroHandler pagSeguroHandler;

    @Validated
    @GetMapping("/pagseguroreturn")
    public String returnPagSeguro(final Model model,
            @RequestParam("tid") @NotBlank String transactionCode,
            Locale locale) {

        List<Transaction> transactions = transactionService.findByCode(transactionCode);
        if (transactionService.isTransactionListNotEmpty(transactions)) {
            prepareModelForBuyCreditsPage(model, locale);
            return URL_BUY_CREDITS;
        }

        Optional<br.com.uol.pagseguro.domain.Transaction> pagSeguroTransactionOpt = pagSeguroService.getTransactionByCode(transactionCode);
        if (pagSeguroTransactionOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = pagSeguroTransactionOpt.get();
        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));
        if (transactionOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Transaction transaction = transactionOpt.get();
        transactionUpdater.updateTransaction(transaction, pagSeguroTransaction);
        transactionService.save(transaction);

        if (pagSeguroHandler.isTransactionPaidOrAvailable(pagSeguroTransaction)) {
            pagSeguroHandler.handleSuccess(transaction);
        }

        prepareModelForBuyCreditsPage(model, locale);
        return URL_BUY_CREDITS;
    }

    @Validated
    @PostMapping("/pagseguronotification")
    public @ResponseBody String pagSeguroNotification(@RequestParam("notificationCode") @NotBlank String notificationCode) {

        Optional<br.com.uol.pagseguro.domain.Transaction> pagSeguroTransactionOpt = pagSeguroService.getTransactionByNotificationCode(notificationCode);
        if (pagSeguroTransactionOpt.isEmpty()) {
            return null;
        }

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = pagSeguroTransactionOpt.get();
        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));
        if (transactionOpt.isEmpty() || transactionOpt.get().isClosed()) {
            return null;
        }

        Transaction newTransaction = transactionUpdater.createNewTransactionFromExisting(transactionOpt.get(), pagSeguroTransaction);
        transactionService.save(newTransaction);

        if (pagSeguroHandler.isTransactionPaidOrAvailable(pagSeguroTransaction)) {
            pagSeguroHandler.handleSuccess(newTransaction);
        }

        return URL_BUY_CREDITS;
    }

    private void prepareModelForBuyCreditsPage(Model model, Locale locale) {
        model.addAttribute("codRegister", "1");
        model.addAttribute("msg", messagesResource.getMessage("msg.credits.thanks", null, locale));
    }
}
