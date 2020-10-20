package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import World.AStar;
import World.Camera;
import World.Mundo;
import World.Vector2i;
import main.Game;
import main.Sons;

public class Enemy extends Entity{

	//private double speed = 0.8, gox, goy;
	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
	public int frames, maxFrames = 5, index = 0, maxIndex = 5;
	private BufferedImage[] sprites;
	private int life = Game.rand.nextInt(50), i = 0, o;
	private boolean colidindo = false;
	public boolean visto = false;
	
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
		depth = 1;
	}
	
	public void tick() {
		//if (Game.rand.nextInt(100) < 20) { usado para caso haver muitas entidades, use isso no lugar da colisão!
		/*
		if (!damagedPlayer() && this.distancia(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 60) {
			if((int) x < Game.player.getX() && Mundo.isFree((int)(x+speed),this.getY()) && !IsColliding((int) (x+speed), this.getY())) {
				x += speed;
			}else if((int) x > Game.player.getX() && Mundo.isFree((int) (x-speed),this.getY()) && !IsColliding((int) (x-speed), this.getY())) {
				x -= speed;
			}if((int) y < Game.player.getY() && Mundo.isFree(this.getX(),(int) (y+speed)) && !IsColliding(this.getX(), (int) (y+speed))) {
				y += speed;
			}else if((int) y > Game.player.getY() && Mundo.isFree(this.getX(),(int) (y-speed)) && !IsColliding(this.getX(), (int) (y-speed))) {
				y -= speed;
			}
		}else if (!damagedPlayer()){
			if (i <= 0) {
				i = 25;
				o = Game.rand.nextInt(150);
				if(o < 50) {
					gox = speed;
				}else if(o < 100){
					gox = (speed*-1);
				}else {
					gox = 0;
				}
				o = Game.rand.nextInt(150);
				if(o < 50) {
					goy=speed;
				}else if(o < 100){
					goy=(speed*-1);
				}else {
					goy = 0;
				}
			}
			else {
				if(Mundo.isFree((int)(x+gox),this.getY()) && !IsColliding((int) (x+gox), this.getY())) {
					x += gox;
				}
				if(o < 50 && Mundo.isFree(this.getX(),(int) (y+goy)) && !IsColliding(this.getX(), (int) (y+goy))) {
					y+=goy;
				}
				if((!Mundo.isFree(this.getX(),(int) (y+goy)) && !Mundo.isFree((int)(x+gox),this.getY())) || IsColliding(this.getX(), (int) (y+goy)) && IsColliding(this.getX(), (int) (x+gox))){
					i = 0;
				}else {
					i--;
				}
			}
		}
		*/
		if(distancia(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 60) {
			this.visto = true;
		}
		//AStar algoritmo
		if(!damagedPlayer()) {
			if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i( (int)(this.getX()/16),(int)this.getY()/16);
				Vector2i end = new Vector2i( (int)(Game.player.lastX/16),(int)Game.player.lastY/16);
			path = AStar.findPath(Game.mundo, start, end);
			}
			followPath(path);
		}
		//colidindo com o player
		else if(Game.rand.nextInt(100) < 10 && !Player.isDamaged && !Player.isJumping && this.damagedPlayer()) {
			Sons.dano.play();
			Game.player.life-=Game.rand.nextInt(3);
			Player.isDamaged = true;
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
				this.life--;
				if (this.life <= 0) {
					e.colidindo = false;
					Sons.inimigoMorrendo.play();
					Game.entities.remove(this);
					Game.inimigos.remove(this);
					return;
				}
			}
		}
		if (colidindo && Game.disparos.size() == 0) {
			colidindo = false;
		}
	}
	
	public boolean damagedPlayer() {
		/*Rectangle current = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Game.TS, Game.TS);
		return current.intersects(player);*/
		return Mundo.pixelPerfectColision(this.sprites[0], this.getX(), this.getY(), Game.player.lastimage, Game.player.getX(), Game.player.getY());
	}
	
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		/*g.setColor(Color.blue);
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);*/ //para ver a colisão
	}
	
}
