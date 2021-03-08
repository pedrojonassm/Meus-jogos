package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import graficos.Spritesheet;
import main.Gerador;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = Gerador.TS;
	public static int maxDistance = (Gerador.WIDTH/Gerador.TS + 10)/2, posX, posY;
	
	public static Spritesheet chaos, chaos128, paredes, paredes128, itens, itens128, escadas, escadas128;
	
	public World(String path){
		//*
		carregar_sprites();
		 try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			
			Spritesheet teste = chaos;
			
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*Gerador.TS,yy*Gerador.TS);
					switch (Gerador.random.nextInt(8)) {
					case 0:
						teste = chaos;
						break;
					case 1:
						teste = chaos128;
						break;
					case 2:
						teste = paredes;
						break;
					case 3:
						teste = paredes128;
						break;
					case 4:
						teste = itens;
						break;
					case 5:
						teste = itens128;
						break;
					case 6:
						teste = escadas;
						break;
					case 7:
						teste = escadas128;
						break;
					}
					tiles[xx + (yy * WIDTH)].adicionarsprite(teste.getAsset(Gerador.random.nextInt(teste.getQuadradosX()*teste.getQuadradosY())));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
	}
	
	private void carregar_sprites() {
		chaos = new Spritesheet("/chaos64.png", 64);
		chaos128 = new Spritesheet("/chaos128.png", 128);
		paredes = new Spritesheet("/paredes64.png", 64);
		paredes128 = new Spritesheet("/paredes128.png", 128);
		itens = new Spritesheet("/itens64.png", 64);
		itens128 = new Spritesheet("/itens128.png", 128);
		escadas = new Spritesheet("/escadas64.png", 64);
		escadas128 = new Spritesheet("/escadas128.png", 128);
	}
	
	public static Tile pegar_chao(int mx, int my) {
		return tiles[(mx >> 6) + (my>>6)*WIDTH]; // 6 pq o tamanho que estamos usando é 64 (2^6 = 64)
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
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
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
		
		int xfinal = xstart + (Gerador.WIDTH >> log2(Gerador.TS)) +1;
		int yfinal = ystart + (Gerador.HEIGHT >> log2(Gerador.TS)) +1;
		
		xstart--;
		ystart--;
		
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
