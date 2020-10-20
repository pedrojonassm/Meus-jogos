package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Enemy extends Entity{
	
	private static BufferedImage[] sprites;
	public int cor, r, c=6;
	private String acao = "random"; //random, perseguir
	private int moveFrames, maxMove = 30, dir;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprites = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			sprites[i] = Game.spritesheet.getSprite((3+i)*Game.TS, 2*Game.TS, Game.TS, Game.TS);
		}
		this.sprite = sprites[0];
	}
	
	public void tick() {
		if (dir == 0) {
			dir = Entity.rand.nextInt(8)+1;
		}
		if (distancia(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) <= 100) {
			acao = "perseguir";
			c=12;
		}else {
			acao = "random";
			c=6;
		}
		if (acao == "random") {
			this.sprite = sprites[0];
			moveFrames++;
			if(moveFrames>maxMove) {
				moveFrames=0;
				dir=0;
			}else {
				switch (dir) {
				case 1:
					x++;
					break;
				case 2:
					x--;
					break;
				case 3:
					y++;
					break;
				case 4:
					y--;
					break;
				case 5:
					x++;
					y++;
					break;
				case 6:
					x--;
					y--;
					break;
				case 7:
					x++;
					y--;
					break;
				case 8:
					x--;
					y++;
					break;
				}		
			}
		}else if (acao == "perseguir") {
			this.sprite = sprites[1];
			if (Game.player.getX() < this.getX()) {
				x--;
			}else {
				x++;
			}
			if (Game.player.getY() < this.getY()) {
				y--;
			}else {
				y++;
			}
		}
		r+=c;
		if(r%360 == 0) {
			r=0;
		}
	}
	
	public void render(Graphics g) {
		//super.render(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(r),this.getX()+Game.TS/2-Camera.x,this.getY()+Game.TS/2-Camera.y);
		g2.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		g2.rotate(-Math.toRadians(r),this.getX()+Game.TS/2-Camera.x,this.getY()+Game.TS/2-Camera.y);
	}
}
