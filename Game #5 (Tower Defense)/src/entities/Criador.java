package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Criador extends Entity{
	private int contador, limite = 180;
	
	
	public Criador(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}
	
	public void tick() {
		contador++;
		if (contador>=limite) {
			limite = Entity.rand.nextInt(180) + 60;
			contador = 0;
			Enemy novo = new Enemy(this.getX(), this.getY(), Game.TS, Game.TS, 2, null);
			Game.inimigos.add(novo);
			Game.entities.add(novo);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(this.getX(),this.getY(),16,16);
	}
}
