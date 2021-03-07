package emailsender;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class MailJava {
	// link que estou usando para aprender a usar isso: https://www.mballem.com/post/enviando-email-com-a-api-javamail/
	
	//indica se o formato de texto será texto ou html
    public static final String TYPE_TEXT_PLAIN = "text/plain";
    public static final String TYPE_TEXT_HTML = "text/html";
    
    /*
       Servidor		smtpHostMail		smtpPortMail	smtpStarttls
     
        Gmail		smtp.gmail.com			587				true
		Bol			smtps.bol.com.br		587				true
		Ibest		smtp.ibest.com.br		587				true
		IG	 		smtp.ig.com.br			587				true
		Hotmail		smtp.live.com			25				true
		
		Caso deseje testar com mais algum servidor faça uma busca no Google por acesso SMTP + o servidor desejado.
     */
    //indica qual sera o servidor de email(gmail, hotmail...) /\
	private String smtpHostMail;
    //indica a porta de acesso ao servidor
    private String smtpPortMail;
    //indica que a necessidade de autenticacao 
	// no servidor(true ou false)
    private String smtpAuth;
    //indica ao servidor que ele esta recebendo uma conexao segura
    private String smtpStarttls;
    //nome do remetente do email
    private String fromNameMail;
    //email do remetente
    private String userMail;
    //senha do email do remetente
    private String passMail;
    //assunto do email
    private String subjectMail;
    //corpo do email, onde esta o texto da mensagem
    private String bodyMail;
    //lista com email e nome dos destinatarios
    private Map<String, String> toMailsUsers;
    //lista contendo os arquivos anexos
    private List<String> fileMails;
    //charset, no caso de html e necessario
    private String charsetMail;
    //tipo do formato da mensagem, texto ou html
    private String typeTextMail;
    
    
    
    public String getSmtpHostMail() {
		return smtpHostMail;
	}
	public void setSmtpHostMail(String smtpHostMail) {
		this.smtpHostMail = smtpHostMail;
	}
	public String getSmtpPortMail() {
		return smtpPortMail;
	}
	public void setSmtpPortMail(String smtpPortMail) {
		this.smtpPortMail = smtpPortMail;
	}
	public String getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public String getSmtpStarttls() {
		return smtpStarttls;
	}
	public void setSmtpStarttls(String smtpStarttls) {
		this.smtpStarttls = smtpStarttls;
	}
	public String getFromNameMail() {
		return fromNameMail;
	}
	public void setFromNameMail(String fromNameMail) {
		this.fromNameMail = fromNameMail;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getPassMail() {
		return passMail;
	}
	public void setPassMail(String passMail) {
		this.passMail = passMail;
	}
	public String getSubjectMail() {
		return subjectMail;
	}
	public void setSubjectMail(String subjectMail) {
		this.subjectMail = subjectMail;
	}
	public String getBodyMail() {
		return bodyMail;
	}
	public void setBodyMail(String bodyMail) {
		this.bodyMail = bodyMail;
	}
	public Map<String, String> getToMailsUsers() {
		return toMailsUsers;
	}
	public void setToMailsUsers(Map<String, String> toMailsUsers) {
		this.toMailsUsers = toMailsUsers;
	}
	public List<String> getFileMails() {
		return fileMails;
	}
	public void setFileMails(List<String> fileMails) {
		this.fileMails = fileMails;
	}
	public String getCharsetMail() {
		return charsetMail;
	}
	public void setCharsetMail(String charsetMail) {
		this.charsetMail = charsetMail;
	}
	public String getTypeTextMail() {
		return typeTextMail;
	}
	public void setTypeTextMail(String typeTextMail) {
		this.typeTextMail = typeTextMail;
	}
}
