package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Inventario.janelaBag;
import database.SingleConnection;
import entities.Entity;
import entities.Player;
import graficos.Ui;
import itens.Item;
import world.Camera;
import world.World;

public class Client extends Canvas implements Runnable, KeyListener, MouseListener, MouseWheelListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private JLabel login, password;
	private JTextField input_login;
	private JPasswordField input_password;
	private JButton logar, registrar, esquerda, direita, esquecer_senha;
	
	private Rectangle auto_login;
	private final String auto_login_text = "entrar automaticamente";
	private boolean selected = false;
	
	public static final int TS = 64, TI = 32; // Tamanho Sprites, Tamanho Itens
	public static int WIDTH, HEIGHT;
	
	private static Rectangle text_box, button_box;
	
	private BufferedImage image;
	
	public static Client instance;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	
	boolean isRunning = true;
	
	private static final String ip = "localhost";
	private static final int port = 7777, close_connection = 50*60; // vc pode ficar quantos segundos afk? 
	
	private int last_input = 0;
	private long last_time; 
	private boolean playing, control, shift;
	
	private int playerid;
	
	public static Player[] players;
	public int max_players, jogadores_conectados;
	
	public Rectangle quadrado;
	
	private ArrayList<String> personagens;
	private int index_personagem, accountID;
	
	private String action = "login",// ele vai determinar que tela deve aparecer, a do jogo, recuperação de conta ou registro
			email_cadastro;
	
	public static Output output;
	public Ui ui;
	
	// Entidades
	public Player player;
	public static SpriteSheets spritesSheets;
	public static World mundo;
	
	public janelaBag movendo_janela = null; // movendo uma bag aberta de lugar
	public Item item_segurando = null;
	private Rectangle pego;
	
	public Client(){
		pego = new Rectangle (0,0,2,2);
		quadrado = new Rectangle(0,0, 2, 2);
		control = shift = false;
		spritesSheets = new SpriteSheets();
		instance = this;
		text_box = new Rectangle(100, 20);
		button_box = new Rectangle(90, 30);
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		WIDTH = 1240;
		HEIGHT = 720;
		setPreferredSize( new Dimension(WIDTH, HEIGHT));
		ui = new Ui();
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (out != null) {
						if (playing) {
							out.writeInt(-1); // deslogar char
						}
						out.writeInt(-1);// desconectar a conta
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				super.windowClosing(e);
			}
		});
		if(conectar()){
			autoLogin();
		}
	}
	
	private void autoLogin() {
		// verifica se tem um login salvo no banco de dados local
		String[] auto_login = SingleConnection.pegar_email_senha();
		if (auto_login != null) {
			realizar_login(auto_login[0], auto_login[1]);
		}
	}

	private void criar_botoes() {
		ui.caixinhaQuantidade.adicionar_no_frame(frame);
		
		login = new JLabel();
		input_login = new JTextField();
		logar = new JButton();
		registrar = new JButton();
		password = new JLabel();
		input_password = new JPasswordField();
		esquerda = new JButton();
		direita = new JButton();
		esquecer_senha = new JButton();
		
		botoes_login();
		
		setar_posicoes();
		add_eventos_botoes();
		frame.add(login);
		frame.add(input_login);
		
		frame.add(password);
		frame.add(input_password);
		
		frame.add(logar);
		frame.add(registrar);
		frame.add(esquerda);
		frame.add(direita);
		frame.add(esquecer_senha);
	}
	
	private void botoes_login() {
		botoes_off();
		esquerda.setVisible(false);
		direita.setVisible(false);
		action = "login";
		input_login.setText("");
		input_password.setText("");
		login.setText("Insira o login: ");
		logar.setText("logar");
		registrar.setText("registrar");
		password.setText("Insira a senha: ");
		esquecer_senha.setText("Esqueci a senha");
		login.setVisible(true);
		logar.setVisible(true);
		registrar.setVisible(true);
		password.setVisible(true);
		esquecer_senha.setVisible(true);
		input_login.setVisible(true);
		input_password.setVisible(true);
	}
	
	private void botoes_registrar() {
		botoes_off();
		action = "registrar";
		input_login.setVisible(true);
		input_login.setText("");
		login.setText("Insira aqui seu e-mail");
		login.setVisible(true);
		logar.setText("voltar");
		logar.setVisible(true);
		registrar.setVisible(true);
	}
	
	private void botoes_esquecer_senha() {
		botoes_off();
		action = "recuperar conta";
		login.setText("Insira seu e-mail de recuperação aqui: ");
		login.setVisible(true);
		logar.setVisible(true);
		logar.setText("voltar");
		logar.setVisible(true);
		esquecer_senha.setVisible(true);
		esquecer_senha.setText("enviar");
		input_login.setVisible(true);
		registrar.setVisible(true);
	}
	
	protected void botoes_token() {
		botoes_off();
		action = "token";
		esquecer_senha.setText("enviar");
		esquecer_senha.setVisible(true);
		input_login.setVisible(true);
		login.setVisible(true);
		input_login.setText("");
		login.setText("Insira o token aqui: ");
	}


	private void botoes_setar_nova_senha() {
		botoes_off();
		action = "recuperar a senha";
		login.setText("coloque seu login");
		input_login.setText("");
		password.setVisible(true);
		password.setText("insira a nova senha");
		input_password.setText("");
		input_password.setVisible(true);
		esquecer_senha.setText("enviar");
	}

	private void botoes_verdadeiro_registro() {
		botoes_off();
		action = "verdadeiro registro";
		login.setText("Insira seu login");
		login.setVisible(true);
		input_login.setText("");
		input_login.setVisible(true);
		password.setText("Insira sua senha");
		password.setVisible(true);
		input_password.setText("");
		input_password.setVisible(true);
		registrar.setVisible(true);
	}
	private void botoes_entrar_com_personagem() {
		botoes_off();
		action = "logar_personagem";
		registrar.setVisible(true);
		registrar.setText("novo");
		esquecer_senha.setVisible(true);
		esquecer_senha.setText("voltar");
		index_personagem = 0;
		if (personagens.size() > 0) {
			logar.setVisible(true);
			logar.setText("entrar");
			password.setVisible(true);
			password.setText(personagens.get(index_personagem));
			esquerda.setText("previus");
			direita.setText("next");
			esquerda.setVisible(true);
			direita.setVisible(true);
		}
	}
	
	private void botoes_criar_personagem() {
		botoes_off();
		action = "criar_personagem";
		esquecer_senha.setText("voltar");
		esquecer_senha.setVisible(true);
		input_login.setVisible(true);
		login.setVisible(true);
		login.setText("Insira o nome do seu personagem");
		registrar.setVisible(true);
		registrar.setText("enviar");
	}
	
	private void botoes_off() {
		// tudo fica invisível
		esquecer_senha.setVisible(false);
		esquerda.setVisible(false);
		direita.setVisible(false);
		input_login.setVisible(false);
		input_password.setVisible(false);
		logar.setVisible(false);
		login.setVisible(false);
		registrar.setVisible(false);
		password.setVisible(false);
	}
	
	public void setar_posicoes() {
		login.setBounds(WIDTH/2-text_box.width/2, HEIGHT/4, text_box.width*3, text_box.height);
		input_login.setBounds(WIDTH/2-text_box.width/2, HEIGHT/4+text_box.height, text_box.width, text_box.height);
		
		password.setBounds(WIDTH/2-text_box.width/2, HEIGHT/3, text_box.width*3, text_box.height);
		input_password.setBounds(WIDTH/2-text_box.width/2, HEIGHT/3+text_box.height, text_box.width, text_box.height);
		
		logar.setBounds(WIDTH/2-button_box.width, HEIGHT/2, button_box.width, button_box.height);
		registrar.setBounds(WIDTH/2, HEIGHT/2, button_box.width, button_box.height);
		
		esquecer_senha.setBounds(WIDTH/2-button_box.width, HEIGHT/2+button_box.height, button_box.width*2, button_box.height);
		
		esquerda.setBounds(WIDTH/2-text_box.width*2, HEIGHT/3, text_box.width, text_box.height);
		direita.setBounds(WIDTH/2+text_box.width, HEIGHT/3, text_box.width, text_box.height);
		
		auto_login = new Rectangle(WIDTH/2-text_box.width/2, HEIGHT/3+text_box.height*3, 10, 10);
	}
	
	private void add_eventos_botoes() {
		logar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				last_input = 0;
				if (action.equalsIgnoreCase("login")) {
					// Enviar o texto do login e da senha
					if (input_login.getText().length() > 0 && input_password.getPassword().length > 0) {
						String pass = "";
						for (char c : input_password.getPassword()) {
							pass+=c;
						}
						realizar_login(input_login.getText(), pass);
					}else {
						JOptionPane.showMessageDialog(null, "Preencha tudo corretamente!");
					}
				}else if(action.equalsIgnoreCase("logar_personagem")){
					iniciar_game(password.getText());
				}else {
					botoes_login();
				}
			}
		});
		
		registrar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				last_input = 0;
				if (action.equalsIgnoreCase("registrar")) {
					if (input_login.getText().length() > 0) {
						email_cadastro = input_login.getText();
						String sucesso = registrar(email_cadastro);
						if (sucesso.equalsIgnoreCase("e-mail enviado")) {
							// criar os botões para receber o token e realizar o verdadeiro registro
							JOptionPane.showMessageDialog(null, "um e-mail com o token foi enviado para o seu e-mail\n"
									+ "Este e-mail terá um token de accesso para você registrar o seu login, senha, etc.\n"
									+ "Você deve Inserir o token na nova caixa de texto que aparecer");
							
							botoes_token();
						}else {
							JOptionPane.showMessageDialog(null, sucesso);
						}
					}else {
						JOptionPane.showMessageDialog(null, "Preencha tudo corretamente!");
					}
				}else if(action.equalsIgnoreCase("verdadeiro registro")){
					if (input_login.getText().length() > 0 && input_password.getPassword().length > 0) {
						String pass = "", login2 = input_login.getText();
						for (char c : input_password.getPassword()) {
							pass+=c;
						}
						verdadeiro_cadastro(login2, pass);
					}
				}else if(action.equalsIgnoreCase("logar_personagem")){
					botoes_criar_personagem();
				}else if(action.equalsIgnoreCase("criar_personagem")){
					if (input_login.getText().length() > 0) {
						criar_personagem(input_login.getText());
					}
				}else {
					botoes_registrar();
				}
			}
		});
		
		esquecer_senha.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				last_input = 0;
				if (action.equalsIgnoreCase("recuperar conta")) {
					if (input_login.getText().length() > 0) {
						String str = recuperar_conta(input_login.getText());
						if  (str.equalsIgnoreCase("e-mail enviado")) {
							JOptionPane.showMessageDialog(null, "Foi enviado um token de acesso para você\n"
									+ "Na tela seguinte, você deverá inserí-lo, se for confirmado você irá poder redefinir a sua senha");
							botoes_token();
						}else {
							JOptionPane.showMessageDialog(null, str);
						}
					}else {
						JOptionPane.showMessageDialog(null, "Preencha tudo corretamente!");
					}
				}else if(action.equalsIgnoreCase("token")){
					// enviar token para o servidor e ver se deu tudo certo
					if (input_login.getText().length() > 0) {
						token(input_login.getText());
					}else {
						JOptionPane.showMessageDialog(null, "Preencha tudo corretamente!");
					}
				}else if(action.equalsIgnoreCase("recuperar a senha")){
					if (input_login.getText().length() > 0 && input_password.getPassword().length > 0) {
						String pass = "";
						for (char c : input_password.getPassword()) {
							pass+=c;
						}
						resetar_senha(input_login.getText(), pass);
					}else {
						JOptionPane.showMessageDialog(null, "Preencha tudo corretamente!");
					}
				}else if(action.equalsIgnoreCase("logar_personagem")){
					botoes_login();
					SingleConnection.clear_table();
				}else if(action.equalsIgnoreCase("criar_personagem")){
					botoes_entrar_com_personagem();
				}else {
					botoes_esquecer_senha();
				}
			}
		});
		esquerda.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				index_personagem--;
				if (index_personagem < 0) {
					index_personagem = personagens.size()-1;
				}
				password.setText(personagens.get(index_personagem));
			}
		});
		direita.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				index_personagem++;
				if (index_personagem >= personagens.size()) {
					index_personagem = 0;
				}
				password.setText(personagens.get(index_personagem));
			}
		});
		
	}

	protected void criar_personagem(String nickname) {
		try {
			out.writeInt(3);
			out.writeInt(accountID);
			out.writeUTF(nickname);
			switch (in.readInt()) {
			case 1:
				personagens.add(nickname);
				botoes_entrar_com_personagem();
				JOptionPane.showMessageDialog(null, "Personagem criado com sucesso!");
				break;
			case -1:
				JOptionPane.showMessageDialog(null, "Alguém já possui um personagem com esse nome!");
				break;
			case 0:
				JOptionPane.showMessageDialog(null, "Algo deu errado, tente novamente mais tarde!");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void verdadeiro_cadastro(String login2, String pass) {
		try {
			out.writeInt(2);
			out.writeUTF(email_cadastro);
			out.writeUTF(login2);
			out.writeUTF(pass);
			if (in.readBoolean()) {
				JOptionPane.showMessageDialog(null, "cadastro realizado com sucesso!");
			}
			email_cadastro = "";
			botoes_login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void resetar_senha(String login, String senha) {
		try {
			out.writeInt(-5);
			out.writeUTF(login);
			out.writeUTF(senha);
			if  (in.readBoolean()) {
				botoes_login();
			}else {
				JOptionPane.showMessageDialog(null, "Deu algo errado, tipo você ter digitado o login errado");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void token(String token) {
		try {
			out.writeInt(-4);
			out.writeUTF(token);
			int k = in.readInt();
			if (k == 2) {
				botoes_setar_nova_senha();
			}else if (k == 1){
				botoes_verdadeiro_registro();
			}else {
				JOptionPane.showMessageDialog(null, "Esse token não é valido");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	protected String registrar(String email) {
		String retorno = "";
		try {
			out.writeInt(-2);
			System.out.println("Mensagem enviada?");
			out.writeUTF(email);
			retorno = in.readUTF();
		} catch (IOException e) {
			retorno = "Não foi possível enviar uma mensagem para o servidor";
			e.printStackTrace();
		}
		return retorno;
		
	}

	protected String recuperar_conta(String email) {
		// pedir para o servidor enviar para o e-mail uma senha nova
		
		String retorno = "Não foi possível enviar o e-mail para o servidor";
		try {
			out.writeInt(-3);
			out.writeUTF(email);
			retorno = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	protected void realizar_login(String text, String password2) {
		try {
			out.writeInt(1);
			out.writeUTF(text);
			out.writeUTF(password2);
			// receber resposta de login aqui
			accountID = in.readInt();
			if (accountID == 0) {
				// login incorreto
				JOptionPane.showMessageDialog(null, "login e/ou senha incorretos");
				return;
			}else if (accountID == -1) {
				// Usuário logado
				JOptionPane.showMessageDialog(null, "Esse usuário já está logado");
				return;
			}
			// ativar auto login
			if (selected) {
				SingleConnection.salvar_email_senha(text, password2);
			}
			// receber chars aqui
			JOptionPane.showMessageDialog(null, "logado");
			personagens = new ArrayList<String>();
			int total_personagens = in.readInt();
			for (int i = 0; i < total_personagens; i++) {
				String numero = in.readUTF();
				personagens.add(numero);
			}
			botoes_entrar_com_personagem();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private boolean conectar() {
		try {
			socket = new Socket(ip, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream()); 
			max_players = in.readInt();
			
			output = new Output(out);
			jogadores_conectados = in.readInt();
			mundo = new World("/world.png");
			players = new Player[max_players];
			for (int i = 0; i < max_players; i++) {
				Player p = new Player();
				p.atualizar_dados(0, 0, 0, 100, 0, 0, 0);
				players[i] = p;
			}
			playing = false;
			Thread thread2 = new Thread(this);
			thread2.start();
			return true;
		} catch (UnknownHostException e1) {
			
		} catch (IOException e1) {
			if (e1.getMessage().equalsIgnoreCase("Connection refused: connect")) {
				JOptionPane.showMessageDialog(null, "Erro de conexão!\n Verifique se a porta e o ip estão corretos, se estiverem, o servidor está fechado.\nAlgum bug, sorry!");
			}
		}
		return false;
	}
	
	
	private void iniciar_game(String nick) {
		try {
			out.writeInt(0);
			if (!in.readBoolean()) {
				// servidor cheio
				JOptionPane.showMessageDialog(null, "O servidor está cheio!");
				return;
			}
			player = new Player();
			out.writeInt(accountID);
			out.writeUTF(nick);
			playing = in.readBoolean();
			playerid = in.readInt();
			players[playerid] = player;
			player.setPlayerid(playerid);
			player.setEntityIdBD(in.readInt()); // ultima coisa que recebeu nos Users
			player.receber_coisas(in, out);
			mundo.receber_itens_no_chao(in);
			if (playing) {
				Input input = new Input(in);
				Thread thread = new Thread(input);
				player.setName(nick);
				thread.start();
				botoes_off();
				Output.enviar_player(player);
			}else {
				JOptionPane.showMessageDialog(null, "algo deu errado!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Client();
	}
	
	public void initFrame(){
		frame = new JFrame("Cliente");
		criar_botoes();
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void render2() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0xEEEEEE));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if(action.equalsIgnoreCase("login")) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", 15, 15));
			g.drawString(auto_login_text, auto_login.x+auto_login.width*2, auto_login.y+auto_login.height*2);
			g.drawRect(auto_login.x, auto_login.y, auto_login.width, auto_login.height);
			if (selected) {
				g.fillRect(auto_login.x, auto_login.y, auto_login.width, auto_login.height);
			}
		}
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,getWidth(),getHeight(),null);
		//g.drawImage(image.getSubimage(auto_login.x, auto_login.y, auto_login.width, auto_login.height), auto_login.x, auto_login.y, auto_login.width, auto_login.height,null);
		bs.show();
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		/*
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,getWidth(),getHeight());
		*/
		mundo.render(g);
		
		player.render(g);
		
		g.setColor(Color.red);
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && (players[i].getX() != 0 && players[i].getY() != 0)) {
				players[i].render(g);
			}
		}
		
		g.fillRect(quadrado.x, quadrado.y, 2, 2);
		g.drawRect(((int) (quadrado.x>>5))<<5, ((int) (quadrado.y>>5))<<5, TI, TI);
		
		ui.render(g);
		
		if (item_segurando != null) {
			item_segurando.render(quadrado.x-TI/2, quadrado.y-TI/2, g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,getWidth(),getHeight(),null);
		bs.show();
	}
	
	public void updateCoordinates (int pid, int x2, int y2, int z2, int life2, int state2, int index2, int skin2) {
		
		// atualiza a posição dos outros jogadores
		//System.out.println("pid: "+pid+" x2: "+x2+" y2: "+y2+" z2: "+z2);
		players[pid].atualizar_dados(x2, y2, z2, life2, state2, index2, skin2);
	}
	
	public void tick() {
		player.tick();
		
		if (movendo_janela != null) {
			movendo_janela.mover();
		}
		
		ui.tick();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / 60; // 60 = Fps max
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			if (playing) {
				long now = System.nanoTime();
				delta+= (now - lastTime) / ns;
				lastTime = now;
				if(delta >= 1) {
					tick();
					render();
					frames++;
					delta -= (int) delta;
				}
				
				if(System.currentTimeMillis() - timer >= 1000){
					//System.out.println("FPS: "+ frames);
					frames = 0;
					timer+=1000;
				}
			}else {
				// renderizar o auto login
				render2();
			}
			if (System.currentTimeMillis() - last_time >= 1000) {
				last_time = System.currentTimeMillis();
				last_input++;
				if (last_input > close_connection) {
					try {
						if (playing) {
							out.writeInt(-1);
						}
						out.writeInt(-1);
						JOptionPane.showMessageDialog(null, "Demorou demais!\n você foi desconectado.");
						break;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}

	public void keyTyped(KeyEvent e) {	}
	
	public boolean getControl() {
		return control;
	}
	
	public boolean getShift() {
		return shift;
	}
	
	public Rectangle getQuadrado() {
		return quadrado;
	}

	public void keyPressed(KeyEvent e) {
		last_input = 0;
		
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			control = true;
		}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = true;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = true;
		}
	
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			control = false;
		}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		}else {
			if (control) {
				if (e.getKeyCode() == KeyEvent.VK_I) {
					player.abrirInventario();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				player.left = false;
			}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.right = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				player.up = false;
			}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				player.down = false;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
		last_input = 0;
		//e.getButton(); 1 = Left click, 2 = middle click, 3 = right click
		if (playing) {
			if(player.clicou_inventario(quadrado.x, quadrado.y, e.getButton())) {
				return;
			}else if (e.getButton() == 1 && ui.caixinhaQuantidade.clicou(e.getX(), e.getY())) {
				return;
			}else if ( e.getButton() == 1 && mundo.pegou_item(((int) ((e.getX()+Camera.x)>>5))<<5, ((int) ((e.getY()+Camera.y)>>5))<<5)) {
				return;
			}
		}else {
			if (auto_login.contains(e.getX(), e.getY())) {
				selected = !selected;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			ui.caixinhaQuantidade.parar_de_seguir_o_mouse();
			movendo_janela = null;
			if (item_segurando != null) {
				if (Entity.distancia(quadrado.x, pego.x, quadrado.y, pego.y) > TI) {
					if (control) {
						player.mover_item(quadrado.x, quadrado.y, item_segurando);
					}else if(shift){
						player.mover_quantidade_x_de_item(quadrado.x, quadrado.y, item_segurando, 1);
					}else {
						// aparecer a caixinha para selecionar a quantidade que vai mover
						ui.caixinhaQuantidade.setItem(item_segurando, quadrado.x, quadrado.y);
					}
				}
				item_segurando = null;
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		last_input = 0;
		// e.getWheelRotation(); -1 girar pra cima e 1 girar pra baixo
		player.descer_subir_bag(quadrado.x, quadrado.y, e.getWheelRotation());
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		quadrado.x = e.getX();
		quadrado.y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		quadrado.x = ((int) (e.getX()/TI))*TI;
		quadrado.y = ((int) (e.getY()/TI))*TI;
		//*/
		quadrado.x = e.getX();
		quadrado.y = e.getY();
	}

	public void setar_item_segurado(Item item) {
		item_segurando = item;
		pego.x = quadrado.x;
		pego.y = quadrado.y;
		if (item.getBag().equals(World.bag)) {
			pego.x = -1 - Client.TS;
			pego.y = -1 - Client.TS;
		}
	}
}
