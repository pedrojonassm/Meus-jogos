package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import World.Camera;
import main.Game;

public class Entity{
	protected double x, y;
	protected int width, height;
	private BufferedImage sprite;
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(1*Game.TS, 1*Game.TS, Game.TS, Game.TS);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(2*Game.TS, 1*Game.TS, Game.TS, Game.TS);
	public static BufferedImage PILHA_EN = Game.spritesheet.getSprite(5*Game.TS, 1*Game.TS, Game.TS, Game.TS);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(8*Game.TS, 0*Game.TS, Game.TS, Game.TS);
	public int maskx,masky,mwidth,mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx,int masky,int mwidth,int mheight){
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, height);
	}
	
	public void tick() {
		
	}
	
	public int getX() {
		return (int) this.x;
	}
	public int getY() {
		return (int) this.y;
	}
	public void setX(int n) {
		this.x = n;
	}
	public void setY(int n) {
		this.y = n;
	}
	public int getHeight() {
		return this.height;
	}
	public int getWidth() {
		return this.width;
	}
	
	public static boolean isColidding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx,e1.getY()+e1.masky,e1.mwidth,e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.mwidth,e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
}
