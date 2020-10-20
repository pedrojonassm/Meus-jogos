package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import main.Game;
import main.Rank;
import world.Camera;
import world.FloorTile;
import world.Mundo;

public class Player extends Entity {

	public boolean right, left, moved, morre, caindo, salto = false, attack = false, isAttacking = false;
	public char lk; //lk = last key --> u, d, r, l
	private int frames, maxFrames = 5, index = 0, maxIndex = 8,attackFrames, attackMaxFrames = 20, direction;
	public static double life = 100, maxLife = 100;
	public BufferedImage attackRight, attackLeft;
	private double vspd = 0;
	
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		attackRight = Game.spritesheet.getSprite(6*Game.TS, 0, Game.TS, Game.TS);
		attackLeft = Game.spritesheet.getSprite(7*Game.TS, 0, Game.TS, Game.TS);;
		this.speed = 2;
		this.gravity = 0.4;
		
	}

	public void tick() {
		updateCamera();
		depth = 1;
		if (!morre) {
			if (right) {
				direction = 1;
			}else if (left){
				direction = -1;
			}
			if (right && Mundo.isFree((int) (x + speed), this.getY())) {
				x += speed;
			} else if (left && Mundo.isFree((int) (x - speed), this.getY())) {
				x -= speed;
			}
			//System.out.println(gravity);
			vspd+=gravity;
			
			if (!Mundo.isFree(getX(), getY()+1) && salto) {
				vspd = -8;
				caindo = true;
				salto = false;
			}
			else {
				
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
					caindo = false;
				}
				y = y + vspd;
				
			}
			if (!left && !right) {
				moved = false;
			}
		} else {
			life--;
			if (life <= 0) {
				ArrayList<Rank> ranks = new ArrayList<Rank>();
				ranks = Rank.pegarRanks();
				if (Game.pontos > 0 && ranks.size() > 0) {
					if (Game.pontos > ranks.get(ranks.size() - 1).ponto) {
						Game.GameState = "novorank";
					}
				} else {
					Game.GameState = "menu";
					Mundo.restartGame("");
				}
			} else {
				Mundo.restartGame("");
			}
		}
		
		
		if (moved && !morre) {
			frames++;
			if (frames > maxFrames) {
				index++;
				frames = 0;
				if (index >= maxIndex) {
					index = 3;
				}
			}
		}
		
		if (attack && !isAttacking) {
			isAttacking = true;
		}
		
		if (isAttacking) {
			attackFrames++;
			if (attackFrames == attackMaxFrames) {
				attackFrames = 0;
				isAttacking = false;
			}
		}
		
		iluminar();
		colisao();
	}
	
	public void iluminar() {
			int tileX = getX()/16, tileY = getY()/16;
			int maxDistance = 6;
			for (int d = 0; d < maxDistance; d++) {
				for (int ty = d*-1; ty <= d; ty++) {
					if (tileY + ty < Mundo.HEIGHT && tileY+ty > 0) {
						for (int tx = d*-1; tx<=d; tx++) {
							if (tileX + tx < Mundo.WIDTH && tileX+tx > 0) {
								if (ty == d*-1 || tx == d*-1 || ty == d || tx == d) {
									if (Mundo.tiles[(tileX+tx)+(tileY+ty)*Mundo.WIDTH].transparence > d*(255/maxDistance)) {
										Mundo.tiles[(tileX+tx)+(tileY+ty)*Mundo.WIDTH].transparence = (int) d*(255/maxDistance);
									}
								}
							}
						}
					}
						
				}
			}
	}

	public void colisao() {
		for (int i = 0; i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if (Entity.rand.nextInt(100) < 30) {
				if(Entity.isColidding(this, e)) {
					if(isAttacking) {
						e.life--;
						System.out.println("asdasd");
					}else {
						life -= 0.5;
					}
				}
			}
		}
	}

	public void render(Graphics g) {
			g.setColor(Color.blue);
			g.fillRect(getX()-Camera.x, getY()-Camera.y, Game.TS, Game.TS);
			if (isAttacking) {
				if (direction == 1) {
					g.drawImage(attackRight, getX()-Camera.x + 8, getY()-Camera.y, null);
				}else if (direction == -1) {
					g.drawImage(attackLeft, getX()-Camera.x - 8, getY()-Camera.y, null);
				}
			}
			
	}
}
