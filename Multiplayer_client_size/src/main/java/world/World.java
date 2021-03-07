package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

import Inventario.Bag;
import entities.Entity;
import itens.Item;
import main.Client;
import main.Output;

public class World {
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static Stack<Item> itens_no_chao;
	public static Bag bag;
	
	public World(String path) {
		try {
			BufferedImage mapa = ImageIO.read(getClass().getResource(path));
			WIDTH = mapa.getWidth();
			HEIGHT = mapa.getHeight();
			int[] pixels = new int[WIDTH*HEIGHT];
			tiles = new Tile[WIDTH*HEIGHT];
			mapa.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			for(int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					//tiles[xx + yy*WIDTH] = new floorTile(xx*Client.TS, yy*Client.TS, Tile.TILE_FLOOR);
					int pixelAtual = pixels[xx + yy*WIDTH];
					if (pixelAtual == 0xFF000000) { // cor preta
						// colocar o chão
						tiles[xx + yy*WIDTH] = new FloorTile(xx*Client.TS, yy*Client.TS, Tile.TILE_FLOOR);
					}
					else if((pixelAtual == 0xFFFFFFFF)){//cro branca
						// Parede
						tiles[xx + yy*WIDTH] = new WallTile(xx*Client.TS, yy*Client.TS, Tile.TILE_WALL);
						
					}
				}
			}
			bag = new Bag(0, 0, 0, 500);
			itens_no_chao = new Stack<Item>();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		return !((tiles[x1+(y1*World.WIDTH)] instanceof WallTile) ||
				tiles[x2+(y2*World.WIDTH)] instanceof WallTile||
				tiles[x3+(y3*World.WIDTH)] instanceof WallTile ||
				tiles[x4+(y4*World.WIDTH)] instanceof WallTile);
	}
	
	public static Tile pegar_chao(int mx, int my) {
		return tiles[(mx >> 6) + (my>>6)*WIDTH]; // 6 pq TS é 64
	}
	
	public void render(Graphics g) {
		int xstart = (int) Camera.x >> 6; // 2^x = TS (no caso, é 6 já que TS é 64) seria o mesmo que dividir por 64
		int ystart = (int) Camera.y >> 6;
		int xfinal = (int) (xstart + Client.WIDTH/Client.TS) + 1; // +1 para sempre renderizar 1 a mais para os lados, dessa forma não ocorrerá de aparecer um bug visual no canto inferior e esquerdo da tela
		int yfinal = (int) (ystart + Client.HEIGHT/Client.TS) + 1;
		/*
		System.out.println("xs: "+xstart+ " xf: "+xfinal+" cX: "+Camera.x);
		System.out.println("ys: "+ystart+ " yf: "+yfinal+" cY: "+Camera.y);//*/
		for(int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + yy*WIDTH];
				tile.render(g);
			}
		}
		
		for (Item i : itens_no_chao) {
			if (i != null)	if (Entity.toRender(i.x, i.y)) i.render(i.x-Camera.x, i.y-Camera.y, g);
		}
	}

	public static void adicionar_item(int quantidade, int tipo, int item_idBD, int x, int y, int z, int id_bag) {
		Item i = Item.criar_item(item_idBD, tipo, quantidade, id_bag);
		i.x = x;
		i.y = y;
		i.z = z;
		itens_no_chao.add(i);
		i.setBag(bag);
	}

	public void receber_itens_no_chao(DataInputStream in) throws IOException {
		int total_itens = in.readInt();
		Item item;
		for (int i =0; i < total_itens; i++) {
			item = Item.criar_item(in.readInt(), in.readInt(), in.readInt(), in.readInt());
			item.x = in.readInt();
			item.y = in.readInt();
			item.z = in.readInt();
			item.setBag(bag);
			itens_no_chao.add(item);
		}
	}

	public boolean pegou_item(int x, int y) {
		for (Item i : itens_no_chao) {
			if (i.z == Client.instance.player.getZ() && x == i.x && y == i.y && Entity.distancia(i.x, Client.instance.player.getX(), i.y, Client.instance.player.getY()) < Client.TS*2) {
				itens_no_chao.remove(i);
				Client.instance.setar_item_segurado(i);
				Output.remover_item_do_chao(i);
				return true;
			}
		}
		return false;
	}

	public static void remover_item(int item_idbd) {
		for (Item i : itens_no_chao) {
			if (i.getItemIdBD() == item_idbd) {
				itens_no_chao.remove(i);
				return;
			}
		}
	}
}
