package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.AStar;
import world.Camera;
import world.Vector2i;

public class Enemy extends Entity{
	private static BufferedImage[] sprites;
	private int moveFrames, maxMove = 15, index = 1, maxIndex = 3;
	public double life, maxLife;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprites = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			sprites[i] = Game.spritesheet.getSprite((2+i)*Game.TS, 0*Game.TS, Game.TS, Game.TS);
		}
		this.sprite = sprites[1];
		int[] l = {1,2,4,8,16};
		this.speed = l[Entity.rand.nextInt(l.length)];
		if (Game.torres1.size() > 0) {
			maxLife = (Entity.rand.nextInt(Game.torres1.size()*16)+16)/speed;
		}else {
			maxLife = 16/speed;
		}
		life = maxLife;
		Vector2i start, end;
		start = new Vector2i(((int)(x/16)),((int)(y/16)));
		end = new Vector2i(((int)(Game.chegada.x/16)),((int)(Game.chegada.y/16)));
		path = AStar.findPath(Game.world, start, end);
	}
	
	public void tick() {
		moveFrames++;
		if (moveFrames >= maxMove) {
			followPath(path);
			moveFrames = 0;
			index++;
			if(index >= maxIndex) {
				index= 0;
			}
		}
		if (life <= 0) {
			Game.entities.remove(this);
			Game.inimigos.remove(this);
		}
	}
	
	public void render(Graphics g) {
		//super.render(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(this.rotate), this.getX()+Game.TS/2, this.getY()+Game.TS/2);
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		g2.rotate(-Math.toRadians(this.rotate), this.getX()+Game.TS/2, this.getY()+Game.TS/2);
		g.setColor(Color.red);

		// Desenhando a vida
		g.fillRect(getX(), getY()-3, (int) ((life/maxLife)*16), 3);
		//g.fillRect(getX() + (int) ((Game.TS-life)/2), getY()-3, life, 3);
	}
}
