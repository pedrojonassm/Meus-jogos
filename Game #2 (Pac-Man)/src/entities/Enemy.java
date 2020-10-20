package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import main.Sons;
import world.AStar;
import world.Camera;
import world.Vector2i;



public class Enemy extends Entity{
	public boolean parado = false, morte = false, meda = false, mudanca = false;
	
	public int ghostFrames, time = Entity.rand.nextInt(5), f, frames, maxFrames=15, index=1, t, maxT = 8*60, bug, maxBug = 10*60;
	
	private static BufferedImage morto, medo = Game.spritesheet.getSprite(5*Game.TS, 2*Game.TS, 16, 16);
	
	public Enemy(int x, int y, int width, int height,int speed, BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		this.speed = rand.nextDouble();
		f = Entity.rand.nextInt(1+Game.nivel);
		this.sprite = Game.spritesheet.getSprite((6+f)*Game.TS, Game.TS, 16, 16);
		morto = Game.spritesheet.getSprite(f*Game.TS, 2*Game.TS, 16, 16);
	}

	public void tick(){
		bug++;
		if(bug > maxBug) {
			bug = 0;
			BugDestroyer bug = new BugDestroyer(getX(), getY(), Game.TS, Game.TS, 1, null, this);
			Game.entities.add(bug);
		}
		if((mudanca || !Game.player.morre) && path != null) {
			mudanca = false;
			for(int i = 0; i < path.size(); i++) {
				path.remove(0);
			}
		}
		ghostFrames++;
		if(ghostFrames == 60*time) {
			time = Entity.rand.nextInt(5);
			ghostFrames = 0;
			if(!parado && Entity.rand.nextInt(100) < 10) {
				parado=true;
				this.speed = rand.nextInt(4);
			}else {
				parado = false;
			}
		}
		if(!parado && !Game.player.morre) {
			depth = 0;
			if(path == null || path.size() == 0) {
					Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
					Vector2i end;
					if (morte) {
						//recuperar vida
						end = new Vector2i(((int)(Game.cabine.x/16)),((int)(Game.cabine.y/16)));
					}else if (meda) {
						int r = rand.nextInt(Game.entities.size());
						end = new Vector2i(((int)(Game.entities.get(r).getX()/16)),((int)(Game.entities.get(r).getY()/16)));
					}
					else {
						end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					}
					path = AStar.findPath(Game.world, start, end);
				}
			
				if(new Random().nextInt(100) < 50)
					followPath(path);
				
				if(x % 16 == 0 && y % 16 == 0) {
					if(new Random().nextInt(100) < 10) {
						Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
						Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
						path = AStar.findPath(Game.world, start, end);
					}
				}
		}
		if (meda) {
			frames++;
			t++;
			if (frames > maxFrames) {
				frames = 0;
				index *= -1;
			}
			if(t>maxT) {
				meda = false;
				t = 0;
			}
		}
		
		if (meda && isColidding(this, Game.player)) {
			morte = true;
			meda = false;
			mudanca = true;
			Game.pontos += rand.nextInt(30);
		}
		
		if (isColidding(this, Game.cabine) && morte) {
			morte = false;
		}
	}
	
	public void render(Graphics g) {
		if(morte) {
			g.drawImage(morto,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if (meda){
			if(index == 1) {
				g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
			}else {
				g.drawImage(medo,this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
		}else {
			g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
	}
	
}
