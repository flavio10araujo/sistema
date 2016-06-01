package com.polifono.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.domain.Player;
import com.polifono.domain.Transaction;
import com.polifono.service.PlayerService;
import com.polifono.service.TransactionService;
import com.polifono.util.EmailSendUtil;

import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;

@Controller
public class PaymentController extends BaseController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PlayerService playerService;

	@RequestMapping(value = {"/buycredits"}, method = RequestMethod.GET)
	public final String buycredits(final Model model) {
		
		// If the player has not confirmed his e-mail yet.
		if (!verifyEmailConfirmed()) {
			model.addAttribute("player", new Player());
			model.addAttribute("playerResend", new Player());
			model.addAttribute("codRegister", 2);
			model.addAttribute("msgRegister", "<br />Para poder comprar créditos é necessário primeiramente confirmar o seu e-mail.<br />No momento do seu cadastro, nós lhe enviamos um e-mail de confirmação de cadastro com um código de ativação. Caso você não possua mais esse e-mail, utilize essa tela para reenviar o código de ativação.");
			return "emailconfirmation";
		}
		
		return "buycredits";
	}
	
	/**
	 * Verify if the player has already confirmed his e-mail.
	 * Return true if the player has already confirmed it. Return false otherwise.
	 * 
	 * @return
	 */
	public boolean verifyEmailConfirmed() {
		
		int playerId = currentAuthenticatedUser().getUser().getId();
		Player player = playerService.getPlayer(playerId);
		
		if (player.isIndEmailConfirmed()) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(value = {"/buycredits"}, method = RequestMethod.POST)
	public final String buycreditssubmit(final Model model, @RequestParam("quantity") String quantityStr) {

		List<String> intParameters = new ArrayList<String>();
		intParameters.add(quantityStr);
		
		if (!isParameterInteger(intParameters)) {
			return "redirect:/";
		}
		
		int quantity = Integer.parseInt(quantityStr);
		
		if (quantity == 0 || quantity > 100) {
			model.addAttribute("codRegister", "2");
			model.addAttribute("msg", "A quantidade de créditos deve ser entre 0 e 100.");
			return "buycredits";
		}
		
		Player player = currentAuthenticatedUser().getUser();
		
		// Register an item in T012_TRANSACTION.
		// This item is not a transaction yet, but it already contain the player, the quantity of credits and the date.
		// The T012.C002_ID will be passed in the attribute "reference".
		Transaction t = new Transaction();
		t.setPlayer(player);
		t.setQuantity(quantity);
		t.setDtInc(new Date());
		t.setClosed(false);
		transactionService.save(t);

		try {
            return "redirect:" + openPagSeguro(t, player, quantity);
        }
        catch (PagSeguroServiceException e) {
            System.err.println(e.getMessage());

            model.addAttribute("codRegister", "2");
			model.addAttribute("msg", "Ocorreu um erro ao acessar o sistema PagSeguro. Por favor, tente novamente em alguns instantes.");
            return "buycredits";
        }
	}
	
	/**
	 * This method connects to the payment system and create a code.
	 * This code is the checkout.
	 * The payment system return and URL. Ex.: https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=EAE29CA5383892D224E9AF9FEFF0FC43
	 * This code is not the transaction code yet.
	 * 
	 * @param t
	 * @param player
	 * @param quantity
	 * @return
	 * @throws PagSeguroServiceException
	 */
	public String openPagSeguro(Transaction t, Player player, int quantity) throws PagSeguroServiceException {
		Checkout checkout = new Checkout();

		checkout.addItem(
			"0001", // Item's number.
			"Créditos Polifono", // Item's name.
			quantity, // Item's quantity.
			new BigDecimal("1.00"), // Price for each unity.
			new Long(0), // Weight.
			null // ShippingCost
		);
		
		checkout.setShippingCost(new BigDecimal("0.00"));

        checkout.setSender(
        	player.getName(), // Client's name.
        	"c08806545722997224739@sandbox.pagseguro.com.br" // Client's e-mail. player.getEmail()
        );

        checkout.setCurrency(Currency.BRL);

        // Sets a reference code for this payment request. The T012.C002_ID is used in this attribute.
        checkout.setReference(""+t.getId());

        checkout.setNotificationURL("http://www.polifono.com/pagseguronotification");

        checkout.setRedirectURL("http://www.polifono.com/pagseguroreturn");
        
        Boolean onlyCheckoutCode = false;
        String checkoutURL = checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);

        System.out.println(checkoutURL);
        
        return checkoutURL;
	}

	@RequestMapping(value = {"/pagseguroreturn"}, method = RequestMethod.GET)
	public final String returnpagseguro(final Model model, @RequestParam("tid") String transactionCode) {

		System.out.println("/pagseguroreturn tid=" + transactionCode);
		
		if (transactionCode == null || "".equals(transactionCode)) {
			return "redirect:/";
		}
		
		List<Transaction> transactions = transactionService.findTransactionByCode(transactionCode);
		
		// It the transaction is already registered.
		if (transactions != null && transactions.size() > 0) {
			return "buycredits";
		}
		
		br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
        	pagSeguroTransaction = TransactionSearchService.searchByCode(PagSeguroConfig.getAccountCredentials(), transactionCode);
        }
        catch (PagSeguroServiceException e) {
        	System.out.println("/pagseguroreturn ERROR with tid=" + transactionCode);
        	System.err.println(e.getMessage());
        	
        	// If the transactionCode is not valid.
        	if (pagSeguroTransaction == null) {
        		System.out.println("/pagseguroreturn TID not valid tid=" + transactionCode);
        		return "redirect:/";
        	}
        }
        
        Transaction transaction = transactionService.find(Integer.parseInt(pagSeguroTransaction.getReference()));
        
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
		
		model.addAttribute("codRegister", "1");
		model.addAttribute("msg", "Obrigado pela compra. Assim que a compra for confirmada, os créditos serão adicionados à sua conta.");
		return "buycredits";
	}
	
	@RequestMapping(value = "/pagseguronotification", method = RequestMethod.POST)
	public @ResponseBody String pagseguronotification(final Model model, @RequestParam("notificationCode") String notificationCode) {
		
		System.out.println("/pagseguronotification notificationCode=" + notificationCode);
		
		if (notificationCode == null || "".equals(notificationCode)) {
			return null;
		}
		
		br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
        	pagSeguroTransaction = NotificationService.checkTransaction(PagSeguroConfig.getAccountCredentials(), notificationCode);
        }
        catch (PagSeguroServiceException e) {
            System.out.println("/pagseguronotification ERROR with notificationCode=" + notificationCode);
        	System.err.println(e.getMessage());
        	
        	// If the notificationCode is not valid.
        	if (pagSeguroTransaction == null) {
        		System.out.println("/pagseguronotification TID not valid notificationCode=" + notificationCode);
        		return null;
        	}
        }

        // TODO - preciso entender melhor como funcionam os status da transação.
        // TODO - posso considerar o status PAID ou CANCELLED como sendo o último?
        
        // pagSeguroTransaction.getReference() == T012.C012_ID
        Transaction transaction = transactionService.find(Integer.parseInt(pagSeguroTransaction.getReference()));
        
        // If the player has already received this credits.
        if (transaction.isClosed()) {
        	return null;
        }
        
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
		
		// If the status is PAID (3).
		if (pagSeguroTransaction.getStatus().getValue() == 3) {
			// Add credits to the player.
			playerService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());
			
			// Register this transaction as finished (to avoid that the player receive twice or more the credits).
			transaction.setClosed(true);
			transactionService.save(transaction);
			
			// Send e-mail.
			sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
		}
		
		return "buycredits";
	}
	
	public void sendEmailPaymentRegistered(Player player, int quantity) {
		String[] args = new String[2];
		args[0] = player.getName();
		args[1] = ""+quantity;
		
		try {
			EmailSendUtil.sendHtmlMail(3, player.getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}