package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.*;
import main.Game;

public class Mundo {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	
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
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000){ // Preto
						//estrada
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){ // Branco
						//grama
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF4CFF00) { // Verde Claro
						// jogador
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		mapaaleatorio();
	}
	
	private void mapaaleatorio() {
		// o tamanho do mundo é isso multiplicado por 16
		WIDTH = 187;
		HEIGHT = 187;
		tiles = new Tile[WIDTH*HEIGHT];
		int r, k = 0, l = 0;
		boolean agora = true, player = false;
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy< HEIGHT; yy++) {
				if(xx == 0 || yy == 0 || xx == WIDTH-1 || yy == HEIGHT-1) {
					tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
				}else {
					tiles[xx+yy*WIDTH] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
				}
			}
		}
		
		for(int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx+yy*WIDTH] instanceof FloorTile) {
					r = Entity.rand.nextInt(100);
					if(r == 1 && l < 5 && xx > 5 && xx < WIDTH-5 && yy > 5 && yy < HEIGHT-5) {
						agora = true;
						//super(x, y, width, height, speed, sprite);
						//Nave n = new Nave(xx*16, yy*16, Game.TS, Game.TS, Entity.rand.nextInt(6)+2, null);
						//Game.entities.add(n);
						for (int i = 0; i < Game.planetas.size() && agora; i++) {
							Gerador g = Game.planetas.get(i);
							if (Game.player.distancia(xx*16, yy*16, g.getX(), g.getY()) < 1500) {
								agora = false;
							}
						}
						if (agora) {
							Gerador g = new Gerador(xx*16, yy*16, 48, 48, l, null);
							Game.entities.add(g);
							Game.planetas.add(g);
							l++;
						}
					}else if(r == 2 && k < 150){
						// meteoros
						k++;
						Meteor m = new Meteor(xx*16, yy*16, Game.TS, Game.TS, 1, null);
						Game.meteoros.add(m);
						Game.entities.add(m);
					}else if(r == 3 && !player) {
						player = true;
						Game.player.setX(xx*Game.TS);
						Game.player.setY(yy*Game.TS);
					}
				}
			}
		}
		/*while (!inimigos) {
			r = Game.rand.nextInt(10);
			int x = 0;
			while(x < r) {
				for(int xx = 0; xx < WIDTH; xx++) {
					for(int yy = 0; yy < HEIGHT; yy++) {
						if(tiles[xx+yy*WIDTH] instanceof floorTile && Game.rand.nextInt(100) < 20) {
							inimigos = true;
							x++;
							Game.inimigos.add(new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.ENEMY_EN));
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
		/*if (Game.player.life <= 0) {
			Game.entities.clear();
			//Game.inimigos.clear();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0,0,Game.TS,Game.TS, 2, Game.spritesheet.getSprite(2*Game.TS, 0, Game.TS, Game.TS));
			Game.entities.add(Game.player);
			
			Game.pontos = 0;
			Game.player.life = 3;
			Game.world = new Mundo("/"+level+".png");
			Game.GameState = "normal";
		}*/
		for (int i = 0; i < Game.entities.size(); i++) {
			Game.entities.remove(0);
		}
		Game.entities.add(Game.player);
		for (int i = 0; i < Game.meteoros.size(); i++) {
			Game.meteoros.remove(0);
		}
		for (int i = 0; i < Game.azul.size(); i++) {
			Game.azul.remove(0);
		}
		for (int i = 0; i < Game.verde.size(); i++) {
			Game.verde.remove(0);
		}
		for (int i = 0; i < Game.vermelho.size(); i++) {
			Game.vermelho.remove(0);
		}
		for (int i = 0; i < Game.amarelo.size(); i++) {
			Game.amarelo.remove(0);
		}
		for (int i = 0; i < Game.azul.size(); i++) {
			Game.azul.remove(0);
		}
		for (int i = 0; i < Game.planetas.size(); i++) {
			Game.planetas.remove(0);
		}
		for (int i = 0; i < Game.lasers.size(); i++) {
			Game.lasers.remove(0);
		}
		if (Game.player.controle != null) {
			Game.player.controle = null;
		}
		Game.player.life = Game.player.maxLife;
		Game.world = null;
		Game.world = new Mundo("aasd");
	}
	
	public void render(Graphics g){
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
