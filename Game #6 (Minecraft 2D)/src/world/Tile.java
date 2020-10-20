package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Tile {
	public static int Tiles = 5;
	public int transparence = 0;
	public static BufferedImage TILE_Fundo = Game.spritesheet.getSprite(0,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Fim = Game.spritesheet.getSprite(Game.TS,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Terra = Game.spritesheet.getSprite(Game.TS*2,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Areia = Game.spritesheet.getSprite(Game.TS*3,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Pedra = Game.spritesheet.getSprite(Game.TS*4,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Luminosa = Game.spritesheet.getSprite(Game.TS*5,0,Game.TS,Game.TS);

	private BufferedImage sprite;
	private int x, y;;
	public boolean solid = false, livre, iluminado = false;
	public int brilhante = 255;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		Graphics2D g2 = (Graphics2D) g;
		if (transparence < 0) {
			transparence = 0;
		}else if (transparence > 255) {
			transparence = 255;
		}
		iluminado = false;
		g2.setColor(new Color(0,0,0,transparence));
		transparence = 255;
		g2.fillRect(x-Camera.x, y-Camera.y, Game.TS, Game.TS);
	}
	
	public void ceuLivre() {
		int yy = y/Game.TS;
		while (yy > 1) {
			if (Mundo.tiles[x/Game.TS+(yy-1)*Mundo.WIDTH] instanceof WallTile) {
				return;
			}
			yy--;
		}
		yy = y/Game.TS;
		yy++;
		livre = true;
		while (Mundo.tiles[x/Game.TS+(yy)*Mundo.WIDTH] instanceof FloorTile) {
			Mundo.tiles[x/Game.TS+(yy)*Mundo.WIDTH].livre = true;
			yy++;
		}
	}
	
	public void sombra() {
		int yy = y/Game.TS + 1;
		while (Mundo.tiles[x/Game.TS+(yy)*Mundo.WIDTH] instanceof FloorTile) {
			Mundo.tiles[x/Game.TS+(yy)*Mundo.WIDTH].livre = false;
			yy++;
		}
	}
	
	public void iluminar() {
		iluminado = true;
		//Desta forma, se o tile brilhar, ele olha ao redor iluminando os outros, funciona mas não é muito eficiente
		//metodo1();
		// Desta outra forma, as paredes checam se existe um brilhante ao redor, e se iluminam.
		// Como elas verificam os mais próximos primeiro, será possível adicionar um return o que talvez acelere
		//metodo2();
		
		// Desta outra forma, a luz é alterada para receber sempre a quantidade de luz mais próxima, não sendo necessario zerá-la
		// e caso esteja com o ceu livre, a luz recebe a variação do dia
		//metodo3();
			
		// Dessa nova forma, é usado uma váriável boolean, caso o tile foi atualizado ou não
		// Essa variável recebe true toda vez que passar por ele, e caso essa variavel seja false, o próprio iluminar chama ele sendo irmao
		// nesse caso, o mundo não necessita de um for
		
		if (Mundo.podeIluminar(x, y)) {
			metodo4();
		}//*/
		//System.out.println(Mundo.tilesDistance(x, y)+"     max: "+Mundo.maxDistance);//*/
	}

	private void metodo1() {
		int xx = x/Game.TS, yy = y/Game.TS;
		if (livre) {
			transparence = (int) ((255/Mundo.maxCiclo)*Mundo.ciclo);
		}else if (brilhante != 255) {
			transparence = brilhante;
		}else {
			int maxDistance = 6;
			for (int d = 0; d < maxDistance; d++) {
				for (int ty = d*-1; ty <= d; ty++) {
					if (yy + ty < Mundo.HEIGHT && yy+ty > 0) {
						for (int tx = d*-1; tx<=d; tx++) {
							if (xx + tx < Mundo.WIDTH && xx+tx > 0) {
								if (ty == d*-1 || tx == d*-1 || ty == d || tx == d) {
									if (Mundo.tiles[(xx+tx)+(yy+ty)*Mundo.WIDTH].transparence > d*(255/maxDistance)) {
										Mundo.tiles[(xx+tx)+(yy+ty)*Mundo.WIDTH].transparence = (int) d*(255/maxDistance);
									}
								}
							}
						}
					}
						
				}
			}
		}
		
	}
	
	private void metodo2() {
		int xx = x/Game.TS, yy = y/Game.TS;
		if (livre) {
			transparence = (int) ((255/Mundo.maxCiclo)*Mundo.ciclo);
		}else if (brilhante != 255) {
			transparence = brilhante;
		}else {
			int maxDistance = 6;
			for (int d = 0; d < maxDistance; d++) {
				for (int ty = d*-1; ty <= d; ty++) {
					if (yy + ty < Mundo.HEIGHT && yy+ty > 0) {
						for (int tx = d*-1; tx<=d; tx++) {
							if (xx + tx < Mundo.WIDTH && xx+tx > 0) {
								if (ty == d*-1 || tx == d*-1 || ty == d || tx == d) {
									if (Mundo.tiles[(xx+tx)+(yy+ty)*Mundo.WIDTH].livre) {
										transparence = d*(255/maxDistance);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void metodo3() {
		if (livre) {
			transparence = (int) ((255/Mundo.maxCiclo)*Mundo.ciclo);
		}else if(brilhante != 255){
			transparence = brilhante;
		}else {
			int xx = x/Game.TS, yy = y/Game.TS;
			for (int ty = yy-1; ty <= yy+1; ty++) {
				if (ty < Mundo.HEIGHT && ty > 0) {
					for (int tx = xx-1; tx<=xx+1; tx++) {
						if (tx < Mundo.WIDTH && tx > 0) {
							if (Mundo.tiles[tx+ty*Mundo.WIDTH].transparence < transparence + 255/15) {
								transparence = Mundo.tiles[tx+ty*Mundo.WIDTH].transparence + 255/15;
							}
						}
					}
				}
			}
		}
	}
	
	private void metodo4() {
		int xx = x/Game.TS, yy = y/Game.TS;
		for (int ty = yy-1; ty <= yy+1; ty++) {
			if (ty < Mundo.HEIGHT && ty > 0) {
				for (int tx = xx-1; tx<=xx+1; tx++) {
					if (tx < Mundo.WIDTH && tx > 0) {
						if (!Mundo.tiles[tx+ty*Mundo.WIDTH].iluminado) {							
							Mundo.tiles[tx+ty*Mundo.WIDTH].iluminar();
						}
					}
				}
			}
		}
		if (livre) {
			transparence = (int) ((255/Mundo.maxCiclo)*Mundo.ciclo);
		}else if(brilhante != 255){
			transparence = brilhante;
		}else {
			for (int ty = yy-1; ty <= yy+1; ty++) {
				if (ty < Mundo.HEIGHT && ty > 0) {
					for (int tx = xx-1; tx<=xx+1; tx++) {
						if (tx < Mundo.WIDTH && tx > 0) {
							if (Mundo.tiles[tx+ty*Mundo.WIDTH].transparence < transparence + 255/15) {
								transparence = Mundo.tiles[tx+ty*Mundo.WIDTH].transparence + 255/15;
							}
						}
					}
				}
			}
		}
	}
	
	public void trocarSprite(int xx, int yy, int tx, int ty) {
		this.sprite = Game.spritesheet.getSprite(xx, yy, tx, ty);
	}

}
