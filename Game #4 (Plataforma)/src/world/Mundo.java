package world;

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
	public static final int TILE_SIZE = 16;
	private boolean player = false;
	
	public Mundo(String path){
		try {
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
					if(pixelAtual == 0xFF000000){
						//Fundo
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){
						//Ch�o
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
						if (((yy-1)*WIDTH) >= 0 && pixels[xx+((yy-1)*WIDTH)] == 0xFFFFFFFF) {
							tiles[xx + (yy * WIDTH)].trocarSprite(2*Game.TS, 2*Game.TS, Game.TS, Game.TS);
						}
					}else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFFFF0000) {
						//Instanciar inimigo e adicionar a lista das entities
						Enemy inimigo = new Enemy(xx*16,yy*16, 16, 16, 1, null);
						Game.inimigos.add(inimigo);
						Game.entities.add(inimigo);
					}else if(pixelAtual == 0xFFFFFF00) {
						Enemy2 inimigo = new Enemy2(xx*16,yy*16, 16, 16, 1, null);
						Game.inimigos2.add(inimigo);
						Game.entities.add(inimigo);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mapaRandom();
	}
	
	private void mapaRandom() {
		WIDTH = 50;
		HEIGHT = 50;
		tiles = new Tile[WIDTH*HEIGHT];
		int r;
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
		}
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
