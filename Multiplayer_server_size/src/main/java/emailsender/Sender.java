package emailsender;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import main.Users;

@SuppressWarnings("unused")
public class Sender {	
	
	private MailJava mj;
	public Sender() { // if recover = false, it's a registration
        mj = new MailJava();
        //configuracoes de envio
        mj.setSmtpHostMail("smtp.gmail.com");
        mj.setSmtpPortMail("587");
        mj.setSmtpAuth("true");
        mj.setSmtpStarttls("true");
        mj.setUserMail("pedrojonasdsm@gmail.com");// seu e-mail
        mj.setFromNameMail("pedrojonasdsm"); // seu nome
        mj.setPassMail("pedrojonas13"); // sua senha do e-mail
        mj.setCharsetMail("ISO-8859-1");
        mj.setTypeTextMail(MailJava.TYPE_TEXT_PLAIN);
        
    }
	
	public void enviar(String email, String password, String user, boolean recover) throws UnsupportedEncodingException, MessagingException {
		if (recover) {
        	mj.setSubjectMail("E-mail autom�tico para recupera��o de senha do JavaPokemon");
	        mj.setBodyMail(recoverMessage(user, password));
        }else {
        	mj.setSubjectMail("E-mail autom�tico de cadastro no JavaPokemon");
	        mj.setBodyMail(signUpMessage(password));
        }
		//sete quantos destinatarios desejar
        Map<String, String> map = new HashMap<String, String>();
        map.put(email, user);
        /*
        map.put("destinatario1@bol.com.br", "email bol");
        map.put("destinatario2@msn.com", "email msn");
        map.put("destinatario3@ig.com.br", "email ig");
        //*/
        mj.setToMailsUsers(map);

        //seta quatos anexos desejar
        List<String> files = new ArrayList<String>();
        //files.add("C:\images\ajax_loader.gif");
        //files.add("C:\images\hover_next.png");
        //files.add("C:\images\hover_prev.png");

        mj.setFileMails(files);
        
        new MailJavaSender().senderMail(mj);
	}

    private String signUpMessage(String token) {
		return
				"Bem vindo ao JavaPokemon!\n"+
				"Esse � um e-mail automatico enviado com seu c�digo para entrar na sua conta\n"+
				"Segue aqui o c�digo:\n"+token+
				"\nObrigado por jogar conosco e bem vindo ao servidor"+
				"Instru��es: Assim que colocar esse c�digo no cliente, voc� ir� para a tela de cadastro, onde nela colocar� seus dados de login e senha"+
				"PS: Seja r�pido, voc� tem 1 hora antes que precise pedir um novo c�digo!"
				;
	}

	private static String recoverMessage(String login, String password) {
    	return
    			"Esse � um email autom�tico de recupera��o do JavaPokemon\n"+
    			"Voc� ou algu�m colocou o seu e-mail no cliente e pediu para recuperar a senha\n"+
    			"Segue aqui a nova senha definida pelo servidor: \n"+password + "\n e espero que n�o tenha esquecido do seu login: \n"+login+ 
    			"\nObrigado e boa jogatina.\n ass: o cara que fez o e-mail autom�tico, Pedrojonassm"
    			;
    }

	private static String htmlMessage() {
        return  "<html> " +
                "<head>" +
                "<title>Email no formato HTML com Javamail!</title> " +
                "</head> " +
                "<body> " +
                "<div style='background-color:orange; width:28%; height:100px;'>" +
                "<ul>  " +
                "<li>Leia o novo tutorial JavaMail do Programando com Java.</li> " +
                "<li>Aprenda como enviar emails com anexos.</li>" +
                " <li>Aprenda como enviar emails em formato texto simples ou html.</li> " +
                "<li>Aprenda como enviar seu email para mais de um destinatario.</li>" +
                "</ul> " +
                "<p>Visite o blog " +
                "<a href='http://mballem.wordpress.com/' target='new'>Programando com Java</a>" +
                "</p>" +
                "</div>" +
                "<div style='background-color:FFFFF; width:28%; height:50px;' align='center'>" +
                "Download do JavaMail<br/>" +
                "<a href='http://www.oracle.com/technetwork/java/javaee/index-138643.html'>" +
                "<img src='https://www.oracleimg.com/admin/images/ocom/hp/oralogo_small.gif'/>" +
                "</a> " +
                "</div>" +
                "</body> " +
                "</html>";
    }
}