package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Tile {
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, Game.TS, Game.TS);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(Game.TS, 0, Game.TS, Game.TS);
	
	public BufferedImage sprite;
	public int x, y;
	public boolean show = false;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
			g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
