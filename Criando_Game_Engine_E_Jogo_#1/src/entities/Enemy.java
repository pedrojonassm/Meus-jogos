package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import World.Camera;
import World.Mundo;
import main.Game;
import main.Sons;

public class Enemy extends Entity{

	private double speed = 0.8;
	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
	public int frames, maxFrames = 5, index = 0, maxIndex = 5;
	private BufferedImage[] sprites;
	private int life = Game.rand.nextInt(50);
	private boolean colidindo = false;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[6];
		int xi = 8, yi = 0;
		for (int i = 0; i < 3; i++) {
			if (xi*Game.TS >= 160) {
				xi = 0;
				yi += 1;
			}
			sprites[i] = Game.spritesheet.getSprite(xi*Game.TS, yi*Game.TS, Game.TS, Game.TS);
			xi++;
		}
		xi = 7;
		yi = 2;
		for (int i = 3; i < 6; i++) {
			sprites[i] = Game.spritesheet.getSprite(xi*Game.TS, yi*Game.TS, Game.TS, Game.TS);
			xi++;
		}
	}
	
	public void tick() {
		//if (Game.rand.nextInt(100) < 20) { usado para caso haver muitas entidades, use isso no lugar da colisão!
		if (!this.damagedPlayer()) {
			if((int) x < Game.player.getX() && Mundo.isFree((int)(x+speed),this.getY()) && !IsColliding((int) (x+speed), this.getY())) {
				x += speed;
			}else if((int) x > Game.player.getX() && Mundo.isFree((int) (x-speed),this.getY()) && !IsColliding((int) (x-speed), this.getY())) {
				x -= speed;
			}if((int) y < Game.player.getY() && Mundo.isFree(this.getX(),(int) (y+speed)) && !IsColliding(this.getX(), (int) (y+speed))) {
				y += speed;
			}else if((int) y > Game.player.getY() && Mundo.isFree(this.getX(),(int) (y-speed)) && !IsColliding(this.getX(), (int) (y-speed))) {
				y -= speed;
			}
			
			
		}
		else {
			//colidindo com o player
			if(Game.rand.nextInt(100) < 10 && !Player.isDamaged && !Player.isJumping) {
				Sons.dano.play();
				Game.player.life-=Game.rand.nextInt(3);
				Player.isDamaged = true;
			}
		}
		frames++;
		if (frames >= maxFrames) {
			frames = 1;
			index++;
			if ((index > maxIndex-3) && !colidindo) {
				index -= 3;
			}
			else if(index > maxIndex && colidindo) {
				index -= 3;
			}
		}
		
		damaged();
		
	}
	
	public void damaged() {
		for (int i = 0; i < Game.disparos.size(); i++) {
			Esfera e = Game.disparos.get(i);
			if (Entity.isColidding(this, e)) {
				e.colidindo = true;
				colidindo = true;
				e.life--;
				this.life--;
				if (this.life <= 0) {
					e.colidindo = false;
					Sons.inimigoMorrendo.play();
					Game.entities.remove(this);
					Game.inimigos.remove(this);
					return;
				}if(e.life <= 0) {
					Sons.esferaDestruida.play();
					Game.disparos.remove(e);
					colidindo = false;
					return;
				}
			}
		}
		if (colidindo && Game.disparos.size() == 0) {
			colidindo = false;
		}
	}
	
	public boolean damagedPlayer() {
		Rectangle current = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Game.TS, Game.TS);
		return current.intersects(player);
	}
	
	public boolean IsColliding(int x, int y) {
		Rectangle current = new Rectangle(x + maskx, y + masky, maskw, maskh);
		for (int i = 0;  i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if (e == this) {
				continue;
			}
			Rectangle target = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(current.intersects(target)) {
				return true;
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		/*g.setColor(Color.blue);
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);*/ //para ver a colisão
	}
	
}
