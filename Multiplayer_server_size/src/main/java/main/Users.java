package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import dao.DaoUser;
import dao.DaoPersonagens;
import dao.DaoToken;
import emailsender.Sender;
import entaties.Player;

public class Users implements Runnable{
	
	public DataOutputStream out;
	public DataInputStream in;
	int id;
	int playerid;
	Thread thread = null;
	DaoUser daou;
	DaoToken daot;
	DaoPersonagens daop;
	Sender send;
	private int idBD;
	private Game game;
	
	public Player player;
	
	
	
	public Users(DataOutputStream out, DataInputStream in) {
		daou = new DaoUser();
		daop = new DaoPersonagens();
		this.out = out;
		this.in = in;
		send = new Sender();
		game = new Game(in, out);
	}
	
	public void setPlayerid(int playerid) {
		this.playerid = playerid;
		game.setPlayerid(playerid);
	}
	
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	public void run() {
		while(true) {
			try {
				boolean fechar = false;
				String email, result;
				switch(in.readInt()) {
					case 0:
						// enviou um char, iniciar jogo
						int pos = Server.tem_espaco();
						if (pos > -1) {// se puder entrar no servidor
							out.writeBoolean(true);
							int k = in.readInt();
							String nickname = in.readUTF();
							if (daop.logar_deslogar_personagem(k, nickname, true)) {
								int idchar = daop.pegar_id_personagem(nickname);
								if (idchar != 0) {
									player = new Player(pos, nickname);
									out.writeBoolean(true);
									out.writeInt(pos);
									out.writeInt(idchar); // enviando o id do char no banco de dados
									int[] coisas = daop.pegar_coisas_para_o_cliente(idchar);
									for (int bd : coisas) {
										out.writeInt(bd);
									}
									Server.entrar_servidor(this, pos);
									game.start(idchar);
								}else {
									out.writeBoolean(false);
								}
								daop.logar_deslogar_personagem(k, nickname, false);
							}
						}else {
							out.writeBoolean(false);
						}
						
						break;
					case 1:
						// recebido um login e uma senha
						idBD = realizar_login(in.readUTF(), in.readUTF());
						out.writeInt(idBD);
						if (idBD > 0) { // se tiver encontrado a conta (login e senha corretos!)
							// mandar os chars para o usuário
							ArrayList<String> chars = daop.pegar_personagens_de_uma_conta(idBD);
							out.writeInt(chars.size());
							for (String nick : chars) {
								out.writeUTF(nick);
							}
						}
						break;
					case 2:
						// enviando informações para criação de conta
						System.out.println("pq estou aqui?");
						out.writeBoolean(daou.criar_usuario(in.readUTF(), in.readUTF(), in.readUTF()));
						break;
					case 3:
						// Pedindo para criar um novo personagem!
						out.writeInt(daop.criar_personagem(in.readInt(), in.readUTF()));
						break;
					case -1:
						// jogador fechou o jogo, encerra-se a thread
						Server.jogadores_conectados--;
						Server.user[playerid] = null;
						fechar = true;
						if (idBD > 0) {
							// se tiver um usuário logado, deslogá-lo
							daou.logar_deslogar(idBD, false);
						}
						break;
					case -2:
						// Jogador está desejando fazer um cadastro
						email = in.readUTF();
						out.writeUTF(criar_token_registro(email));
						break;
					case -3:
						// Jogador esqueceu a senha ou está pedindo o token para fazer o cadastro
						email = in.readUTF();
						result = nova_senha(email);
						out.writeUTF(result);
						break;
					case -4:
						// foi enviado um token
						String token = in.readUTF();
						int r = (tokenExiste(token));
						out.writeInt(r);
						if (r != 0) {
							DaoToken daot = new DaoToken();
							daot.deletar_token(token);
						}
						break;
					case -5:
						// enviando um email e uma nova senha
						out.writeBoolean(daou.set_password_by_login(in.readUTF(), in.readUTF()));
						break;
				}
				if (fechar) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Server.user[playerid] = null;
				break;
			}
		}
	}
	
	private String criar_token_registro(String email) {
		/*
		if (isValidEmailAddress(email)) {
			return "O e-mail não existe";
		}
		//*/
		String token = gerarToken();
		DaoToken dao = new DaoToken();
		dao.insert_token(token, false);
		return enviar_email(email, token, "", false);
	}
	private String enviar_email(String email, String token, String user, boolean redefinir) {
		try {
			send.enviar(email, token, user, redefinir);
			return "e-mail enviado";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			if (e.getMessage().equalsIgnoreCase("Invalid Addresses")) {
				return "E-mail Inválido";
			}
		}
		return "e-mail não enviado";
	}
	private int realizar_login(String email, String senha) {
		DaoUser dao = new DaoUser();
		return dao.logar(email, senha);
	}
	public static boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
	
	
	
	
	private String nova_senha(String email) {
		/*
		if (isValidEmailAddress(email)) {
			return "O e-mail não existe";
		}
		//*/
		if (daou.email_cadastrado(email)) {
			DaoUser dao = new DaoUser();
			String token = gerarToken();
			DaoToken daot = new DaoToken();
			daot.insert_token(token, true);
			return enviar_email(email, token, dao.get_login_by_email(email), true);
		}else {
			return "E-mail não cadastrado no sistema";
		}
	}
	private String gerarToken() {
		Random rand = new Random();
		String retorno;
		char[] letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		do {
			retorno = "";
			int k = rand.nextInt(250);
			for (int i = 0; i < k; i++) {
				retorno += letras[rand.nextInt(letras.length)];
			}
		} while (tokenExiste(retorno) > 0);
		return retorno;
	}
	private int tokenExiste(String token) {
		DaoToken dao = new DaoToken();
		return dao.validaToken(token);
	}
}
