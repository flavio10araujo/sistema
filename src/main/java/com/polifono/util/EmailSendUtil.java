package com.polifono.util;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.mail.MultiPartEmail;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

public class EmailSendUtil {
	
	private static ResourceBundle resourceBundle;
	
	static {
		 resourceBundle = ResourceBundle.getBundle("application", Locale.getDefault());
	}
	
	/**
	 * The class MailThread can send an email asynchronously.
	 * If it is used this method, the method that called this method can not know if the email was sent.
	 */
	static class MailAsync extends Thread {

		private String senderAddress;
		private String recipientAddress;
		private String subject;
		private String message;

		public MailAsync(String senderAddress, String subject, String message, String recipientAddress) {
			this.senderAddress = senderAddress;
			this.subject = subject;
			this.message = message;
			this.recipientAddress = recipientAddress;
		}

		@Override
		public void run() {
			try	{
				MultiPartEmail hm = new MultiPartEmail();
				
				/*
				Gmail configuration.
				hm.setHostName("smtp.gmail.com");
				hm.setSslSmtpPort("587");
				hm.setSmtpPort(Integer.parseInt("587"));
				hm.setAuthentication("email@gmail.com", "senha");
				hm.setTLS(true);
				*/

				// Integrator configuration.
				hm.setHostName("localhost");
				hm.setSmtpPort(Integer.parseInt("25"));

				hm.setAuthentication(resourceBundle.getString("email.general"), resourceBundle.getString("email.general.password"));
				
				hm.setCharset("UTF-8");
				hm.setSubject(subject);
				hm.setFrom(senderAddress);
				hm.addTo(recipientAddress);
				
				message = HTMLEntitiesUtil.unhtmlentities(message);
				message = HTMLEntitiesUtil.htmlentities(message);
				
				hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);
				
				hm.send();
				
				System.out.println("E-mail enviado!");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("An email was not sent: ", e);
			}
		}
	}
	
	static class MailSync {

		private String senderAddress;
		private String recipientAddress;
		private String subject;
		private String message;

		public MailSync(String senderAddress, String subject, String message, String recipientAddress) {
			this.senderAddress = senderAddress;
			this.subject = subject;
			this.message = message;
			this.recipientAddress = recipientAddress;
		}

		public void start() {
			try	{
				MultiPartEmail hm = new MultiPartEmail();
				
				// Integrator configuration.
				hm.setHostName("localhost");
				hm.setSmtpPort(Integer.parseInt("25"));
				
				hm.setAuthentication(resourceBundle.getString("email.general"), resourceBundle.getString("email.general.password"));
				
				hm.setCharset("UTF-8");
				hm.setSubject(subject);
				hm.setFrom(senderAddress);
				hm.addTo(recipientAddress);
				
				message = HTMLEntitiesUtil.unhtmlentities(message);
				message = HTMLEntitiesUtil.htmlentities(message);
				
				hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);
				
				hm.send();
				
				System.out.println("E-mail enviado!");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("An email was not sent: ", e);
			}
		}
	}
	
	public static void sendHtmlMail(boolean async, int messageType, String to, String[] args) throws Exception {
		String from = "", subject = "", message = "";
		
		if (messageType == 1) {
			from = resourceBundle.getString("email.noreply");
			subject = "Confirme seu cadastro na " + resourceBundle.getString("email.company");
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Você iniciou seu cadastro na plataforma " + resourceBundle.getString("email.company") + " com o e-mail <a href=\"#\">{1}</a>. Para finalizá-lo, precisamos que você valide seu email.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Para validar:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/emailconfirmation\">" + resourceBundle.getString("email.url") + "/emailconfirmation</a></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe o e-mail cadastrado;</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe o seguinte código de ativação: <b>{2}</b></font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br>Atenciosamente,<br>Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>"
					+ "</td></tr></tbody></table>";

			message = replaceParamsMessage(message, args);
		}
		else if (messageType == 2) {
			from = resourceBundle.getString("email.noreply");
			subject = "Sua solicitação de alteração de senha na " + resourceBundle.getString("email.company");
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Você (ou alguém) solicitou a alteração da senha na plataforma " + resourceBundle.getString("email.company") + " para o login <a href=\"#\">{1}</a>.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Para dar continuidade na alteração da senha:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "\">" + resourceBundle.getString("email.url") + "</a></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Clique no botão [ENTRAR];</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Clique no link [Esqueci minha senha];</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe o login cadastrado;</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe o seguinte código de confirmação da alteração da senha: <b>{2}</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe a nova senha desejada.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\"><br /><br />Caso a alteração de senha não seja mais necessária, apenas ignore este e-mail.</font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		else if (messageType == 3) {
			from = resourceBundle.getString("email.general");
			subject = "Sua compra de créditos foi confirmada na " + resourceBundle.getString("email.company");
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">A sua compra foi confirmada e os créditos adquiridos já estão disponíveis em sua conta.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\"><br />O total de créditos adquiridos foi de: <b>{1}</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Obs.: caso esteja logado no sistema, é necessário deslogar e logar novamente para que os novos créditos sejam mostrados.</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\"><br /><br />Não perca tempo e comece a estudar agora mesmo.</font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		else if (messageType == 4) {
			from = resourceBundle.getString("email.general");
			subject = "Você foi convidado para uma sala de aula na " + resourceBundle.getString("email.company");
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">O professor {1} te adicionou na sala de aula {2}.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\"><br />Caso você queira participar dessa sala de aula, acesse o seguinte link para confirmar sua participação:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\"><br /><br /><a href=\"https://" + resourceBundle.getString("email.url") + "/classinvitation\">" + resourceBundle.getString("email.url") + "/classinvitation</a></font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://"+ resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		else if (messageType == 5) {
			from = resourceBundle.getString("email.general");
			to = resourceBundle.getString("email.general.to");
			subject = "Novo contato: " + args[0];
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>A seguinte mensagem foi enviada por {0}:</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Mensagem: {1} </font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		
		if (async) {
			new EmailSendUtil.MailAsync(from, subject, message, to).start();
		} else {
			new EmailSendUtil.MailSync(from, subject, message, to).start();
		}
	}
	
	public static void sendMessageCommunication(boolean async, int messageType, String to, String[] args) throws Exception {
		String from = "", subject = "", message = "";
		
		if (messageType == 1) {
			
		}
		else if (messageType == 2) {
			
		}
		else if (messageType == 3) {
			
		}
		else if (messageType == 4) {
			from = resourceBundle.getString("email.general");
			subject = "Sentimos sua falta na " + resourceBundle.getString("email.company") + "... Acesse sua conta para continuar a aprender música!";
			
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Sentimos sua falta! Já faz tempo que você não acessa a " + resourceBundle.getString("email.company") + "! "
					+ "<p><font face=\"arial\" size=\"2\">Estamos sempre melhorando nosso sistema e já temos cursos de trompete, violão, saxofone, flauta doce e teoria musical.</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Para continuar a estudar música com a gente:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "\">" + resourceBundle.getString("email.url") + "</a></font></p>";
					
			if (args[1] != null) {
				message = message + "<p><font face=\"arial\" size=\"2\">- Clique no botão [Facebook] que aparecerá na tela;</font></p>";
			} else {
				message = message + "<p><font face=\"arial\" size=\"2\">- Clique no botão [ENTRAR] (caso esteja acessando com celular, antes é preciso clicar no botão para mostrar as opções);</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Informe o seu email e sua senha e clique no botão [ENTRAR];</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Caso tenha esquecido a sua senha, faça:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- - Clique no link [Esqueci minha senha];</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- - Informe o seu email;</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- - Clique no botão [ENVIAR] e você receberá um email com as instruções para a troca da senha.</font></p>";
			}
			
			message = message + "<p><font face=\"arial\" size=\"2\"><br />Após acessar o sistema, escolha o curso de música que você deseja fazer e bons estudos!.</font></p>" 
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Aguardamos você!<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\"></font>"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		else if (messageType == 5) {
			from = resourceBundle.getString("email.general");
			subject = "Sentimos sua falta na " + resourceBundle.getString("email.company") + "... Acesse sua conta para continuar a aprender música!";
			
			message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
					+ "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + resourceBundle.getString("email.company") + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
					+ "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
					+ "<p><font face=\"arial\" size=\"2\">Sentimos sua falta! Já faz tempo que você não acessa a " + resourceBundle.getString("email.company") + "! "
					+ "<p><font face=\"arial\" size=\"2\">Estamos sempre melhorando nosso sistema, criando novas funcionalidades e adicionando novas aulas.</font></p>";
					
			// Credits > 0
			if (Integer.parseInt(args[2]) > 0) {
				message = message + "<p><font face=\"arial\" size=\"2\">Você ainda tem créditos em sua conta da sua última compra!</font></p>";
			}
			
			message = message + "<p><font face=\"arial\" size=\"2\">Mini relatório do aluno:</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Créditos em conta: {2}</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Pontuação total: {3}</font></p>"
					+ "<p><font face=\"arial\" size=\"2\">- Nível musical: {4}</font></p>";
			
			message = message + "<p><font face=\"arial\" size=\"2\"><br />Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "\">" + resourceBundle.getString("email.url") + "</a> e continue estudando para subir de nível!</font></p>"
					+ "</td></tr><tr><td>"
					+ "<font face=\"arial\" size=\"-1\"><br />Aguardamos você!<br />Equipe " + resourceBundle.getString("email.company") + "<br /><br /><strong>" + resourceBundle.getString("email.company") + "</strong> - " + resourceBundle.getString("email.company.slogan") + "</font>"
					+ "</td></tr><tr><td align=\"center\">"
					+ "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + resourceBundle.getString("email.url") + "/#faq\">" + resourceBundle.getString("email.url") + "/#faq</a></font>"
					+ "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\"></font>"
					+ "</td></tr></tbody></table>";
			
			message = replaceParamsMessage(message, args);
		}
		
		if (async) {
			new EmailSendUtil.MailAsync(from, subject, message, to).start();
		} else {
			new EmailSendUtil.MailSync(from, subject, message, to).start();
		}
	}
	
	/**
	 * This method get an email and replaces the strings into the email by the args strings.<br>
	 * Eg:<br>
	 * {0} will be replaced by the first argument into the array<br>
	 * {1} will be replaced by the second argument into the array
	 *
	 * @param bodyText the message
	 * @param args the string to replace into the email string.
	 * @return The new string email with the correct messages.
	 */
	public static String replaceParamsMessage(String message, String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null) {
				message = message.replace("{"+i+"}", args[i]);
			}
		}
		
		return message;
	}
	
	/**
	 * This method is used to send the email to the user confirm his email.
	 * 
	 * @param player
	 */
	public static void sendEmailConfirmRegister(Player player) {
		String[] args = new String[3];
		args[0] = player.getName();
		args[1] = player.getEmail();
		args[2] = player.getEmailConfirmed();
		
		try {
			EmailSendUtil.sendHtmlMail(true, 1, player.getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to send the email to the user when he doesn't remember his password.
	 * 
	 * @param player
	 */
	public static void sendEmailPasswordReset(Player player) {
		String[] args = new String[3];
		args[0] = player.getName();
		args[1] = player.getEmail();
		args[2] = player.getPasswordReset();
		
		try {
			EmailSendUtil.sendHtmlMail(true, 2, player.getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send one email of the type 3 (payment registered).
	 * 
	 * @param player
	 * @param quantity
	 */
	public static void sendEmailPaymentRegistered(Player player, int quantity) {
		String[] args = new String[2];
		args[0] = player.getName();
		args[1] = ""+quantity;
		
		try {
			if (player.getEmail() != null && !"".equals(player.getEmail())) {
				EmailSendUtil.sendHtmlMail(true, 3, player.getEmail(), args);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send one email of the type 4 (invitation to class).
	 * 
	 * @param player
	 * @param classPlayer
	 */
	public static void sendEmailInvitationToClass(Player player, ClassPlayer classPlayer) {
		String[] args = new String[3];
		args[0] = classPlayer.getPlayer().getName(); // Student
		args[1] = player.getName(); // Teacher
		args[2] = classPlayer.getClazz().getName();
		
		try {
			EmailSendUtil.sendHtmlMail(true, 4, classPlayer.getPlayer().getEmail(), args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send one email of the type 5 (contact).
	 * 
	 * @param email
	 * @param message
	 */
	public static void sendEmailContact(String email, String message) {
		String[] args = new String[2];
		args[0] = email;
		args[1] = message;
		
		try {
			EmailSendUtil.sendHtmlMail(true, 5, "", args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to send the email to list of players from the group communication of type groupcommunicationId.
	 * 
	 * @param groupcommunicationId
	 * @param players
	 */
	public static void sendEmailCommunication(int groupcommunicationId, List<Player> players) {
		for (Player player : players) {
			String[] args = new String[5];
			args[0] = player.getName();
			args[1] = (player.getIdFacebook() != null ? String.valueOf(player.getIdFacebook()) : null);
			args[2] = String.valueOf(player.getCredit());
			args[3] = String.valueOf(player.getScore());
			args[4] = "(" + player.getRankLevel() + ") " + player.getRankColor();
			
			try {
				EmailSendUtil.sendMessageCommunication(false, groupcommunicationId, player.getEmail(), args);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}