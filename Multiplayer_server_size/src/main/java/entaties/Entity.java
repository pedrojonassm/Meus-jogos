package entaties;

public class Entity {
	
	int pos, x, y, z, life, state, index, skin;
	String name;
	
	public Entity(int pos, String name) {
		this.pos = pos;
		this.name = name;
	}
	
	public void atualizar(int xin, int yin, int zin, int lifeIn, int stateIn, int indexIn, int skinIn) {
		x = xin;
		y = yin;
		z = zin;
		state = stateIn;
		index = indexIn;
		skin = skinIn;
		
	}
	
	public static double distancia(int x1, int x2, int y1, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
}
