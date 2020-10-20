package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.*;
import graficos.*;
import graficos.UI;
import world.Camera;
import world.Mundo;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static int WIDTH = 480, HEIGHT = 320, SCALE = 2, TS = 16;
	
	private BufferedImage image, fundo;
	
	public static int nivel;
	
	public static List<Entity> entities;
	public static List<Gerador> planetas;
	public static List<Nave> amarelo, verde, vermelho, azul, rosa;
	public static List<Meteor> meteoros;
	public static List<disparoLaser> lasers;
	public static Spritesheet spritesheet, naves;
	public static Mundo world;
	public static int pontos = 0, cx, cy, times = 1;
	public static Menu menu;
	public static String GameState = "menu";
	private static String novorank = "";
	public static Player player;
	
	public UI ui;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		try {
			fundo = ImageIO.read(getClass().getResource("/fundo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		naves = new Spritesheet("/naves.png");
		entities = new ArrayList<Entity>();
		meteoros = new ArrayList<Meteor>();
		lasers = new ArrayList<disparoLaser>();
		azul = new ArrayList<Nave>();
		verde = new ArrayList<Nave>();
		vermelho = new ArrayList<Nave>();
		amarelo = new ArrayList<Nave>();
		rosa = new ArrayList<Nave>();
		//super(x, y, width, height, speed, sprite);
		player = new Player(TS, TS, TS, TS, 3, spritesheet.getSprite(2*TS, 0*TS, TS, TS));
		Game.entities.add(player);
		planetas = new ArrayList<Gerador>();
		menu = new Menu();
		world = new Mundo("/level1.png");
		ui = new UI();
		
		
	}
	
	public void initFrame(){
		frame = new JFrame("Sem nome");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		int fim = 0;
		for (int i = 0; i < planetas.size(); i++) {
			if (planetas.get(i).destruido) {
				fim++;
			}
		}
		if (fim == 5) {
			GameState = "rank";
		}
		if (GameState == "normal") {
			if (meteoros.size() < 150 && Entity.rand.nextInt(100) <= 2) {
				boolean c = true;
				Meteor m = new Meteor(Entity.rand.nextInt(3000), Entity.rand.nextInt(3000), 16, 16, 1, null);
				for (int i = 0; i < planetas.size(); i++) {
					Gerador g = planetas.get(i);
					if (m.distancia(m.getX(), m.getY(), g.getX(), g.getY()) < 100 && m.getX() > 16 && m.getY() > 16 && m.getY() < 2950 && m.getX() < 2950) {
						c = false;
					}
				}
				if (c) {
					Game.meteoros.add(m);
					Game.entities.add(m);
				}
			}
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for(int i = 0; i < lasers.size(); i++) {
				disparoLaser e = lasers.get(i);
				e.tick();
			}
		}else if(GameState == "menu") {
			menu.tick();
		}
	}
	


	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.drawImage(fundo, 0, 0, WIDTH, HEIGHT, Camera.x, Camera.y, (WIDTH+Camera.x), (HEIGHT+Camera.y), null);
		//g.drawImage(fundo, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		//world.render(g);
		if(GameState == "normal") {
			/*Renderização do jogo*/
			//Graphics2D g2 = (Graphics2D) g;
			Collections.sort(entities,Entity.nodeSorter);
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.render(g);
			}
		}else if(GameState == "menu") {
			menu.render(g);
		}else if(GameState == "rank") {
			ArrayList<Rank> ranks = new ArrayList<Rank>();
			int w;
			ranks = Rank.pegarRanks();
			if(ranks.size() > 0) {
				Collections.sort(ranks, Rank.nodeSorter);
				Rank atual;
				g.setColor(Color.black);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				for (int i = 0; i < ranks.size(); i++) {
					g.setColor(Color.white);
					g.setFont(new Font("Arial", Font.BOLD, 24));
					atual = ranks.get(i);
					String str = (i+1)+"--"+atual.nome +": "+ Integer.toString(atual.ponto);
					w = g.getFontMetrics().stringWidth(str);
					g.drawString(str, WIDTH/2 - w/2, (i+1)*20);
				}
			}else {
				String str = "Ainda não existem ranks nesse jogo...";
				w = g.getFontMetrics().stringWidth(str);
				g.drawString(str, WIDTH/2 - w/2, HEIGHT/2);
			}
		}else if(GameState == "novorank") {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			int w;
			w = g.getFontMetrics().stringWidth("insira seu nome abaixo");
			g.drawString("insira seu nome abaixo", WIDTH/2 - w/2, HEIGHT/2-20);
			g.setColor(Color.yellow);
			w = g.getFontMetrics().stringWidth(novorank);
			g.drawString(novorank, WIDTH/2 - w/2, HEIGHT/2);
		}
		for(int i = 0; i < lasers.size(); i++) {
			disparoLaser e = lasers.get(i);
			e.render(g);
		}
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		if (GameState == "controles") {
			g.setColor(Color.white);
			String[] str = {"As teclas que você vai usar são simplesmente essas: W, A, S, D, SHIFT, CTRL", "Vou começar pelo CTRL, para você entender as outras... Essa é a tecla para possuir inimigos!", "Chegue perto de alguém e segure ctrl, ficara evidente o inimigo selecionado", "Aperte A ou D para alternar entre as opções próximas.", "Caso você selecione o que você ja está possuindo, você sairá do inimigo, caso não selecione nada, nada acontecerá", "Caso esteja em uma nave, use SHIFT para atirar, W para avança, S freiar, e A ou D para girar", "Note que a movimentação é diferente quando se está em uma nave para quando está com outra coisa", "", "", "OBJETIVOS:", "Você só quer paz, mas tem 5 planetas guerreando próximo a você, então destrua ao menos 4 deles", "Não to zoando, o objetivo é só isso mesmo kk", "E claramente a história é similar"};
			g.setFont(new Font("Arial", 18, 18));
			for (int i = 0; i < str.length; i++) {
				int w1 = g.getFontMetrics().stringWidth(str[i]);
				g.drawString(str[i], (Game.WIDTH*Game.SCALE)/2-w1/2, 18*(i+1)+18*8);
			}
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (GameState == "normal") {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				 if (player.controle instanceof Nave) {
					 player.moved = true;
				 }else {
					 player.up = true;
				 }
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				player.atirar = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				if (player.controle instanceof Nave) {
					player.freio = true;
				}
				else {
					player.down = true;
				}
			}
			 if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				 player.atacar = true;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_A) {
				 player.left = true;
			 }else if(e.getKeyCode() == KeyEvent.VK_D) {
				 player.right = true;
			 }
		}else if(GameState == "menu") {
			if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W){
				menu.up = true;
				
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				menu.down = true;	
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu.selecionado = true;
			}
		}else if(GameState == "rank") {
			Sons.blip_Select.play();
			GameState = "menu";
		}else if(GameState == "novorank") {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Rank.novoRank(novorank, pontos);
				novorank = "";
				Mundo.restartGame("sla");
				GameState = "menu";
			}else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				novorank = novorank.replace(novorank.substring(novorank.length()-1), "");
			}else {
				novorank += e.getKeyChar();
			}
		}else if (GameState == "controles") {
			GameState = "menu";
		}
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S) {
			 player.freio = false;
			 player.down = false;
		 }
		if (e.getKeyCode() == KeyEvent.VK_W) {
			if (player.controle instanceof Nave) {
				 player.moved = false;
			}else {
				player.up = false;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			 player.atacar = false;
		 }
		if (e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}else if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
