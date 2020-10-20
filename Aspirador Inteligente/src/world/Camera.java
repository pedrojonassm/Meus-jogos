package world;

import main.Game;

public class Camera {

	public static int x = 0, y = 0;
	public static boolean left = false, right = false, up = false, down = false;
	private static int speed = 3;
	
	public static int clamp(int Atual,int Min,int Max){
		if(Atual < Min){
			Atual = Min;
		}
		
		if(Atual > Max) {
			Atual = Max;
		}
		
		return Atual;
	}
	
	public static void tick() {
		if (up) {
			for (int i = 1; i <= speed; i++) {
				if (y > 0) {
					y -= 1;
				}
			}
		}else if (down) {
			for (int i = 1; i <= speed; i++) {
				if (y < Mundo.HEIGHT*Game.TS - Game.HEIGHT) {
					y += 1;
				}
			}
		}
		if (left) {
			for (int i = 1; i <= speed; i++) {
				if (x > 0) {
					x -= 1;
				}
			}
		}else if (right) {
			for (int i = 1; i <= speed; i++) {
				if (x < Mundo.WIDTH*Game.TS - Game.WIDTH) {
					x += 1;
				}
			}
		}
	}
	
}
