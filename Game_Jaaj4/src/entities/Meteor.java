package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import world.Camera;
import world.Mundo;

public class Meteor extends Entity{
	
	
	public Meteor(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		int k = rand.nextInt(5)+2;
		this.sprite = Game.spritesheet.getSprite(k*Game.TS, 0, Game.TS, Game.TS);
		this.life = this.maxLife= k*100;
		vx = rand.nextDouble();
		vy = rand.nextDouble();
		cacador = new ArrayList<Nave>();
		if (k == 2) {
			//
			mx = 4;
			my = 4;
			mwidth = 7;
			mheight = 8;
		}else if (k == 3) {
			//
			mx = 3;
			my = 3;
			mwidth = 5;
			mheight = 6;
		}else if (k == 4) {
			//
			mx = 2;
			my = 2;
			mwidth = 4;
			mheight = 4;
		}else {
			//
			mx = 1;
			my = 1;
			mwidth = 2;
			mheight = 2;
		}
	}
	
	public void tick() {
		if (escolhido) {
			if (!Game.player.hold) {
				escolhido = false;
			}
		}
		if (x < 0 || y < 0 || x > Mundo.WIDTH*16 || y > Mundo.HEIGHT*16) {
			Game.meteoros.remove(this);
			Game.entities.remove(this);
		}else if (controlado) {
			x = Game.player.getX();
			y = Game.player.getY();
		}else {
			if (Mundo.isFree((int)(x+vx), (int)y)){
				x += vx;
			}else {
				vx*=-1;
			}
			if (Mundo.isFree((int)(x), (int) (y+vy))) {
				y += vy;
			}else {
				vy*=-1;
			}
		}
	}
	
	public void render(Graphics g) {
		if (escolhido) {
			g.setColor(Color.red);
			g.fillRect(getX()-Camera.x, getY()-Camera.y, 1, 3);
			g.fillRect(getX()-Camera.x, getY()-Camera.y, 3, 1);
			g.fillRect(getX()-Camera.x+Game.TS-1, getY()-Camera.y+Game.TS-3, 1, 3);
			g.fillRect(getX()-Camera.x+Game.TS-3, getY()-Camera.y+Game.TS-1, 3, 1);
		}
		/*g.setColor(Color.red);
		g.fillRect(getX()+mx-Camera.x, getY()+my-Camera.y, this.getWidth()-mwidth, this.getHeight()-mheight);//mascara de colisao*/
		g.drawImage(sprite, getX()-Camera.x, getY()-Camera.y, null);
	}
	
}
