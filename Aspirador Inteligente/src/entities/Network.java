package entities;

import java.awt.Graphics;
import java.util.ArrayList;

import world.Tile;

public class Network {
	public static ArrayList<Tile> AsConhecidos, AsExplorados, MoConhecidos, MoExplorados, SeConhecidos,SeExplorados;
	public ArrayList<Entity> agentes;
	public Network() {
		AsConhecidos = new ArrayList<Tile>();
		MoConhecidos = new ArrayList<Tile>();
		SeConhecidos = new ArrayList<Tile>();
		AsExplorados = new ArrayList<Tile>();
		MoExplorados = new ArrayList<Tile>();
		SeExplorados = new ArrayList<Tile>();
		agentes = new ArrayList<Entity>();
	}
	public void reset() {
		int a;
		a = AsConhecidos.size();
		for (int i = 0; i < a; i++) {
			AsConhecidos.get(0).estado = 0;
			AsConhecidos.remove(0);
		}
		a = AsExplorados.size();
		for (int i = 0; i < a; i++) {
			AsExplorados.get(0).estado = 0;
			AsExplorados.remove(0);
		}
		a = MoConhecidos.size();
		for (int i = 0; i < a; i++) {
			MoConhecidos.get(0).estado = 0;
			MoConhecidos.remove(0);
		}
		a = MoExplorados.size();
		for (int i = 0; i < a; i++) {
			MoExplorados.get(0).estado = 0;
			MoExplorados.remove(0);
		}
		a = SeConhecidos.size();
		for (int i = 0; i < a; i++) {
			SeConhecidos.get(0).estado = 0;
			SeConhecidos.remove(0);
		}
		a = SeExplorados.size();
		for (int i = 0; i < a; i++) {
			SeExplorados.get(0).estado = 0;
			SeExplorados.remove(0);
		}
		
		for (int i = 0; i < agentes.size(); i++) {
			agentes.get(i).reset();
		}
	}
	
	public void tick() {
		for (int i = 0; i < agentes.size(); i++) {
			agentes.get(i).tick();
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < agentes.size(); i++) {
			agentes.get(i).render(g);
		}
	}

	public void parar() {
		for (int i = 0; i < agentes.size(); i++) {
			agentes.get(i).explorar = !agentes.get(i).explorar;
		}
	}
	
	public boolean verifica(Tile a) {
		for (int i = 0; i < AsExplorados.size(); i++) {
			if (AsExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < AsConhecidos.size(); i++) {
			if (AsConhecidos.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < MoExplorados.size(); i++) {
			if (MoExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < MoConhecidos.size(); i++) {
			if (MoConhecidos.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < SeExplorados.size(); i++) {
			if (SeExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < SeConhecidos.size(); i++) {
			if (SeConhecidos.get(i) == a) {
				return false;
			}
		}
		return true;
	}
	public boolean verifica2 (Tile a) {
		for (int i = 0; i < MoExplorados.size(); i++) {
			if (MoExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < MoConhecidos.size(); i++) {
			if (MoConhecidos.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < SeExplorados.size(); i++) {
			if (SeExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < SeConhecidos.size(); i++) {
			if (SeConhecidos.get(i) == a) {
				return false;
			}
		}
		return true;
	}
	public boolean verifica3 (Tile a) {
		for (int i = 0; i < SeExplorados.size(); i++) {
			if (SeExplorados.get(i) == a) {
				return false;
			}
		}
		for (int i = 0; i < SeConhecidos.size(); i++) {
			if (SeConhecidos.get(i) == a) {
				return false;
			}
		}
		return true;
	}
}
