package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.*;
import graficos.Spritesheet;
import main.Game;

public class Mundo {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = Game.TS;
	// 255 = 24*60*Game.amountOfTicks
	//
	public static double ciclo = 0, maxCiclo = 24*Game.amountOfTicks;
	private int tempo = 1;
	public static int maxDistance = (Game.WIDTH/Game.TS + 10)/2, posX, posY;
	
	public Mundo(String path){
		/*try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*Game.TS,yy*Game.TS,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000){//preto
						//Fundo
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*Game.TS,yy*Game.TS,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){ //Branco
						//Chão
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*Game.TS,yy*Game.TS,Tile.TILE_WALL);
						if (((yy-1)*WIDTH) >= 0 && pixels[xx+((yy-1)*WIDTH)] == 0xFFFFFFFF) {
							//caso tenha um chao em cima, retira-se a grama
							
							//tiles[xx + (yy * WIDTH)].trocarSprite(2*Game.TS, 2*Game.TS, Game.TS, Game.TS);
						}
					}else if(pixelAtual == 0xFF0026FF) { //Azul Escuro
						//Player
						Game.player.setX(xx*Game.TS);
						Game.player.setY(yy*Game.TS);
					}else if(pixelAtual == 0xFFFF0000) { // Vermelho
						//Instanciar inimigo e adicionar a lista das entities
						Enemy inimigo = new Enemy(xx*Game.TS,yy*Game.TS, Game.TS, Game.TS, 1, null);
						Game.inimigos.add(inimigo);
						Game.entities.add(inimigo);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		mapaRandom();
	}
	
	public void tick() {
		//ciclo+=tempo;
		if (ciclo >= maxCiclo || ciclo <= 0) {
			tempo *= -1;
		}
		iluminacao();
	}
	
	public void iluminacao() {
		posX = ((Game.WIDTH)/2 + Camera.x)/Game.TS;
		posY = ((Game.HEIGHT)/2 + Camera.y)/Game.TS;
		
		// Usados para os métodos de iluminação 1, 2 e 3
		/*
		for (int d = maxDistance; d >= 0; d--) {
			for (int ty = d*-1; ty <= d; ty++) {
				if (posY + ty < Mundo.HEIGHT && posY+ty > 0) {
					for (int tx = d*-1; tx<=d; tx++) {
						if (posX + tx < Mundo.WIDTH && posX+tx > 0) {
							if (ty == d*-1 || tx == d*-1 || ty == d || tx == d) {
								tiles[posX+tx+(posY+ty)*WIDTH].iluminar();
							}
						}
					}
				}		
			}
		}//*/
		tiles[posX+posY*WIDTH].iluminar();
	}
	
	public static boolean podeIluminar(int x, int y) {
		return (int) Game.player.distancia(posX, posY, x/Game.TS, y/Game.TS) < maxDistance;
	}
	
	private void mapaRandom() {
		WIDTH = Entity.rand.nextInt(5000)+200;
		HEIGHT = Entity.rand.nextInt(5000)+200;
		tiles = new Tile[WIDTH*HEIGHT];
		int maxAltura = HEIGHT*(3/4);
		int[] r = {-3, -2, -1, 0, 1, 2, 3};
		int alturaInicial = Entity.rand.nextInt(20 + 14) + 14;
		int bioma = Entity.rand.nextInt(Tile.Tiles-2)+2, maxNextBioma = Entity.rand.nextInt(30), nextBioma = 0;
		for(int xx = 0; xx < WIDTH; xx++) {
			do {
				alturaInicial = r[Entity.rand.nextInt(r.length)] + alturaInicial;
			}while (alturaInicial > WIDTH && alturaInicial < maxAltura);
			for(int yy = 0; yy < HEIGHT; yy++) {
				if (yy == HEIGHT - 1 || xx == 0 || xx == WIDTH -1 || yy == 0) {
					WallTile t = new WallTile(xx*16, yy*16, Tile.TILE_Fim);
					t.solid = true;
					tiles[xx+yy*WIDTH] = t;
				}else {
					if (yy >= alturaInicial) {
						if (bioma == 2) {
							tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_Terra);
						}else if (bioma == 3) {
							tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_Areia);
						}else if (bioma == 4) {
							tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_Pedra);
						}
					}else {
						tiles[xx+yy*WIDTH] = new FloorTile(xx*16, yy*16, Tile.TILE_Fundo);
						tiles[xx+yy*WIDTH].livre = true;
					}
					
				}
			}
			nextBioma++;
			if (nextBioma > maxNextBioma) {
				nextBioma = 0;
				bioma = Entity.rand.nextInt(Tile.Tiles-2)+2;
				maxNextBioma = Entity.rand.nextInt(30);
			}
		}
		Game.player.setX(2*16);
		Game.player.setY(2*16);
		/*
		for(int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx+yy*WIDTH] instanceof FloorTile) {
					r = Entity.rand.nextInt(100);
					if(r < 2 && Game.player.distancia(Game.player.getX(), Game.player.getY(), xx*Game.TS, yy*Game.TS) > 30) {
						//Enemy en = new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, 1, null);
						//Game.inimigos.add(en);
						//Game.entities.add(en);
					}else if(r < 6) {
						tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}
					else if(!player && r < 40) {
						player = true;
						Game.player.setX(xx*Game.TS);
						Game.player.setY(yy*Game.TS);
					}
				}
			}
		}*/
		/*while (!inimigos) {
			r = Entity.rand.nextInt(10);
			int x = 0;
			while(x < r) {
				for(int xx = 0; xx < WIDTH; xx++) {
					for(int yy = 0; yy < HEIGHT; yy++) {
						if(tiles[xx+yy*WIDTH] instanceof FloorTile && Entity.rand.nextInt(100) < 20) {
							inimigos = true;
							x++;
							Enemy en = new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, 1, Game.spritesheet.getSprite(6*Game.TS, Game.TS, 16, 16));
							Game.entities.add(en);
							Game.inimigos.add(en);
						}
					}
				}
			}
		}*/
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*Mundo.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*Mundo.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*Mundo.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*Mundo.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(String level){
		if (Game.player.life <= 0) {
			Game.entities.clear();
			//Game.inimigos.clear();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0,0,Game.TS,Game.TS, 2, Game.spritesheet.getSprite(2*Game.TS, 0, Game.TS, Game.TS));
			Game.entities.add(Game.player);
			
			Game.pontos = 0;
			Game.player.life = 3;
			Game.world = new Mundo("/"+level+".png");
			Game.GameState = "normal";
		}
	}
	private int log2(int n) {
		int k = 0;
		while (n%2 == 0) {
			k++;
			n = n/2;
		}
		return k;
	}
	public void render(Graphics g){
		int xstart = Camera.x >> log2(Game.TS);
		int ystart = Camera.y >> log2(Game.TS);
		
		int xfinal = xstart + (Game.WIDTH >> log2(Game.TS));
		int yfinal = ystart + (Game.HEIGHT >> log2(Game.TS));
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				tiles[xx + (yy*WIDTH)].render(g);
			}
		}
	}
}
