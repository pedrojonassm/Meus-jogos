package main;

import java.awt.image.BufferedImage;

import entities.Enemy;
import entities.Entity;
import world.Camera;
import world.Mundo;

public class EnemySpawn {
	public int intervalo = (int) (Game.amountOfTicks*6);
	public int curTime = 0;
	public void tick() {
		curTime++;
		if (curTime == intervalo) {
			curTime = 0;
			// estão nascendo alto demais
			Enemy enemy = new Enemy(Entity.rand.nextInt(Game.WIDTH)+Camera.x, Entity.rand.nextInt(Game.HEIGHT)+Camera.y, Game.TS, Game.TS, 2, null);
			if (enemy.getX() < Game.TS) {
				enemy.setX(Game.TS);
			}
			else if (enemy.getX() > Mundo.HEIGHT*Game.TS) {
				enemy.setX(Mundo.HEIGHT*(Game.TS-1));
			}
			if (enemy.getY() < Game.TS) {
				enemy.setY(Game.TS);
			}
			else if (enemy.getX() > Mundo.HEIGHT*Game.TS) {
				enemy.setY(Mundo.HEIGHT*(Game.TS-1));
			}
			
			Game.entities.add(enemy);
			Game.inimigos.add(enemy);
		}
	}
}
