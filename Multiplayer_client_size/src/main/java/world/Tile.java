package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Client;

public class Tile {
	public static BufferedImage TILE_FLOOR;// = Game.spritesheet.getSprite(0, 0, Game.TS, Game.TS);
	public static BufferedImage TILE_WALL;// = Game.spritesheet.getSprite(Game.TS, 0, Game.TS, Game.TS);
	protected Color c;
	public BufferedImage sprite;
	public int x, y;
	public boolean show = false;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.setColor(c);
		g.fillRect(x - Camera.x, y - Camera.y, Client.TS, Client.TS);
		//g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
