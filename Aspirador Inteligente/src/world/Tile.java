package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Tile {
	public static int Tiles = 5;
	public static BufferedImage TILE_Fundo = Game.spritesheet.getSprite(0,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Parede = Game.spritesheet.getSprite(Game.TS,0,Game.TS,Game.TS);

	private BufferedImage sprite;
	public int x,y;
	
	public int estado = 0;
	public boolean solid = false;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		if (estado == 4) {//seco
			g.setColor(Color.orange);
			g.fillOval(x - Camera.x+Game.TS/4, y - Camera.y+Game.TS/4, Game.TS/2, Game.TS/2);
		}else if (estado == 3) {//molhado
			g.setColor(Color.blue);
			g.fillOval(x - Camera.x+Game.TS/4, y - Camera.y+Game.TS/4, Game.TS/2, Game.TS/2);
		}
		else if (estado == 2) {//limpo
			g.setColor(Color.green);
			g.fillOval(x - Camera.x+Game.TS/4, y - Camera.y+Game.TS/4, Game.TS/2, Game.TS/2);
		}else if (estado == 1) {//conhecido
			g.setColor(Color.yellow);
			g.fillOval(x - Camera.x+Game.TS/4, y - Camera.y+Game.TS/4, Game.TS/2, Game.TS/2);
		}
	}

	public void trocarSprite(int xx, int yy, int tx, int ty) {
		this.sprite = Game.spritesheet.getSprite(xx, yy, tx, ty);
	}

}
