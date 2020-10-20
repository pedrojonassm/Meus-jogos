package graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(3, 5, 50, 5);
		g.setColor(Color.GREEN);
		g.fillRect(3, 5, (int) ((Game.player.life/Game.player.maxlife)*50), 6);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life+"/"+(int) Game.player.maxlife, 4, 11);
	}
}
