package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class BugDestroyer extends Entity{
	private int time, maxTime = 4*60;
	private Enemy alvo;
	private boolean destruir = false;
	
	public BugDestroyer(double x, double y, int width, int height, double speed, BufferedImage sprite, Enemy alvo) {
		super(x, y, width, height, speed, sprite);
		this.alvo = alvo;
	}
	
	public void tick() {
		time++;
		if(time > maxTime - 60 && !destruir) {
			destruir = true;
			if(time > maxTime)
			Game.entities.remove(this);
		}else if (isColidding(this, alvo) && destruir && !Game.player.morre) {
			Game.entities.remove(alvo);
			Game.inimigos.remove(alvo);
			Game.entities.remove(this);
		}
	}
}
