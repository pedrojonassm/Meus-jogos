package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import main.Rank;
import world.Camera;
import world.Mundo;

public class Player extends Entity{
	
	public boolean right,up,left,down, moved, morre;
	private int frames, maxFrames = 5, index = 0, maxIndex = 3, prox = 1;
	private int frames2, maxFrames2 = 10, index2 = 0, maxIndex2 = 9;
	public int r, life = 3, maxLife = 3;
	public BufferedImage[] morrendo, andando;

	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		morrendo = new BufferedImage[9];
		andando = new BufferedImage[3];
		this.speed = 2;
		for  (int i = 0; i < 3; i++) {
			andando[i] = Game.spritesheet.getSprite(Game.TS*2 + i*Game.TS, 0, Game.TS, Game.TS);
		}
		int t = 0;
		for(int i = 0; i < 9; i++) {
			if (i == 0) {
				morrendo[i] = Game.spritesheet.getSprite(Game.TS*2, 0, Game.TS, Game.TS);
			}else {
				if(i < 6) {
					morrendo[i] = Game.spritesheet.getSprite(Game.TS*(4+i), 0, Game.TS, Game.TS);
				}else {
					morrendo[i] = Game.spritesheet.getSprite(Game.TS*(i-6), Game.TS, Game.TS, Game.TS);
				}
			}
		}
	}
	
	public void tick(){
		updateCamera();
		depth = 1;
		if (!morre) {
			if(right && Mundo.isFree((int)(x+speed),this.getY())) {
				x+=speed;
				r = 0;
			}
			else if(left && Mundo.isFree((int)(x-speed),this.getY())) {
				x-=speed;
				r = 180;
			}
			if(up && Mundo.isFree(this.getX(),(int)(y-speed))){
				y-=speed;
				r = 270;
			}
			else if(down && Mundo.isFree(this.getX(),(int)(y+speed))){
				y+=speed;
				r = 90;
			}
			if (!down && !up && !left && !right) {
				moved = false;
			}
			if (!left && !right) {
				if (up)
				r = 270;
				else if (down) {
					r = 90;
				}
			}
			else if (!up && !down) {
				if (left) {
					r = 180;
				}else {
					r = 0;
				}
			}else if (left) {
				if(up) {
					r = 225;
				}else if(down) {
					r = 135;
				}
			}else if (right) {
				if(up) {
					r = 315;
				}else if(down) {
					r = 45;
				}
			}
		}else{
			frames2++;
			if(frames2> maxFrames2) {
				frames2 = 0;
				if(index2>=maxIndex2-1) {
					morre = false;
					index2 = 0;
					life--;
					if(life <= 0) {
						ArrayList<Rank> ranks = new ArrayList<Rank>();
						ranks = Rank.pegarRanks();
						if(Game.pontos > 0 && ranks.size() > 0) {
							if (Game.pontos > ranks.get(ranks.size()-1).ponto) {
								Game.GameState = "novorank";
							}
						}else {
							Game.GameState = "menu";
							Mundo.restartGame("");
						}
					}else {
						Mundo.restartGame("");
					}
				}else {
					index2++;
				}
			}
		}
		if(moved && !morre) {
			frames++;
			if(frames> maxFrames) {
				frames = 0;
				if((index>=maxIndex-1 && prox == 1) || (index<=0 && prox == -1)) {
					prox*=-1;
				}
				index+=prox;
			}
		}
		colisao();
	}
	
	public void colisao() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(Entity.isColidding(this, e)) {
				if(e instanceof Fruta) {
					Game.entities.remove(e);
					Game.comidas++;
					Game.pontos += this.rand.nextInt(20);
					return;
				}else if(e instanceof Enemy) {
					if((!((Enemy) e).morte) && !(((Enemy) e).meda)) {
						morre = true;
					}
					return;
				}else if(e instanceof Poder) {
					Game.entities.remove(e);
					for(int k = 0; k < Game.inimigos.size(); k++) {
						Enemy en = Game.inimigos.get(k);
						if(en instanceof Enemy) {
							en.meda = true;
							en.mudanca = true;
							en.t = 0;
						}
					}
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(r),this.getX()+Game.TS/2 - Camera.x,this.getY()+Game.TS/2 - Camera.y);
		if(moved && !morre) {
			g2.drawImage(andando[index], this.getX() - Camera.x, this.getY() - Camera.y,null);
		}else if(morre) {
			g2.drawImage(morrendo[index2], this.getX() - Camera.x, this.getY() - Camera.y,null);
		}else {
			g2.drawImage(andando[0], this.getX() - Camera.x, this.getY() - Camera.y,null);
		}
		g2.rotate(-Math.toRadians(r),this.getX()+Game.TS/2 - Camera.x,this.getY()+Game.TS/2 - Camera.y);
	}
}
