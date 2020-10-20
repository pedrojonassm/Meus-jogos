package main;

import java.awt.Color;
import java.awt.Graphics;

import world.Camera;
import world.FloorTile;
import world.Mundo;
import world.Tile;
import world.WallTile;

public class Inventory {
	// inventario de 5 slots onde cada slot possui 36x36 pixels
	public int hotBarSlots = 5, tamanhoHotBarSlots = 36, hotBarIniPos = Game.WIDTH*Game.SCALE/2 - hotBarSlots*tamanhoHotBarSlots/2;
	private int[] items = {1, 2, 3, 4, 0}; // id dos itens
	
	public int select = 0, mx, my; //MouseX and MouseY
	public boolean rodaDown = false, rodaUp = false, mousePressed;
	public String mouseAction = "";
	
	public void tick() {
		if (rodaDown) {
			rodaDown = false;
			select--;
			if (select < 0) {
				select = hotBarSlots -1;
			}
		}else if(rodaUp) {
			rodaUp = false;
			select++;
			if (select >= hotBarSlots) {
				select = 0;
			}
		}
		if (mousePressed) {
			int tileX = (mx+Camera.x)/Game.TS;
			int tileY = (my+Camera.y)/Game.TS;
			if (mouseAction == "destroy") {
				if (Mundo.tiles[tileX+tileY*Mundo.WIDTH].solid == false) {
					Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new FloorTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Fundo);
					Mundo.tiles[tileX+tileY*Mundo.WIDTH].ceuLivre();
				}
			}else if (mouseAction == "use") {
				if (Mundo.tiles[tileX+tileY*Mundo.WIDTH] instanceof FloorTile) {
					if (items[select] == 1) {
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new WallTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Terra);
						Mundo.tiles[tileX+tileY*Mundo.WIDTH].sombra();
					}else if (items[select] == 2) {
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new WallTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Areia);
						Mundo.tiles[tileX+tileY*Mundo.WIDTH].sombra();
					}else if (items[select] == 3){
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new WallTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Pedra);
						Mundo.tiles[tileX+tileY*Mundo.WIDTH].sombra();
					}else if (items[select] == 4){
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new WallTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Luminosa);
						Mundo.tiles[tileX+tileY*Mundo.WIDTH].brilhante = 0;
						Mundo.tiles[tileX+tileY*Mundo.WIDTH].sombra();
					}
				}
			}
		}
		
	}
	
	public void render(Graphics g) {
		
		for (int i = 0; i < hotBarSlots; i++) {
			g.setColor(Color.gray);
			if (i == select) {
				g.setColor(Color.green);
			}
			g.fillRect(hotBarIniPos+tamanhoHotBarSlots*i, Game.HEIGHT*Game.SCALE-tamanhoHotBarSlots, tamanhoHotBarSlots, tamanhoHotBarSlots);
			if (items [i] == 0) {
				
			}else if (items[i] == 1) {
				g.drawImage(Tile.TILE_Terra, hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2, null);
			}else if (items[i] == 2) {
				g.drawImage(Tile.TILE_Areia, hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2, null);
			}else if (items[i] == 3) {
				g.drawImage(Tile.TILE_Pedra, hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2, null);
			}else if (items[i] == 4) {
				g.drawImage(Tile.TILE_Luminosa, hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2, null);
			}
			g.setColor(Color.black);
			g.drawRect(hotBarIniPos+tamanhoHotBarSlots*i, Game.HEIGHT*Game.SCALE-tamanhoHotBarSlots, tamanhoHotBarSlots, tamanhoHotBarSlots);
		}
	}

}
