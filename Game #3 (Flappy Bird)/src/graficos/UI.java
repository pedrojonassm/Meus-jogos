package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial",12,12));
		String pontos = "Pontos: "+Game.pontos ;
		int w2 = g.getFontMetrics().stringWidth(pontos);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w2, 28);
	}
	
}
