package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;

public class UI {
	private int bv = 200; //bv = barra de vida
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 10, bv, 30);
		g.setColor(Color.green);
		g.fillRect(10, 10, (int) ((Game.player.life/Game.player.maxLife)*bv), 30);
		g.setColor(Color.white);
		g.drawRect(10, 10, bv, 30);
		g.setFont(new Font("Arial",12,12));
		g.setColor(Color.black);
		String pontos = "Pontos: "+Game.pontos;
		int w1 = g.getFontMetrics().stringWidth(pontos);
		g.drawString(pontos, Game.WIDTH*Game.SCALE-w1, 28);
	}
	
}
