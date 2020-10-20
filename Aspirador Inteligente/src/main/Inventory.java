package main;

import java.awt.Color;
import java.awt.Graphics;

import entities.Aspirador;
import entities.Molhador;
import entities.Secador;
import world.FloorTile;
import world.Mundo;
import world.Tile;
import world.WallTile;

public class Inventory {
	// inventario de 5 slots onde cada slot possui 36x36 pixels
	private int[] items = {1, 2, 3, 4}; // id dos itens
	public int hotBarSlots = items.length, tamanhoHotBarSlots = 36, hotBarIniPos = Game.WIDTH*Game.SCALE/2 - hotBarSlots*tamanhoHotBarSlots/2;
	
	public int select = 0, mx, my; //MouseX and MouseY
	public boolean rodaDown = false, rodaUp = false, mousePressed;
	public String mouseAction = "";
	
	public void tick() {
		if (rodaDown) {
			rodaDown = false;
			select++;
			if (select >= hotBarSlots) {
				select = 0;
			}
		}else if(rodaUp) {
			rodaUp = false;
			select--;
			if (select < 0) {
				select = hotBarSlots -1;
			}
		}
		if (mousePressed) {
			int tileX = mx/Game.TS;
			int tileY = my/Game.TS;
			if (mouseAction == "destroy") {
				for (int i = 0; i < Game.network.agentes.size(); i++) {
					if (Game.network.agentes.get(i).getX()/Game.TS == tileX && tileY == Game.network.agentes.get(i).getY()/Game.TS) {
						Game.network.agentes.remove(i);
					}
				}
				if (Mundo.tiles[tileX+tileY*Mundo.WIDTH].solid == false) {
					if (Mundo.tiles[tileX+tileY*Mundo.WIDTH] instanceof WallTile) {
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new FloorTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Fundo);
					}
				}
			}else if (mouseAction == "use") {
				if (Mundo.tiles[tileX+tileY*Mundo.WIDTH] instanceof FloorTile) {
					if (items[select] == 1) {
						Mundo.tiles[tileX+tileY*Mundo.WIDTH] = new WallTile(tileX*Game.TS, tileY*Game.TS, Tile.TILE_Parede);
					}
					else {
						this.mousePressed = false;
						boolean pode = true;
						for (int i = 0; i < Game.network.agentes.size(); i++) {
							if (Game.network.agentes.get(i).getX() == tileX && tileY == Game.network.agentes.get(i).getY()) {
								pode = false;
							}
						}
						if (pode) {
							if(items[select] == 2) {
								Game.network.agentes.add(new Aspirador(tileX*Game.TS, tileY*Game.TS,Game.TS,Game.TS,2,null));
							}else if (items[select] == 3) {
								Game.network.agentes.add(new Molhador(tileX*Game.TS, tileY*Game.TS,Game.TS,Game.TS,2,null));
							}else if (items[select] == 4){
								Game.network.agentes.add(new Secador(tileX*Game.TS, tileY*Game.TS,Game.TS,Game.TS,2,null));
							}
						}
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
			if (items [i] == 1) {
				g.drawImage(Tile.TILE_Parede, hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2, null);
			}else if (items[i] == 2) {
				g.setColor(Color.red);
				g.fillRect(hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2);
			}else if (items[i] == 3) {
				g.setColor(Color.blue);
				g.fillRect(hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2);
			}else if (items[i] == 4) {
				g.setColor(Color.orange);
				g.fillRect(hotBarIniPos+tamanhoHotBarSlots*i + tamanhoHotBarSlots/4, Game.HEIGHT*Game.SCALE + tamanhoHotBarSlots/4 - tamanhoHotBarSlots, tamanhoHotBarSlots/2, tamanhoHotBarSlots/2);
			}
			g.setColor(Color.black);
			g.drawRect(hotBarIniPos+tamanhoHotBarSlots*i, Game.HEIGHT*Game.SCALE-tamanhoHotBarSlots, tamanhoHotBarSlots, tamanhoHotBarSlots);
		}
	}

}
