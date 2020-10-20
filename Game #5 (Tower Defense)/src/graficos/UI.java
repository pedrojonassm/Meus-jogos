package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;

public class UI {
	private int bv = 100; //bv = barra de vida
	public void render(Graphics g) {
		g.setColor(Color.white);
		String pontos = "Grana: $"+Game.pontos;
		g.setFont(new Font("Arial", 18, 18));
		int w1 = g.getFontMetrics().stringWidth(pontos);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w1, 18);
	}
	
}
