package main;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import World.Mundo;
import entities.Enemy;
import entities.Entity;
import entities.Esfera;
import entities.Player;
import entities.Weapon;
import graficos.UI;
import graficos.spriteSheet;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private Thread thread;
	private boolean isRunning = true;
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> inimigos;
	public static List<Esfera> disparos;
	public static spriteSheet spritesheet;
	public static Player player;
	public static Random rand;
	public static Mundo mundo;
	public static String GameState = "menu";
	public static int TS = 16; //tiI = tamanho dos sprites (nesse caso � 16x16)
	private int level = 0, maxLevel = 1;
	public UI ui;
	public Menu menu;
	public Sons som;
	
	public Game() {
		rand = new Random();
		Sons.music.loop();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initframe();
		ui = new UI();
		menu = new Menu();
		//iniciando objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		inimigos = new ArrayList<Enemy>();
		disparos = new ArrayList<Esfera>();
		spritesheet = new spriteSheet("/spritesheet.png");
		player = new Player(0,0,TS,TS,spritesheet.getSprite(2*TS, 0, TS, TS));
		entities.add(player);
		mundo = new Mundo("/nivel0.png");
	}
	
	public void initframe() {
		frame = new JFrame("Teste 1"); //nome que aparece no canto superior
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // o jogo fecha quando apertar em fechar
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		if(GameState == "normal") {
			for(int i = 0; i < entities.size(); i++){
				Entity e = entities.get(i);
				e.tick();
			}
			for(int i = 0; i < disparos.size(); i++){
				Entity e = disparos.get(i);
				e.tick();
			}
			if(this.inimigos.size() <= 0) {
				level++;
				if (level > maxLevel) {
					level = 0;
				}
			String nivel = "nivel"+level+".png";
				Mundo.restartGame(nivel);
			}
		}else if(GameState == "menu") {
			menu.tick();
		}
		if(disparos.size()>0) {
			Sons.esferaAndando.loop();
		}else {
			Sons.esferaAndando.stop();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3); // Sequencia de Buffers pra otimizar a renderiza��o
			return;
		}
		Graphics g = image.getGraphics();
		mundo.render(g);
		//Graphics2D g2 = (Graphics2D) g;
		for(int i = 0; i < entities.size(); i++){
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < disparos.size(); i++){
			Entity e = disparos.get(i);
			e.render(g);
		}
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 17));
		g.setColor(Color.white);
		g.drawString("muni��o: "+Player.ammo, 10, 50);
		if (GameState == "Over") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 50));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("Arial", Font.BOLD, 28));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH/2)*SCALE - 80, (HEIGHT/2)*SCALE);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString("<Pressione a tecla enter para continuar>", (WIDTH/2)*SCALE - 150, (HEIGHT/2)*SCALE+20);
		}else if(GameState == "menu") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks; // 1 segundo dividido pelo fps
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//ande para a direita
			player.right = true;
			player.lastkey = KeyEvent.VK_D;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//ande para a direita
			player.left = true;
			player.lastkey = KeyEvent.VK_A;
		}if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//ande para a cima
			player.up = true;
			if(GameState == "menu") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//ande para a baixo
			player.down = true;
			if(GameState == "menu") {
				menu.down = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_X) {
			if (player.arma && Player.ammo > 0) {
				Weapon.atirando = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameState = "menu";
			menu.pause = true;
		}else if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;
		}
		if(GameState == "Over" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			Mundo.restartGame("nivel0.png");
			GameState = "normal";
		}else if(GameState == "menu" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			menu.selecionado = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//ande para a direita
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//ande para a direita
			player.left = false;
		}if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//ande para a cima
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//ande para a baixo
			player.down = false;
		}else if(e.getKeyCode() == KeyEvent.VK_X) {
			Weapon.atirando = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(player.arma && Player.ammo > 0) {
			Weapon.atirando = true;
			Player.mdisparando = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (player.arma) {
			Weapon.atirando = false;
			player.mx = e.getX()/3;
			player.my = e.getY()/3;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
