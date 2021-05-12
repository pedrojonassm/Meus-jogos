package world;

import java.util.ArrayList;

public class Casa {
	private ArrayList<Integer> tiles; // guarda a posição dos tiles do mundo
	private int door; // local em frente a porta, ao dar tp ela aparece ali
	private String nome;
	
	public Casa(ArrayList<Integer> tiles, int door, String nome) {
		this.door = door;
		this.tiles = tiles;
		this.nome = nome;
	}
	
	public void add_or_remove_tile(int pos) {
		if (!tiles.remove((Integer) pos)) tiles.add(pos);
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setDoor(int door) {
		this.door = door;
	}
}
