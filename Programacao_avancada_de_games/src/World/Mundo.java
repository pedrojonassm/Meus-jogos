package World;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.*;
import graficos.spriteSheet;
import main.Game;
import main.Sons;

public class Mundo {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT; 
	private boolean arma = false, player = false, inimigos = false;
	
	public static boolean isFree(int xnext, int ynext) {
		/*int x1 = xnext/Game.TS;
		int y1 = ynext/Game.TS;
		
		int x2 = (xnext+Game.TS-1)/Game.TS;
		int y2 = ynext/Game.TS;
		
		int x3 = xnext/Game.TS;
		int y3 = (ynext+Game.TS-1)/Game.TS;
		
		int x4 = (xnext+Game.TS-1)/Game.TS;
		int y4 = (ynext+Game.TS-1)/Game.TS;
		return !((tiles[x1+(y1*Mundo.WIDTH)] instanceof wallTile) ||
				tiles[x2+(y2*Mundo.WIDTH)] instanceof wallTile||
				tiles[x3+(y3*Mundo.WIDTH)] instanceof wallTile ||
				tiles[x4+(y4*Mundo.WIDTH)] instanceof wallTile);*/
		
		int x = (Game.player.getX()/Game.TS)-1, y = (Game.player.getY()/Game.TS)-1;
		
		for(int xx = x; xx < x+3; xx++) {
			for (int yy = y; yy < y+3; yy++) {
				if(yy < 0 || xx < 0 || xx>= Mundo.WIDTH || yy >= Mundo.HEIGHT) {
					continue;
				}
				if(tiles[xx+yy*Mundo.WIDTH] instanceof wallTile) {
					if(pixelPerfectColision(Game.player.lastimage, Game.player.getX(), Game.player.getY(),tiles[xx+yy*Mundo.WIDTH].sprite,tiles[xx+yy*Mundo.WIDTH].x,tiles[xx+yy*Mundo.WIDTH].y)) {
						if (Game.player.up && tiles[xx+yy*Mundo.WIDTH].y <= Game.player.getY()) {
							return false;
						}else if (Game.player.left && tiles[xx+yy*Mundo.WIDTH].x <= Game.player.getX()){
							return false;
						}else if (Game.player.down && tiles[xx+yy*Mundo.WIDTH].y >= Game.player.getY()){
							return false;
						}else if (Game.player.right && tiles[xx+yy*Mundo.WIDTH].x >= Game.player.getX()) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {
		int x1 = xnext/width;
		int y1 = ynext/height;
		
		int x2 = (xnext+width-1)/width;
		int y2 = ynext/height;
		
		int x3 = xnext/width;
		int y3 = (ynext+height-1)/height;
		
		int x4 = (xnext+width-1)/width;
		int y4 = (ynext+height-1)/height;
		return !((tiles[x1+(y1*Mundo.WIDTH)] instanceof wallTile) ||
				tiles[x2+(y2*Mundo.WIDTH)] instanceof wallTile||
				tiles[x3+(y3*Mundo.WIDTH)] instanceof wallTile ||
				tiles[x4+(y4*Mundo.WIDTH)] instanceof wallTile);
	}
	
	public Mundo(String path) {
		/*try {
			BufferedImage mapa = ImageIO.read(getClass().getResource(path));
			WIDTH = mapa.getWidth();
			HEIGHT = mapa.getHeight();
			int[] pixels = new int[WIDTH*HEIGHT];
			tiles = new Tile[WIDTH*HEIGHT];
			mapa.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			for(int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					tiles[xx + yy*WIDTH] = new floorTile(xx*Game.TS, yy*Game.TS, Tile.TILE_FLOOR);
					int pixelAtual = pixels[xx + yy*WIDTH];
					if (pixelAtual == 0xFF000000) {
						// colocar o chão
						tiles[xx + yy*WIDTH] = new floorTile(xx*Game.TS, yy*Game.TS, Tile.TILE_FLOOR);
					}
					else if((pixelAtual == 0xFFFFFFFF)){//FFFFFF = branco, basta adicionar FF antes (se for pega pelo paint.net)
						// Parede
						tiles[xx + yy*WIDTH] = new wallTile(xx*Game.TS, yy*Game.TS, Tile.TILE_WALL);
						
					}else if(pixelAtual == 0xFF4800FF){
						//player
						Game.player.setX(xx*Game.TS);
						Game.player.setY(yy*Game.TS);
					}else if(pixelAtual == 0xFFFF0000){
						//Inimigo
						Enemy en = new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.inimigos.add(en);
					}else if(pixelAtual == 0xFF00FF11){
						//Lifepack
						Game.entities.add(new LifePack(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.LIFEPACK_EN));
					}else if(pixelAtual == 0xFFFF00E9){
						//arma
						Game.entities.add(new Weapon(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFFF3FF70){
						//Bateria
						Game.baterias.add(new Bateria(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.PILHA_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		// Mapa randomico
		mapaRandom();
	}
	
	private void mapaRandom() {
		WIDTH = 50;
		HEIGHT = 50;
		tiles = new Tile[WIDTH*HEIGHT];
		int r;
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy< HEIGHT; yy++) {
				if(xx == 0 || yy == 0 || xx == WIDTH-1 || yy == HEIGHT-1) {
					tiles[xx+yy*WIDTH] = new wallTile(xx*16, yy*16, Tile.TILE_WALL);
				}else {
					tiles[xx+yy*WIDTH] = new floorTile(xx*16, yy*16, Tile.TILE_FLOOR);
				}
			}
		}
		
		for(int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx+yy*WIDTH] instanceof floorTile) {
					r = Game.rand.nextInt(100);
					if(r < 2) {
						inimigos = true;
						Enemy en = new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.inimigos.add(en);
					}else if(r < 6) {
						Bateria b = new Bateria(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.PILHA_EN);
						Game.entities.add(b);
						Game.baterias.add(b);
					}else if(r < 10) {
						Game.entities.add(new LifePack(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.LIFEPACK_EN));
					}else if(r < 20){
						tiles[xx+yy*WIDTH] = new wallTile(xx*16, yy*16, Tile.TILE_WALL);
					}else if(!player && r < 40) {
						player = true;
						Game.player.setX(xx*Game.TS);
						Game.player.lastX = xx*Game.TS;
						Game.player.setY(yy*Game.TS);
						Game.player.lastY = yy*Game.TS;
					}
				}
			}
		}
		while (!inimigos) {
			r = Game.rand.nextInt(10);
			int x = 0;
			while(x < r) {
				for(int xx = 0; xx < WIDTH; xx++) {
					for(int yy = 0; yy < HEIGHT; yy++) {
						if(tiles[xx+yy*WIDTH] instanceof floorTile && Game.rand.nextInt(100) < 20) {
							inimigos = true;
							x++;
							Game.inimigos.add(new Enemy(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.ENEMY_EN));
						}
					}
				}
			}
		}
		while (!arma) {
			for(int xx = 0; xx < WIDTH && !arma; xx++) {
				for(int yy = 0; yy < HEIGHT && !arma; yy++) {
					if (arma) {
						continue;
					}
					else if(tiles[xx+yy*WIDTH] instanceof floorTile && Game.rand.nextInt(100) < 20) {
						arma = true;
						Game.entities.add(new Weapon(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.WEAPON_EN));
					}
				}
			}
		}
	}
	
	public static void GenerateParticles(int amount, int x, int y) {
		for (int i = 0; i < amount; i++) {
			Game.entities.add(new Particle(x,y,1,1,null));
		}
	}
	
	public static void restartGame(String level) {
		Sons.colisaoVida.stop();
		Sons.dano.stop();
		Sons.esferaAndando.stop();
		Sons.esferaDestruida.stop();
		Sons.ativandoArma.stop();
		Sons.buffInimigos.stop();
		Sons.carregandoEsfera.stop();
		Sons.colisaoBateria.stop();
		Sons.esferaMaxima.stop();
		Game.inimigos = new ArrayList<Enemy>();
		Game.entities = new ArrayList<Entity>();
		Game.spritesheet = new spriteSheet("/spritesheet.png");
		Game.player = new Player(0,0,Game.TS,Game.TS,Game.spritesheet.getSprite(2*Game.TS, 0, Game.TS, Game.TS));
		Game.entities.add(Game.player);
		Game.mundo = new Mundo("/"+level);
		Game.player.ammo = 0;
		Game.player.life = 100;
		Game.player.disparando = false;
		Game.player.mdisparando = false;
		return;
	}
	
	public void render(Graphics g) {
		int xstart = (int) Camera.x>>4;
		int ystart = (int) Camera.y>>4;
		int xfinal = (int) xstart + Game.WIDTH/16;
		int yfinal = (int) ystart + Game.HEIGHT/16;
		for(int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + yy*WIDTH];
				tile.render(g);
			}
		}
	}

	public static void renderMinimap() {
		for(int i = 0; i< Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0; //preto
		}
		for(int xx=0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				Tile t = tiles[xx + yy*WIDTH];
				if(t instanceof wallTile && t.show) {
					Game.minimapaPixels[xx + yy*WIDTH] = 0xff1122ff;
				}
			}
		}
		
		int xPlayer = Game.player.getX()/16, yPlayer = Game.player.getY()/16;
		Game.minimapaPixels[xPlayer + yPlayer*WIDTH] = 0;
		for (int i = 0; i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if(e.visto) {
				Game.minimapaPixels[e.getX()/16 + (e.getY()/16)*WIDTH] = 0xffff0000;
			}
		}
	}

	public static void maisbaterias() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy< HEIGHT; yy++) {
				if(tiles[xx+yy*WIDTH] instanceof floorTile) {
					Game.baterias.add(new Bateria(xx*Game.TS, yy*Game.TS, Game.TS, Game.TS, Entity.PILHA_EN));
				}
			}
		}
	}
	public static boolean pixelPerfectColision(BufferedImage sprite1, int x1, int y1, BufferedImage sprite2, int x2, int y2) {
		int[] pixels1 = new int[sprite1.getWidth()*sprite1.getHeight()];
		int[] pixels2 = new int[sprite2.getWidth()*sprite2.getHeight()];
		sprite1.getRGB(0, 0, sprite1.getWidth(), sprite1.getHeight(), pixels1, 0, sprite1.getWidth());
		sprite2.getRGB(0, 0, sprite2.getWidth(), sprite2.getHeight(), pixels2, 0, sprite2.getWidth());
		//Transparent = 0x00ffffff
		for(int xx1 = 0; xx1 < sprite1.getWidth(); xx1++) {
			for (int yy1 = 0; yy1 < sprite1.getHeight(); yy1++) {
				for(int xx2 = 0; xx2 < sprite2.getWidth(); xx2++) {
					for (int yy2 = 0; yy2 < sprite2.getHeight(); yy2++) {
						int pixelAtual1 = pixels1[xx1+yy1*sprite1.getWidth()];
						int pixelAtual2 = pixels1[xx2+yy2*sprite2.getWidth()];
						if(pixelAtual1 == 0x00ffffff || pixelAtual2 == 0x00ffffff) {
							continue;
						}
						if (xx1+x1 == xx2+x2 && yy1+y1 == yy2+y2) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
}
