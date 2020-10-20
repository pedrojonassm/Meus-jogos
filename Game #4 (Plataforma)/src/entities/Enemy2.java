package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.Mundo;

public class Enemy2 extends Entity{
	
	private BufferedImage[] sprites;
	private boolean left = false, right;
	public boolean morrendo = false, grande = false, change = false;
	private int changeFrames, changeMax = 5, changeCount, changeCountMax = 5, morteFrames, morteMax = 30;
	
	public Enemy2(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprites = new BufferedImage[3];
		for(int i = 0; i < 3; i++) {
			sprites[i] = Game.spritesheet.getSprite((6+i)*Game.TS, 2*Game.TS, Game.TS, Game.TS);
		}
		this.speed = 1.4;
		this.gravity = 2;
		this.setMask(3, 5, 10, 11);
	}
	
	public void tick() {
		if (!morrendo) {
			if (change) {
				changeFrames++;
				if (changeFrames >= changeMax) {
					changeCount++;
					changeFrames = 0;
					if (grande) {
						grande = false;
					}else {
						grande = true;
					}
				}
				if (changeCount >= changeCountMax) {
					change = false;
					changeCount = 0;
				}
			}
			if (Mundo.isFree((int) x, (int) (y+gravity))) {
				y+=gravity;
			}
			if(right && Mundo.isFree((int) (x+speed), (int) y) && !Mundo.isFree((int) (x+Game.TS), (int) (y+1))) {
				x += speed;
			}else {
				right = false;
				left = true;
			}
			if (left && Mundo.isFree((int) (x-speed), (int) y) && !Mundo.isFree((int) (x-Game.TS), (int) (y+1))){
				x -= speed;
			}else {
				left = false;
				right = true;
			}
		}else {
			morteFrames++;
			if (morteFrames>=morteMax) {
				Game.entities.remove(this);
				Game.inimigos2.remove(this);
			}
		}
	}
	
	public void render(Graphics g) {
		//super.render(g);
		if (morrendo) {
			g.drawImage(sprites[2], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if (grande) {
			g.drawImage(sprites[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else {
			g.drawImage(sprites[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
