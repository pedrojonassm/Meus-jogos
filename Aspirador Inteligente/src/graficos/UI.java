package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;
import world.Camera;

public class UI {
	private int bv = 200; //bv = barra de vida
	public int s = 0, min = 0, frames = 0;
	
	
	public void tick() {
		frames++;
		if (frames >= Game.amountOfTicks) {
			frames = 0;
			s++;
			if (s == 60) {
				s = 0;
				min++;
			}
		}
	}
	public void render(Graphics g) {
		g.setFont(new Font("Arial",12,12));
		g.setColor(Color.black);
		String pontos = "Tempo de jogo: ";
		if (min < 10) {
			pontos += "0";
		}
		pontos += min+":";
		if (s < 10) {
			pontos += "0";
		}
		pontos += s;
		int w1 = g.getFontMetrics().stringWidth(pontos);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w1, 20);
		pontos = ""+Game.amountOfTicks;
		w1 = g.getFontMetrics().stringWidth(pontos);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w1, 40);
	}
	
}
