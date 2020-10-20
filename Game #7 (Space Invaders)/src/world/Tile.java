package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Tile {
	public static BufferedImage TILE_Fundo = Game.spritesheet.getSprite(0,0,Game.TS,Game.TS);
	public static BufferedImage TILE_Fim = Game.spritesheet.getSprite(Game.TS,0,Game.TS,Game.TS);

	private BufferedImage sprite;
	private int x, y;
	public boolean solid = false;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
	public void trocarSprite(int xx, int yy, int tx, int ty) {
		this.sprite = Game.spritesheet.getSprite(xx, yy, tx, ty);
	}

}
