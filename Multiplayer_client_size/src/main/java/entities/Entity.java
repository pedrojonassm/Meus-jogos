package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graficos.SpriteSheet;
import interfaces.PossuiSprites;
import main.Client;
import world.Camera;

public class Entity implements PossuiSprites{
	protected int x, y, z, state, totalStates, index, skin, total_skins, framesPorSkin, animation_time, max_animation_time, life, maxLife;
	/*
	 * State é qual animação esta acontecendo, por exemplo, se o player está andando para baixo ou para cima
	 * o totalStates representa o tatal de states (no caso do player, são 8). isso serve pra mudar a skin e na hora de pegar os sprites
	 * index é qual dos frames da animação está agora
	 */
	private static ArrayList<BufferedImage[]> sprites;
	protected ArrayList<Integer> maxIndex; // posição no Array; numero máximo de posições em um array
	protected int entityIdBD, exp;
	
	public Entity(int posX, int posY, int posZ) {
		life = 100;
		x = posX; y = posY; z = posZ;
		sprites = new ArrayList<BufferedImage[]>();
		animation_time = state = skin = index = 0;
		max_animation_time = 3;
		maxIndex = new ArrayList<Integer>();
	}
	
	public void setEntityIdBD(int entityIdBD) {
		this.entityIdBD = entityIdBD;
	}
	
	public void tick() {
		// animação
		animation_time++;
		if (animation_time >= max_animation_time) {
			animation_time = 0;
			index++;
			if (index >= maxIndex.get(state)) {
				index = 0;
				endAnimation();
			}
		}
	}
	
	public int getEntityIdBD() {
		return entityIdBD;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getState() {
		return state;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getSkin() {
		return skin;
	}
	public void setSkin(int skin) {
		this.skin = skin;
	}
	protected void adicionar_sprites() {sprites.clear();}
	
	public void atualizar_dados(int x, int y, int z, int life, int state, int index, int skin2) {
		setX(x); setY(y); setZ(z); setLife(life); setState(state); setIndex(index); setSkin(skin2);
	}
	
	public static double distancia(int x1, int x2, int y1, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public void addSprites (int frames, int xI, int skin, SpriteSheet spriteSheet){
		int yI = 0;
		// Percorre o SpriteSheet pegando frames sprites a partir da posição x e y
		xI += skin*spriteSheet.getSpritesPorSkin();
        maxIndex.add(frames);
        BufferedImage[] t = new BufferedImage[frames];
        for (int i = 0; i < frames; i++){
        	if (xI > spriteSheet.getQuadrados()-1){
                yI+= (int) (xI/spriteSheet.getQuadrados());
                xI = xI%spriteSheet.getQuadrados();
            }
            t[i] = spriteSheet.getAsset(xI, yI);
            xI++;
        }
        sprites.add(t);
    }
	
	public static boolean toRender(int x, int y) {
		return distancia(x, Client.instance.player.getX(), y, Client.instance.player.getY()) < 1500;
	}
	
	private void endAnimation() {	} // ao finalizar uma animação, algo deve acontecer?

	public void render(Graphics g) {
		g.setColor(Color.blue);
		//g.fillRect(x - Camera.x, y - Camera.y, Client.TS, Client.TS);
		g.drawImage(sprites.get(state + totalStates*skin)[index], x-Camera.x, y-Camera.y, null);
	}
}
