package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Client;

public class Spritesheet {

	private BufferedImage spritesheet;
	private int tamanho, quadrados, spritesPorSkin;
	public Spritesheet(String path, int t) {
		tamanho = t;
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
			quadrados = spritesheet.getWidth()/tamanho;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getAsset(int position){
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