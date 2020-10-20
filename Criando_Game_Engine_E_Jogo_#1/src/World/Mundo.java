package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.*;
import graficos.spriteSheet;
import main.Game;

public class Mundo {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT; 
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext/Game.TS;
		int y1 = ynext/Game.TS;
		
		int x2 = (xnext+Game.TS-1)/Game.TS;
		int y2 = ynext/Game.TS;
		
		int x3 = xnext/Game.TS;
		int y3 = (ynext+Game.TS-1)/Game.TS;
		
		int x4 = (xnext+Game.TS-1)/Game.TS;
		int y4 = (ynext+Game.TS-1)/Game.TS;
		return !(tiles[x1+(y1*Mundo.WIDTH)] instanceof wallTile ||
				tiles[x2+(y2*Mundo.WIDTH)] instanceof wallTile||
				tiles[x3+(y3*Mundo.WIDTH)] instanceof wallTile ||
				tiles[x4+(y4*Mundo.WIDTH)] instanceof wallTile);
	}
	
	public Mundo(String path) {
		try {
			BufferedImage mapa = ImageIO.read(getClass().getResource(path));
			WIDTH = mapa.getWidth();
			HEIGHT = mapa.getHeight();
			int[] pixels = new int[WIDTH*HEIGHT];
			tiles = new Tile[WIDTH*HEIGHT];
			mapa.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			for(int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					tiles[xx + yy*WIDTH] = new floorTile(xx*Game.TS, yy*Game.TS, Tile.TILE_FLOOR);
					int pixelAtual = pixels[xx + yy*WIDTH];
					if (pixelAtual == 0xFF000000) {
						// colocar o chão
						tiles[xx + yy*WIDTH] = new floorTile(xx*Game.TS, yy*Game.TS, Tile.TILE_FLOOR);
					}
					else if((pixelAtual == 0xFFFFFFFF)){//FFFFFF = branco, basta adicionar FF antes (se for pega pelo paint.net)
						// Parede
						tiles[xx + yy*WIDTH] = new wallTile(xx*Game.TS, yy*Game.TS, Tile.TILE_WALL);
						
					}else if(pixelAtual == 0xFF4800FF){
						//player
						Game.player.setX(xx*Game.TS);
						Game.player.setY(yy*Game.TS);
					}else if(pixelAtual == 0xFFFF0000){
						//Inimigo
						Enemy en = new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.inimigos.add(en);
					}else if(pixelAtual == 0xFF00FF11){
						//Lifepack
						LifePack pack = new LifePack(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.LIFEPACK_EN);
						//pack.setMask(8, 8, 8, 8);
						Game.entities.add(pack);
					}else if(pixelAtual == 0xFFFF00E9){
						//arma
						Game.entities.add(new Weapon(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFFF3FF70){
						//Bateria
						Game.entities.add(new Bateria(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.PILHA_EN));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.inimigos = new ArrayList<Enemy>();
		Game.spritesheet = new spriteSheet("/spritesheet.png");
		Game.player = new Player(0,0,Game.TS,Game.TS,Game.spritesheet.getSprite(2*Game.TS, 0, Game.TS, Game.TS));
		Game.entities.add(Game.player);
		Game.mundo = new Mundo("/"+level);
		return;
	}
	
	public void render(Graphics g) {
		int xstart = (int) Camera.x>>4;
		int ystart = (int) Camera.y>>4;
		int xfinal = (int) xstart + Game.WIDTH/16;
		int yfinal = (int) ystart + Game.HEIGHT/16;
		for(int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + yy*WIDTH];
				tile.render(g);
			}
		}
	}
}
