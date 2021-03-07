package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Client;

public class SpriteSheet {
	// tem 40 sprites por linha no sprite de itens
	// tem 10 sprites por linha nos sprites de pokemons
	private BufferedImage spritesheet;
	private int tamanho, quadrados, spritesPorSkin;
	public SpriteSheet(String path, int t, int spr) {
		tamanho = t;
		spritesPorSkin = spr;
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
			quadrados = spritesheet.getWidth()/tamanho;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getAsset(int x,int y){
		return pegarSprite(x + y*tamanho);
	}
	
	public BufferedImage pegarSprite(int position){
        return spritesheet.getSubimage((position%tamanho)*tamanho, (position/tamanho)*tamanho, tamanho, tamanho);
    }
	
    public int getQuadrados() {
        return quadrados;
    }

    public int getTamanho() {
        return tamanho;
    }
    
    public int getSpritesPorSkin() {
		return spritesPorSkin;
	}
}