package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import World.Camera;
import World.Mundo;
import main.Game;
import main.Sons;

public class Player extends Entity{
	
	public boolean right, up, left, down, moved;
	public double speed = 1.4;
	public int lastkey = KeyEvent.VK_A, frames, maxFrames = 5,frames2, maxFrames2 = 10, index, maxIndex = 2, damaged, lastkey2=lastkey;
	private BufferedImage[] rightPlayer, leftPlayer, damagedPlayer;
	public double life = 100, maxlife = 100;
	public static int ammo = 0;
	public static boolean isDamaged = false;
	public boolean arma = false, som = false, jump = false;
	public static boolean isJumping = false;
	public boolean jumpUp = false;
	public boolean jumpDown = false;
	public static boolean disparando = false, mdisparando = false, municao = false;
	public Esfera disparo;
	public int px, py,mx, my, z = 0, jumpHeight = 50, jumpCur = 0, jumpSpeed = 3;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		damagedPlayer = new BufferedImage[2];
		damagedPlayer[0] = Game.spritesheet.getSprite(Game.TS*6, 1*Game.TS, Game.TS, Game.TS);
		damagedPlayer[1] = Game.spritesheet.getSprite(Game.TS*7, 1*Game.TS, Game.TS, Game.TS);
		for (int i = 0; i < 3; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(Game.TS*2 + i*Game.TS, 0, Game.TS, Game.TS);
			rightPlayer[i] = Game.spritesheet.getSprite(Game.TS*5 + i*Game.TS, 0, Game.TS, Game.TS);
		}
		
	}
	
	public void tick() {
		if(jump) {
			if(!isJumping) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		if(isJumping) {
			if(jumpUp) {
				jumpCur += jumpSpeed;
			}else if(jumpDown) {
				jumpCur -= jumpSpeed;
				if(jumpCur<=0) {
					isJumping = false;
					jumpDown = false;
					jumpCur = 0;
				}
			}
			z = jumpCur;
			if(jumpCur >= jumpHeight) {
				jumpUp = false;
				jumpDown = true;
				jumpCur = jumpHeight;
			}
		}
		moved = false;
		if(right && (Mundo.isFree((int)(x+speed),(int) y) || isJumping) && (getX() - Camera.x) < Game.WIDTH - Game.TS) {
			x += speed;
			moved = true;
			if (disparo!=null) disparo.x+=speed;
		}else if(left && (Mundo.isFree((int)(x-speed), (int) y) || isJumping) && this.getX() > 0) {
			x -= speed;
			moved = true;
			if (disparo!=null) disparo.x -= speed;
		}if(up && (Mundo.isFree((int) x,(int)(y - speed)) || isJumping) && this.getY() > 0) {
			y -= speed;
			moved = true;
			if (disparo!=null) disparo.y-=speed;
		}else if(down && (Mundo.isFree((int) x,(int)(y + speed)) || isJumping) && this.getY() - Camera.y < Game.HEIGHT - Game.TS) {
			y += speed;
			moved = true;
			if (disparo!=null) disparo.y+=speed;
		}
		if (moved) {
			frames++;
			if (frames >= maxFrames) {
				frames = 1;
				index++;
				if (index > maxIndex) {
					index = 1;
				}
			}
		}
		
		if (disparo != null && disparo.life >= 30) {
			frames2++;
			if (frames2 > maxFrames2) {
				disparo.index++;
				frames2 = 0;
				if(disparo.index > 3) {
					disparo.index = 0;
				}
			}
		}
		
		Weapon.atirar();
		
		if (isDamaged) {
			damaged++;
			if (damaged >= 15) {
				isDamaged=false;
				damaged = 0;
			}
			
		}
		
		if (life <= 0) {
			life = 0;
			Game.GameState = "Over";
		}
		
		this.cura();
		this.colisaobateria();
		this.pegarArma();
		
		if(Weapon.atirando && arma && !disparando) {
			//criar munição
			if(lastkey == KeyEvent.VK_A) {
				disparo = new Esfera(this.getX()-15, this.getY(), Game.TS, Game.TS, null, -1, 0);
			}else {
				disparo = new Esfera(this.getX()+15, this.getY(), Game.TS, Game.TS, null, 1, 0);
			}
			disparando = true;
			Sons.ativandoArma.play();
		}
		if(Weapon.atirando&&disparando&&ammo>0) {
			if (disparo.life < 40) {
				ammo--;
				disparo.life++;
			}
		}
		if (lastkey != lastkey2 && lastkey2 == KeyEvent.VK_A && disparo!= null) {
			disparo.x = this.getX()+15;
			lastkey2 = lastkey;
			disparo.dx=1;
		}else if(lastkey != lastkey2 && lastkey2 == KeyEvent.VK_D && disparo!=null) {
			disparo.x = this.getX()-15;
			lastkey2 = lastkey;
			disparo.dx=-1;
		}
		
		if (!Weapon.atirando && disparando && !mdisparando && arma) {//libera o disparo
			disparando = false;
			if (lastkey == KeyEvent.VK_A) {
				px = -18;
			}else {
				px = 18;
			}
			Sons.esferaSolta.play();
			Esfera e = new Esfera(this.getX()+px,this.getY()+py,Game.TS,Game.TS,null,disparo.dx,disparo.dy);
			e.life = disparo.life; 
			e.setMask(disparo.maskx, disparo.masky, disparo.mwidth, disparo.mheight);
			Game.disparos.add(e);
			disparo = null;
		}
		
		if (!Weapon.atirando && mdisparando && arma) {//libera o disparo
			mdisparando = false;
			double angle = 0;
			if (lastkey == KeyEvent.VK_D) {
				px = 18;
				angle = Math.atan2(my - (getY()+py - Camera.y),mx - (getX()+px - Camera.x));
			}else {
				px = -8;
				angle = Math.atan2(my - (getY()+py - Camera.y),mx - (getX()+px - Camera.x));
			}
			disparo.dx = Math.cos(angle);
			disparo.dy = Math.sin(angle);
			
		}
		updateCamera();
		
		if (ammo <= 0 && municao) {
			Sons.municaoBaixa.play();
			municao = false;
		}
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - Game.WIDTH/2, 0, Mundo.WIDTH*Game.TS - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - Game.HEIGHT/2, 0, Mundo.HEIGHT*Game.TS - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if (lastkey == KeyEvent.VK_A) {
			if (!isDamaged) {
				if (arma) {
					//desenhar a arma para a esquerda
					Weapon.desenhar(g, (int) (this.getX()-Camera.x-7), (int) (this.getY()-Camera.y+2 -z), 1);
				}
				if (!left && !(up || down)) {
					g.drawImage(leftPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
				}
				else {
					g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
				}
			}
			else {
				g.drawImage(damagedPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
			}
		}
		else if (lastkey == KeyEvent.VK_D) {
			if (!isDamaged) {
				if (arma) {
					//desenhar a arma para a direita
					Weapon.desenhar(g, (int) (this.getX()-Camera.x+7), (int) (this.getY()-Camera.y+2-z), 0);
				}
				if (!right && !(up || down)) {
					g.drawImage(rightPlayer[0], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
				}
				else {
					g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
				}
			}
			else {
				g.drawImage(damagedPlayer[1], this.getX() - Camera.x, this.getY() - Camera.y - z,null);
			}
		}
		if (disparo != null) {
			disparo.render(g);
		}
		if(isJumping) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,120));
			g2.fillOval(this.getX()-Camera.x+3, this.getY() - Camera.y+10, 10, 8);
		}
	}
	
	public void pegarArma() {
		for (int i = 0; i < Game.entities.size(); i++) { //recomendado criar uma lista para cada item caso seja um jogo grande
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon && Entity.isColidding(this, atual)) {
				arma = true;
				Sons.pegarArma.play();
				Game.entities.remove(atual);
			}
		}
	}
	
	public void cura() {
		for (int i = 0; i < Game.entities.size(); i++) { //recomendado criar uma lista para cada item caso seja um jogo grande
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColidding(this, atual)) {
					if (life < 100) {
						if(!som) {
							Sons.colisaoVida.play();
							som = true;
						}
						((LifePack) atual).cura -= 1;
						life+=1;
					}
					if(((LifePack) atual).cura <= 0) {
						Game.entities.remove(atual);
					}
				}else {
					som = false;
				}
			}
		}
	}
	
	public void colisaobateria(){
		for (int i = 0; i < Game.entities.size(); i++) { //recomendado criar uma lista para cada item caso seja um jogo grande
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bateria) {
				if(Entity.isColidding(this, atual)) {
						ammo+=Game.rand.nextInt(100);
						municao = true;
						Game.entities.remove(atual);
				}
			}
		}
	}
}
