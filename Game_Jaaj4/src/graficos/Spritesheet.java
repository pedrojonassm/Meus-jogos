package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {

	private BufferedImage spritesheet;
	
	public Spritesheet(String path)
	{
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x,int y,int width,int height){
		return spritesheet.getSubimage(x, y, width, height);
	}
	
	public BufferedImage getNave(int x, int y){
		return spritesheet.getSubimage(16+x*20, 16+y*20, 16, 16);
		//X: 0 = 29, 1 = 49, 2 = 69
	}
	
}
