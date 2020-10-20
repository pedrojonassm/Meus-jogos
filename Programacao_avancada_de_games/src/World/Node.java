package World;

public class Node {
	public Vector2i tile;
	public Node parent;
	public double fCost, gCost, hCost;
	
	public Node(Vector2i tile, Node parent, double gCoste, double hCost) {
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.fCost = gCost+hCost;
	}
}
