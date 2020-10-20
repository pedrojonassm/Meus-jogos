package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import World.Camera;
import World.Node;
import World.Vector2i;
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
	protected List<Node> path;
	public int depth; //ordem de renderização
	
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
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {//ainda há caminho pra percorrer
				Vector2i target = path.get(path.size()-1).tile;
				//xprev = x;
				//yprev = y;
				if(x<target.x*16) {
					x++;
				}else if(x>target.x*16){
					x--;
				}
				if (y < target.y*16) {//para não andar na diagonal troque o if por else if
					y++;
				}else if(y>target.y*16){
					y--;
				}
				if(x == target.x*16 && y==target.y*16) {
					path.remove(path.size()-1);
				}
			}
		}
	}
	
	public void tick() {}
	
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
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity n0,Entity n1) {
			if(n1.depth < n0.depth)
				return +1; //se n0 for maior, renderize primeiro
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
		
	};
	
	public static boolean isColidding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx,e1.getY()+e1.masky,e1.mwidth,e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx,e2.getY()+e2.masky,e2.mwidth,e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public double distancia(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)); //retorna a distancia mais curta entre esses dois pontos
	}
	
	public boolean IsColliding(int x, int y) {
		Rectangle current = new Rectangle(x + maskx, y + masky, mwidth, mheight);
		for (int i = 0;  i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if (e == this) {
				continue;
			}
			Rectangle target = new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);
			if(current.intersects(target)) {
				return true;
			}
		}
		return false;
	}
	
}
