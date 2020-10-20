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
			if (right && Mundo.isFree((int) (x + speed), this.getY())) {
				x += speed;
			} else if (left && Mundo.isFree((int) (x - speed), this.getY())) {
				x -= speed;
			}
			if (down && Mundo.isFree(this.getX(), (int) (x + speed))) {
				y += speed;
			} else if (up && Mundo.isFree(this.getX(), (int) (y - speed))) {
				y -= speed;
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
