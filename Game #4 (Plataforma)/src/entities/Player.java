package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import main.Rank;
import world.Camera;
import world.Mundo;

public class Player extends Entity {

	public boolean right, left, moved, morre, ld, grande = false, caindo, holdJump = false, salto = false; //ld = last key = direita?
	private int frames, maxFrames = 5, index = 0, maxIndex = 8;
	public static double life = 100, maxLife = 100;
	public BufferedImage[] andandod, andandoe, saltoe, saltod;
	private double gravity = 0.4;
	private double vspd = 0;
	
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		andandod = new BufferedImage[8];
		andandoe = new BufferedImage[8];
		saltoe = new BufferedImage[2];
		saltod = new BufferedImage[2];
		this.speed = 2;
		for (int i = 0; i < 8; i++) {
			andandod[i] = Game.spritesheet.getSprite(Game.TS * 2 + i * Game.TS, 0, Game.TS, Game.TS);
			andandoe[i] = Game.spritesheet.getSprite(i * Game.TS, Game.TS, Game.TS, Game.TS);
		}
		for (int i = 0; i < 2; i++) {
			saltod[i] = Game.spritesheet.getSprite(i * Game.TS, 2*Game.TS, Game.TS, Game.TS);
			saltoe[i] = Game.spritesheet.getSprite((i+8) * Game.TS, 1*Game.TS, Game.TS, Game.TS);
		}
	}

	public void tick() {
		//super.render(g);
		updateCamera();
		depth = 1;
		if (!morre) {
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
					while(Mundo.isFree((int)x,(int)(y+signVsp)) && holdJump) {
						y = y+signVsp;
					}
					vspd = 0;
					caindo = false;
				}
				y = y + vspd;
				
				for (int i = 0; i < Game.inimigos2.size(); i++) {
					Enemy2 e = Game.inimigos2.get(i);
					if (Entity.isColidding(this, e)) {
						if (!e.morrendo && !e.change) {
							if(this.getY()<e.getY()-8) {
								if (e.grande) {
									e.change = true;
								}else {
									e.morrendo = true;
									Game.pontos += Entity.rand.nextInt(20);
								}
							}else {
								if (Entity.rand.nextInt() < 5) {
									life--;
								}
							}
						}
					}
				}
				for (int i = 0; i < Game.inimigos.size(); i++) {
					Enemy e = Game.inimigos.get(i);
					if (Entity.isColidding(this, e)) {
						if (Entity.rand.nextInt() < 5) {
							life--;
						}
					}
				}
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
				frames = 0;
				if (grande) {
					if (index >= maxIndex - 1) {
						index = 3;
					}
				}else {
					if (index >= maxIndex/2 - 1) {
						index = -1;
					}
				}
				index += 1;
			}
		}
		colisao();
	}

	public void colisao() {
		/*for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
		}*/
	}

	public void render(Graphics g) {
		
		if (moved && !morre) {
			if (!caindo) {
				if (right) {
					g.drawImage(andandod[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(andandoe[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}else {
				if (ld) {
					if(grande) {
						g.drawImage(saltod[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}else {
						g.drawImage(saltod[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}
				}else {
					if(grande) {
						g.drawImage(saltoe[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}else {
						g.drawImage(saltoe[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}
				}
			}
		}else if (!moved && !morre){
			if (grande) {
				if (ld) {
					g.drawImage(andandod[4], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(andandoe[4], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}else {
				if (ld) {
					g.drawImage(andandod[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(andandoe[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
	}
}
