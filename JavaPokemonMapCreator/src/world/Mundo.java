package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Gerador;

public class Mundo {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = Gerador.TS;
	public static int maxDistance = (Gerador.WIDTH/Gerador.TS + 10)/2, posX, posY;
	
	public Mundo(String path){
		//*
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
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*Gerador.TS,yy*Gerador.TS,Tile.TILE_Vazio);
					if(pixelAtual == 0xFF000000){//preto
						//Fundo
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*Gerador.TS,yy*Gerador.TS,Tile.TILE_Vazio);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
	}
	
	public void tick() {
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
	
	private int log2(int n) {
		int k = 0;
		while (n%2 == 0) {
			k++;
			n = n/2;
		}
		return k;
	}
	public void render(Graphics g){
		int xstart = Camera.x >> log2(Gerador.TS);
		int ystart = Camera.y >> log2(Gerador.TS);
		
		int xfinal = xstart + (Gerador.WIDTH >> log2(Gerador.TS));
		int yfinal = ystart + (Gerador.HEIGHT >> log2(Gerador.TS));
		
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
