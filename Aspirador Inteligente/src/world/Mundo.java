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
	public static final int TILE_SIZE = Game.TS;
	private boolean player = false;
	
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
	
	private void mapaRandom() {
		WIDTH = 50;
		HEIGHT = 50;
		tiles = new Tile[WIDTH*HEIGHT];
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if (xx == 0 || yy == 0 || xx == WIDTH-1 || yy == HEIGHT-1) {
					tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16, Tile.TILE_Parede);
					tiles[xx+yy*WIDTH].solid = true;
				}else {
					tiles[xx+yy*WIDTH] = new FloorTile(xx*16, yy*16, Tile.TILE_Fundo);
				}
			}
		}
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
