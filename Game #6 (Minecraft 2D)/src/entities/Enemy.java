package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.FloorTile;
import world.Mundo;
import world.Tile;

public class Enemy extends Entity{
	private int dir = 1, quebrarticks = 0, maxQuebra;
	private boolean left = false, right = true;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.gravity = 0.4;
		maxLife = Entity.rand.nextInt(100-60)+60;
		life = maxLife;
		dentroParede();
	}
	
	public void tick() {
		// queda
		vspd+=gravity;
		if (life <= 0) {
			Game.entities.remove(this);
			Game.inimigos.remove(this);
		}
		if(!Mundo.isFree((int)x,(int)(y+vspd))) {
			
			int signVsp = 0;
			if(vspd >= 0){
				signVsp = 1;
			}else  {
				signVsp = -1;
			}
			while(Mundo.isFree((int)x,(int)(y+signVsp))) {
				y = y+signVsp;
			}
			vspd = 0;
		}else {
			y = y + vspd;
		}
		if (dir == 1) {
			if(!Mundo.isFree((int)(x+speed),(int)y)) {
				while(Mundo.isFree((int)(x+1),(int)y)) {
					x += 1;
				}
				dir = rand.nextInt(2)-1;
				if (dir == -1) {
					right = false;
					left = true;
				}else {
					maxQuebra = rand.nextInt((int) (Game.amountOfTicks*5));
				}
			}else {
				x += speed;
			}
		}else if (dir == -1) {
			if(!Mundo.isFree((int)(x-speed),(int)y)) {
				while(Mundo.isFree((int)(x-1),(int)y)) {
					x -= 1;
				}
				dir = rand.nextInt(2);
				if (dir == 1) {
					left = false;
					right = true;
				}else {
					maxQuebra = rand.nextInt((int) (Game.amountOfTicks*5));
				}
			}else {
				x -= speed;
			}
		}else if(dir == 0) {
			quebrarticks++;
			if (quebrarticks > maxQuebra) {
				quebrarticks = 0;
				if (left) {
					dir = -1;
					if (!(Mundo.tiles[(getX()/Game.TS - 1)+(this.getY()/Game.TS)*Mundo.WIDTH].solid)) {
						Mundo.tiles[(getX()/Game.TS - 1)+(this.getY()/Game.TS)*Mundo.WIDTH] = new FloorTile((getX()/16 - 1)*Game.TS, getY(), Tile.TILE_Fundo);
					}
				}else if (right) {
					dir = 1;
					if (!(Mundo.tiles[(getX()/Game.TS + 1)+(this.getY()/Game.TS)*Mundo.WIDTH].solid)) {
						Mundo.tiles[(getX()/Game.TS + 1)+(this.getY()/Game.TS)*Mundo.WIDTH] = new FloorTile((getX()/16 + 1)*Game.TS, getY(), Tile.TILE_Fundo);
					}
				}
				Mundo.tiles[(getX()/Game.TS + 1)+(this.getY()/Game.TS)*Mundo.WIDTH].ceuLivre();
			}
		}
	}
	
	public void render(Graphics g) {
		if (dir == 0) {
			g.setColor(Color.pink);
		}
		else {
			g.setColor(Color.red);
		}
		g.fillRect(getX()-Camera.x, getY()-Camera.y, Game.TS, Game.TS);
		
		g.setColor(Color.red);
		g.fillRect(getX() - Camera.x, getY()-Camera.y-Game.TS/2, Game.TS, 5);
		g.setColor(Color.green);
		g.fillRect(getX() - Camera.x, getY()-Camera.y-Game.TS/2, (int) ((life/maxLife)*Game.TS), 5);
	}
}
