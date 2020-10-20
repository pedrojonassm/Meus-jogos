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
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int SCALE = 2, WIDTH = 320, HEIGHT = 240, TS = 16;
	
	private BufferedImage image;
	
	public static int nivel;
	public static List<Entity> entities;
	//public static List<Enemy> inimigos;
	
	public static Inventory inventario;
	public static Network network;
	public static Spritesheet spritesheet;
	public static Mundo world;
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
		network = new Network();
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		//inimigos = new ArrayList<Enemy>();
		menu = new Menu();
		world = new Mundo("/level1.png");
		ui = new UI();
		
		inventario = new Inventory();
		
		
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
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			//Mundo.restartGame("level1");
			network.tick();
			inventario.tick();
			ui.tick();
			Camera.tick();
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
		if(GameState == "normal") {
			/*Renderização do jogo*/
			//Graphics2D g2 = (Graphics2D) g;
			Collections.sort(entities,Entity.nodeSorter);
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.render(g);
			}
			network.render(g);
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
			if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
					e.getKeyCode() == KeyEvent.VK_D){
				Camera.right = true;
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A){
				Camera.left = true;
			}else if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W){
				Camera.up = true;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				Camera.down = true;
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				network.parar();
			}else if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				network.reset();
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
			Camera.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
			Camera.left = false;
		}else if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
			Camera.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			Camera.down = false;	
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
		inventario.mx = (int) (e.getX()/SCALE) + Camera.x;
		inventario.my = (int) (e.getY()/SCALE) + Camera.y;
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
		if (inventario.mousePressed) {
			inventario.mx = (int) (e.getX()/SCALE) + Camera.x;
			inventario.my = (int) (e.getY()/SCALE) + Camera.y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			inventario.rodaUp = true;
		}else if (e.getWheelRotation() > 0) {
			inventario.rodaDown = true;
		}
		
	}

	
}
