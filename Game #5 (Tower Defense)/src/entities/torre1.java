package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class torre1 extends Entity{
	
	private static BufferedImage[] sprites;
	private int frames, maxFrames = 30, index, attackFrames, maxAttack=5;
	private boolean atacando = false;
	private Enemy foco;
	
	
	public torre1(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		sprites = new BufferedImage[4];
		for (int i = 0; i < 4; i++) {
			sprites[i] = Game.spritesheet.getSprite((6+i)*Game.TS, 0*Game.TS, Game.TS, Game.TS);
		}
	}
	
	public void tick() {
		for (int i = 0; i < Game.inimigos.size() && foco == null;i++) {
			Enemy e = Game.inimigos.get(i);
			if (this.distancia(this.getX(), this.getY(), e.getX(), e.getY()) < 16*3) {
				foco = e;
				break;
			}
		}
		
		if (foco != null && this.distancia(this.getX(), this.getY(), foco.getX(), foco.getY()) <= 16*2) {
			maxFrames = 5;
			atacando = true;
			rotate = Math.atan2(foco.getY()+8-this.getY(),foco.getX()+8-this.getX());
		}else {
			atacando = false;
			foco = null;
			maxFrames = 30;
		}
		frames++;
		if(frames >= maxFrames) {
			frames = 0;
			index++;
			if (atacando) {
				if (index >= 4) {
					index=2;
				}
				attackFrames++;
				if(attackFrames > maxAttack) {
					attackFrames = 0;
					if (Entity.rand.nextInt(100) > 40) {
						foco.life -= Entity.rand.nextInt(4)+1;
					}
					if (foco.life <= 0) {
						Game.pontos++;
						foco = null;
					}
				}
			}else {
				attackFrames=0;
				if (index >= 2) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(this.rotate, this.getX()+Game.TS/2, this.getY()+Game.TS/2);
		g2.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		g2.rotate(-this.rotate, this.getX()+Game.TS/2, this.getY()+Game.TS/2);
		g.setColor(Color.red);
	}
}
