package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial",12,12));
		String frutas = "Frutas comidas:"+Game.comidas+"/"+Game.frutas, pontos = "Pontos: "+Game.pontos ;
		int w1 = g.getFontMetrics().stringWidth(frutas), w2 = g.getFontMetrics().stringWidth(frutas);
		g.drawString(frutas, Game.WIDTH*Game.SCALE-w1, 12);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w2, 28);
	}
	
}
