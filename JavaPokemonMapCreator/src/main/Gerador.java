package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
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

import graficos.*;
import world.Camera;
import world.Mundo;
import world.Tile;

public class Gerador extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 1240, HEIGHT = 720, TS = 64;
	public static int FPS = 0;
	private BufferedImage image;
	
	public static int nivel;
	public static Spritesheet chaos, chaos128, paredes, paredes128, itens, itens128, escadas, escadas128;
	public static Mundo world;
	public static double amountOfTicks = 60.0;
	
	private int horizontal, vertical;
	
	private Rectangle quadrado;
	Tile escolhido;
	
	public Gerador(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		quadrado = new Rectangle(64, 64);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		//Inicializando objetos.
		chaos = new Spritesheet("/chaos64.png", 64);
		chaos128 = new Spritesheet("/chaos128.png", 128);
		paredes = new Spritesheet("/paredes64.png", 64);
		paredes128 = new Spritesheet("/paredes128.png", 128);
		itens = new Spritesheet("/itens64.png", 64);
		itens128 = new Spritesheet("/itens128.png", 128);
		escadas = new Spritesheet("/escadas64.png", 64);
		escadas = new Spritesheet("/escadas128.png", 128);
		
		world = new Mundo("/padrao.png");
	}
	
	public void initFrame(){
		frame = new JFrame("Gerador de mundo JavaPokemon");
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
		Gerador game = new Gerador();
		game.start();
	}
	
	public void tick(){
		if (Camera.x + horizontal > 0 && Camera.x + horizontal < Mundo.WIDTH*Gerador.TS - Gerador.WIDTH) Camera.x += horizontal;
		if (Camera.y + vertical > 0 && Camera.y + vertical < Mundo.HEIGHT*Gerador.TS - Gerador.HEIGHT) Camera.y += vertical;
		world.tick();
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
		
		g.setColor(Color.red);
		
		//g.drawRect(((int) (quadrado.x>>6))<<6, ((int) (quadrado.y>>6))<<6, quadrado.width, quadrado.height);
		//*
		escolhido = Mundo.pegar_chao(quadrado.x + Camera.x, quadrado.y+Camera.y);
		g.drawRect(escolhido.getX()-Camera.x, escolhido.getY()-Camera.y, quadrado.width, quadrado.height);
		//*/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH,HEIGHT,null);
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
				FPS = frames;
				frames = 0;
				timer+=1000;
			}
		}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) horizontal = 1;
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) horizontal = -1;
		if (e.getKeyCode() == KeyEvent.VK_UP) vertical = -1;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) vertical = 1;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) horizontal = 0;
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) vertical = 0;
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
	public void mouseDragged(MouseEvent e) {
		quadrado.x = e.getX();
		quadrado.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		quadrado.x = e.getX();
		quadrado.y = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	
}
