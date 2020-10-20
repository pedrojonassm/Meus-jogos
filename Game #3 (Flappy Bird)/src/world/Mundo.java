package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Cano;
import entities.Criador;
import entities.Entity;
import entities.Player;
import graficos.Spritesheet;
import main.Game;

public class Mundo {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	private boolean player = false, inimigos = false, cabine = false;

	public Mundo(String path) {
		path = "/level1.png";
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {
						// Floor
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFFFFFFFF) {
						// Parede
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == 0xFFFF0000) {
						Criador criador = new Criador(Game.WIDTH-16, yy * 16, 16, 16, 0, null);
						Game.entities.add(criador);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// mapaRandom();
	}

	private void mapaRandom() {
		WIDTH = 50;
		HEIGHT = 50;
		tiles = new Tile[WIDTH * HEIGHT];
		int r;
		Game.player.setX(Game.WIDTH / 3);
		Game.player.setY(Game.HEIGHT / 3);
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if (xx == 0 || yy == 0 || xx == WIDTH - 1 || yy == HEIGHT - 1) {
					tiles[xx + yy * WIDTH] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
				} else {
					tiles[xx + yy * WIDTH] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
				}
			}
		}

		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if (tiles[xx + yy * WIDTH] instanceof FloorTile) {
					r = Entity.rand.nextInt(100);
					if (r < 2) {
						// gerar canos
					}
				}
			}
		}
	}

	public static boolean isFree(int xnext, int ynext) {

		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * Mundo.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * Mundo.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * Mundo.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * Mundo.WIDTH)] instanceof WallTile));
	}

	public static boolean pixelPerfectColision(BufferedImage sprite1, int x1, int y1, BufferedImage sprite2, int x2,
			int y2) {
		int[] pixels1 = new int[sprite1.getWidth() * sprite1.getHeight()];
		int[] pixels2 = new int[sprite2.getWidth() * sprite2.getHeight()];
		sprite1.getRGB(0, 0, sprite1.getWidth(), sprite1.getHeight(), pixels1, 0, sprite1.getWidth());
		sprite2.getRGB(0, 0, sprite2.getWidth(), sprite2.getHeight(), pixels2, 0, sprite2.getWidth());
		// Transparent = 0x00ffffff
		for (int xx1 = 0; xx1 < sprite1.getWidth(); xx1++) {
			for (int yy1 = 0; yy1 < sprite1.getHeight(); yy1++) {
				for (int xx2 = 0; xx2 < sprite2.getWidth(); xx2++) {
					for (int yy2 = 0; yy2 < sprite2.getHeight(); yy2++) {
						int pixelAtual1 = pixels1[xx1 + yy1 * sprite1.getWidth()];
						int pixelAtual2 = pixels1[xx2 + yy2 * sprite2.getWidth()];
						if (pixelAtual1 == 0x00ffffff || pixelAtual2 == 0x00ffffff) {
							continue;
						}
						if (xx1 + x1 == xx2 + x2 && yy1 + y1 == yy2 + y2) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.canos.clear();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, Game.TS, Game.TS, 2,
				Game.spritesheet.getSprite(2 * Game.TS, 0, Game.TS, Game.TS));
		Game.entities.add(Game.player);
		Game.pontos = 0;
		Game.mundo = new Mundo("/level1.png");
		Game.GameState = "menu";
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
