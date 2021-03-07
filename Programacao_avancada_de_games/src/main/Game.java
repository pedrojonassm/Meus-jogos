package main;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import World.Mundo;
import entities.Bateria;
import entities.Enemy;
import entities.Entity;
import entities.Esfera;
import entities.NPC;
import entities.Player;
import entities.Weapon;
import graficos.UI;
import graficos.spriteSheet;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("exports")
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
	public static List<Bateria> baterias;
	public static spriteSheet spritesheet;
	public static Player player;
	public static Random rand;
	public static Mundo mundo;
	public static String GameState = "menu";
	public static int TS = 16; //tiI = tamanho dos sprites (nesse caso é 16x16)
	public static int level = 1, maxLevel = 2;
	public static boolean cutscene = true;
	/*public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf"); // fontes personalizadas
	public Font newFont;*/
	public UI ui;
	public Menu menu;
	public NPC npc;
	public Sons som;
	public int mx, my, xx, yy;
	
	public int[] pixels, lightmapPixels;
	public static int[] minimapaPixels;
	@SuppressWarnings("exports")
	public BufferedImage lightmap;
	public static BufferedImage minimapa;
	
	public Game() {
		/*try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(60f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}*/
		addMouseMotionListener(this);
		rand = new Random();
		Sons.music.loop();
		addKeyListener(this);
		addMouseListener(this);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));//fullscreen
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initframe();
		ui = new UI();
		menu = new Menu();
		//iniciando objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightmapPixels = new int [lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightmapPixels,0,lightmap.getWidth()); //sombra
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); //manipulando pixels.
		entities = new ArrayList<Entity>();
		inimigos = new ArrayList<Enemy>();
		disparos = new ArrayList<Esfera>();
		baterias = new ArrayList<Bateria>();
		spritesheet = new spriteSheet("/spritesheet.png");
		player = new Player(0,0,TS,TS,spritesheet.getSprite(2*TS, 0, TS, TS));
		entities.add(player);
		npc = new NPC(player.getX()+116,player.getY(),TS,TS,spritesheet.getSprite(0, 3*TS, TS, TS));
		entities.add(npc);
		mundo = new Mundo("/nivel1.png");
		minimapa = new BufferedImage(Mundo.WIDTH, Mundo.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt) minimapa.getRaster().getDataBuffer()).getData();
	}
	
	public void initframe() {
		frame = new JFrame("Jogo avançado"); //nome que aparece no canto superior
		frame.add(this);
		//aplicando fullscreen
		//frame.setUndecorated(true);//retira o minimizar, fechar janela e o maximizar
		
		Image imagem = null;
		//trocando ícone e cursor
		try {
			imagem = ImageIO.read(getClass().getResource("/nivel1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image2 = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(image2, new Point(0,0), "img");
		frame.setCursor(c); //troca o cursor
		frame.setIconImage(imagem);//troca o ícone
		
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
			if(Game.inimigos.size() <= 0) {
				level++;
				if (level > maxLevel) {
					level = 1;
				}
			String nivel = "nivel"+level+".png";
			Mundo.restartGame(nivel);
			}else if(player.ammo <= 0 && baterias.size() <= 0) {
				Mundo.maisbaterias();
			}
		}else if(GameState == "menu") {
			menu.tick();
		}else if(GameState == "entrada") {
			if(player.getX() < 100) {
				npc.setY(player.getY());
				player.setX(player.getX()+1);
			}else {				
				GameState = "normal";
				cutscene = false;
			}
		}else if(GameState == "mensagem") {
			npc.tick();
		}
		if(disparos.size()>0) {
			Sons.esferaAndando.loop();
		}else {
			Sons.esferaAndando.stop();
		}
		xx++;
	}
	
	public void drawRectangleExemple(int xoff, int yoff) {
		for(int xx = 0; xx < 32; xx++) {
			for (int yy = 0; yy<32;yy++) {
				int xOff = xx+xoff;
				int yOff = yy+yoff; //Offset
				if(xOff < 0 || yOff < 0 || xOff > WIDTH || yOff > HEIGHT) {
					continue;
				}
				pixels[xOff+(yOff*WIDTH)] = 0; //seria o mesmo que [xx][yy]; desenha um retangulo na tela;
			}
		}
	}
	
	public void applyLight() {
		for (int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightmapPixels[xx+(yy * Game.WIDTH)] == 0xFFFFFFFF) { // se for branco
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x808080, 0); //0x808080 = transparencia, basta alterar o S no Paint.net; 0 ambiente de luz padrão
					pixels[xx+(yy*Game.WIDTH)] = pixel;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3); // Sequencia de Buffers pra otimizar a renderização
			return;
		}
		Graphics g = image.getGraphics();
		mundo.render(g);
		//Graphics2D g2 = (Graphics2D) g;
		Collections.sort(entities, Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++){
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < disparos.size(); i++){
			Entity e = disparos.get(i);
			e.render(g);
		}
		applyLight();
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		//drawRectangleExemple(xx, yy);
		
		// Modo fullscreen
		//g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
		
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 17));
		g.setColor(Color.white);
		g.drawString("munição: "+player.ammo, 10, 50);
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
			player.updateCamera();
			menu.render(g);
		}
		Mundo.renderMinimap();
		g.drawImage(minimapa, WIDTH*SCALE - Mundo.WIDTH*3, 0, Mundo.WIDTH*3, Mundo.HEIGHT*3, null);
		//girando objetos
		/*Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(200+25-my, 200+25-mx);
		g2.rotate(angleMouse, 200+25, 200+25); //25 é a metade do tamanho
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 50);*/
		
		//criando fontes personalizadas
		/*g.setFont(newFont);
		g.drawString("Teste com a nova fonte", 100, 40);*/
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
	public void keyTyped(@SuppressWarnings("exports") KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(@SuppressWarnings("exports") KeyEvent e) {
		
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
				Menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//ande para a baixo
			player.down = true;
			if(GameState == "menu") {
				Menu.down = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_X) {
			if (player.arma && player.ammo > 0) {
				Weapon.atirando = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameState = "menu";
			Menu.pause = true;
		}else if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;
		}
		if(GameState == "Over" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			player.life = 100;
			player.ammo = 0;
			GameState = "normal";
			Mundo.restartGame("nivel0.png");
			GameState = "normal";
		}else if(GameState == "menu" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			Menu.selecionado = true;
		}else if(GameState == "mensagem" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			npc.mensagem = false;
			if(npc.frase < 4) {
				npc.frase++;
			}
			GameState = "normal";
		}
	}

	@Override
	public void keyReleased(@SuppressWarnings("exports") KeyEvent e) {
		
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
	public void mouseClicked(@SuppressWarnings("exports") MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(@SuppressWarnings("exports") MouseEvent e) {
		
		if(player.arma && player.ammo > 0) {
			Weapon.atirando = true;
			Player.mdisparando = true;
		}
	}

	@Override
	public void mouseReleased(@SuppressWarnings("exports") MouseEvent e) {
		
		if (player.arma) {
			Weapon.atirando = false;
			player.mx = e.getX()/3;
			player.my = e.getY()/3;
		}
	}

	@Override
	public void mouseEntered(@SuppressWarnings("exports") MouseEvent e) {
		
	}

	@Override
	public void mouseExited(@SuppressWarnings("exports") MouseEvent e) {
		
		
	}

	@Override
	public void mouseDragged(@SuppressWarnings("exports") MouseEvent e) {
		
		
	}

	@Override
	public void mouseMoved(@SuppressWarnings("exports") MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}
}
