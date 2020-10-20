package entities;

import java.awt.image.BufferedImage;

import main.Game;
import world.Mundo;

public class torreControle extends Entity{
	
	public boolean isPressed = false, pode;
	public int xT, yT;
	
	public torreControle(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		int xx = (xT/16)*16, yy = (yT/16)*16;
		if(isPressed) {
			for (int i = 0; i < Game.entities.size() && pode; i++) {
				Entity e = Game.entities.get(i);
				if (e instanceof torre1 && e.getX() == xx && e.getY() == yy) {
					pode = false;
				}
			}
			if (pode) {
				if (!Mundo.isFree(xx, yy) && Game.pontos >= 5) {
					Game.pontos -= 5;
					torre1 novaTorre = new torre1(xx, yy, Game.TS, Game.TS, 0, Game.spritesheet.getSprite(5*Game.TS, 0*Game.TS, Game.TS, Game.TS));
					Game.entities.add(novaTorre);
					Game.torres1.add(novaTorre);
				}
			}
			pode = true;
			isPressed = false;
		}
	}
	
}
