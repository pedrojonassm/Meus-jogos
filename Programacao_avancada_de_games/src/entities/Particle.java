package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import World.Camera;
import main.Game;

public class Particle extends Entity{
	public int lifeTime = 15, curLife = 0;
	public double spd = 2, dx = 0, dy = 0;

	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		dx = Game.rand.nextGaussian();
		dy = Game.rand.nextGaussian();
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(curLife >= lifeTime) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(getX()-Camera.x, getY()-Camera.y, width, height);
	}
	
}
