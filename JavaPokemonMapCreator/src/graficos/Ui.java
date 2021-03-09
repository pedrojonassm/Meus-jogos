package graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ui {
	
	public static boolean mostrar, colocar_parede;
	private Rectangle colocar_paredes;
	private String colocar_as_paredes = "setar paredes";
	
	public Ui() {
		mostrar = true;
		colocar_parede = false;
		colocar_paredes = new Rectangle(200, 20, 10, 10);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if (mostrar) {
			g.setColor(Color.white);
			if (colocar_parede) g.fillRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			else g.drawRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			g.drawString(colocar_as_paredes, colocar_paredes.x+colocar_paredes.width*2, colocar_paredes.y+colocar_paredes.height);
		}
	}

	public boolean clicou(int x, int y) {
		if (colocar_paredes.contains(x, y)) {
			colocar_parede = !colocar_parede;
			return true;
		}
		return false;
	}

}
