package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import graficos.Spritesheet;
import graficos.Ui;
import main.Gerador;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT,HIGH;
	public static final int TILE_SIZE = Gerador.TS;
	public static int maxDistance = (Gerador.WIDTH/Gerador.TS + 10)/2, posX, posY;
	public static ArrayList<BufferedImage[]> sprites_do_mundo; // chaos64, chaos128, paredes64, paredes128, itens64, itens128, escadas64, escadas128
	
	
	public static int tiles_index, tiles_animation_time, max_tiles_animation_time;
	
	public World(String path){
		//*
		tiles_index = tiles_animation_time = 0;
		max_tiles_animation_time = 15;
		 try {
			 if (path == null) {
				 WIDTH = 20; HEIGHT = 20; HIGH = 7;
			 }else {
				 
			 }
			tiles = new Tile[WIDTH * HEIGHT * HIGH];
			for(int xx = 0; xx < WIDTH; xx++)
				for(int yy = 0; yy < HEIGHT; yy++)
					for (int zz = 0; zz < HIGH; zz++)
						tiles[(xx + (yy * WIDTH))*HIGH+zz] = new Tile(xx*Gerador.TS,yy*Gerador.TS, zz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//*/
	}
	
	public static void carregar_sprites() {
		sprites_do_mundo = new ArrayList<BufferedImage[]>();
		Spritesheet[] sprites = new Spritesheet[8];
		int[] total_de_sprites = {36*40+16, 9, 27*20-3, 40*32-11, 40*23-16, 20*16+2, 35, 40};
		sprites[0] = new Spritesheet("/chaos64.png", 64); // total de sprites: 36*40 + 16
		sprites[1] = new Spritesheet("/chaos128.png", 128); // total de sprites: 9
		sprites[2] = new Spritesheet("/paredes64.png", 64); // total de sprites: 27*20 - 3
		sprites[3] = new Spritesheet("/paredes128.png", 128); // total de sprites: 40*32 - 11
		sprites[4] = new Spritesheet("/itens64.png", 64); // total de sprites: 40*23 - 16
		sprites[5] = new Spritesheet("/itens128.png", 128); // total de sprites: 20*16 + 2
		sprites[6] = new Spritesheet("/escadas64.png", 64); // total de sprites: 35
		sprites[7] = new Spritesheet("/escadas128.png", 128); // total de sprites: 40
		
		int max_pagina = 0;
		for (int i = 0; i < 8; i++) {
			sprites_do_mundo.add(sprites[i].get_x_sprites(total_de_sprites[i]));
			max_pagina += total_de_sprites[i];
		}
		Gerador.ui.max_pagina_por_total_de_sprites(max_pagina);
	}
	
	public static Tile pegar_chao(int pos) {
		return tiles[pos];
	}
	
	public static Tile pegar_chao(int mx, int my) {
		// (mx >> 6) + (my>>6)*WIDTH
		return pegar_chao(((mx >> 6) + (my>>6)*World.WIDTH)*World.HIGH+Ui.camada); // 6 pq o tamanho que estamos usando é 64 (2^6 = 64)
	}
	
	public void tick() {
		if (++tiles_animation_time >= max_tiles_animation_time) {
			tiles_animation_time = 0;
			if (++tiles_index >= 100) {
				tiles_index = 0;
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
		
		return !((tiles[x1 + (y1*World.WIDTH)].getSolid()) ||
				(tiles[x2 + (y2*World.WIDTH)].getSolid()) ||
				(tiles[x3 + (y3*World.WIDTH)].getSolid()) ||
				(tiles[x4 + (y4*World.WIDTH)].getSolid()));
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
		
		for(int xx = xstart; xx <= xfinal; xx++)
			for(int yy = ystart; yy <= yfinal; yy++)
				for (int zz = 0; zz < HIGH; zz++){
					if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
						continue;
					}
					tiles[(xx + (yy * WIDTH))*HIGH+zz].render(g);
			}
	}
}
