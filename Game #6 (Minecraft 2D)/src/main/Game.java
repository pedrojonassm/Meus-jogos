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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import entities.*;
import graficos.*;
import graficos.UI;
import world.Camera;
import world.Mundo;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener, MouseWheelListener{

	private static final long serialVersionUID = 1L;
	private Thread thread;
	private boolean isRunning = true;
	public static JFrame frame;
	public static final int SCALE = 2, WIDTH = 320, HEIGHT = 240, TS = 16;
	
	private BufferedImage image;
	
	public static int nivel;
	public static List<Entity> entities;
	public static List<Enemy> inimigos;
	public static EnemySpawn spawnEnemy;
	
	public static Inventory inventario;
	
	
	public static Spritesheet spritesheet;
	public static Mundo world;
	public static Player player;
	public static int pontos;
	public static Menu menu;
	public static String GameState = "menu";
	private static String novorank = "";
	
	public static double amountOfTicks = 60.0;
	
	public UI ui;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		Sons.fundo.loop();
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		inimigos = new ArrayList<Enemy>();
		spawnEnemy = new EnemySpawn();
		menu = new Menu();
		player = new Player(0,0,TS,TS,1,null);
		world = new Mundo("/level1.png");
		ui = new UI();
		
		inventario = new Inventory();
		
		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Plataforma");
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
		if (GameState == "normal") {
			world.tick();
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			spawnEnemy.tick();
			//Mundo.restartGame("level1");
			inventario.tick();
			ui.tick();
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
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		world.render(g);
		if(GameState == "normal" || GameState == "pause") {
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
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		
		inventario.render(g);
		
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
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
				//System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (GameState == "pause") {
				GameState = "normal";
			}else {
				GameState = "pause";
			}
		}
		else if (GameState == "normal") {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
					e.getKeyCode() == KeyEvent.VK_D){
				player.right = true;
				player.moved = true;
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A){
				player.left = true;
				player.moved = true;
			}else if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W){
				player.up = true;
				player.moved = true;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
				player.moved = true;
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.salto = true;
			}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				player.attack = true;
			}
		}else if(GameState == "menu") {
			Sons.blip_Select.play();
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
		}
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
			player.right = false;
			player.lk = 'r';
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			player.left = false;
			player.lk = 'l';
		}else if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			player.up = false;
			player.lk = 'u';
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;	
			player.lk = 'd';
		}else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.salto = false;
		}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			player.attack = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		inventario.mousePressed = true;
		inventario.mx = (int) (e.getX()/SCALE);
		inventario.my = (int) (e.getY()/SCALE);
		if (e.getButton() == MouseEvent.BUTTON1) {
			inventario.mouseAction = "destroy";
		}else if (e.getButton() == MouseEvent.BUTTON3) {
			inventario.mouseAction = "use";
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		inventario.mousePressed = false;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		inventario.mx = (int) (e.getX()/SCALE);
		inventario.my = (int) (e.getY()/SCALE);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() > 0) {
			inventario.rodaUp = true;
		}else if (e.getWheelRotation() < 0) {
			inventario.rodaDown = true;
		}
		
	}

	
}
