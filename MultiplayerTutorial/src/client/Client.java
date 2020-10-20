package client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;

import server.Server;

public class Client extends Canvas implements Runnable, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static final int SCALE = 2, WIDTH = 320, HEIGHT = 240, TS = 16;
	
	private BufferedImage image;
	
	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	
	boolean isRunning = true;
	
	static Client cliente;
	
	int playerid;
	int[] x = new int[10], y = new int[10];
	boolean left, right, down, up;
	int playerx, playery;
	
	public Client() throws Exception {
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

		socket = new Socket(Server.ip ,Server.port);
		in = new DataInputStream(socket.getInputStream());
		playerid = in.readInt();
		out = new DataOutputStream(socket.getOutputStream());
		Input input = new Input(in, this);
		Thread thread = new Thread(input);
		thread.start();
		Thread thread2 = new Thread(this);
		thread2.start();
	}
	
	public static void main(String[] args) throws Exception {
		cliente = new Client();
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

	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		g.setColor(Color.blue);
		for (int i = 0; i < 10; i++) {
			g.fillRect(x[i], y[i], 10, 10);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		bs.show();
	}
	
	public void updateCoordinates (int pid, int x2, int y2) {
		x[pid] = x2;
		y[pid] = y2;
	}
	
	public void tick() {
		if (left) {
			playerx--;
		}else if (right) {
			playerx++;
		}if (up) {
			playery--;
		}else if (down) {
			playery++;
		}
		
		if (left || right || down || up) {
			x[playerid] = playerx;
			y[playerid] = playery;
			try {
				out.writeInt(playerid);
				out.writeInt(playerx);
				out.writeInt(playery);
			} catch (IOException e) {
				System.out.println("Não foi possivel enviar as coordenadas");
			}
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / 60; // 60 = ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(true){
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
	}

	@Override
	public void keyTyped(KeyEvent e) {	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = true;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = true;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;
		}
	}
	
	
	
}
