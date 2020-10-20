package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Fruta extends Entity{
	public static BufferedImage[] frutaSprites = new BufferedImage[3];
	public int f;
	
	public Fruta(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		f = Entity.rand.nextInt(3);
		for (int i = 0; i < 3; i++) {
			frutaSprites[i] = Game.spritesheet.getSprite((3+i)*Game.TS, 1*Game.TS, Game.TS, Game.TS); 
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(frutaSprites[f], getX() - Camera.x, getY() - Camera.y, null);
	}
}
