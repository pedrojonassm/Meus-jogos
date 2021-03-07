package itens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Inventario.Bag;
import Inventario.simpleBag;
import graficos.SpriteSheet;
import interfaces.PossuiSprites;
import itens.dinheiro.Coin;
import itens.dinheiro.DollarNote;
import itens.dinheiro.HundredDollarNote;
import itens.dinheiro.MillionDollarNote;
import itens.dinheiro.TenThousandDollarNote;
import main.Client;

public class Item implements PossuiSprites{
	protected int quantidade, quantidade_maxima, index, item_idBD, item_origem; // item_origem é o id no banco de dados do item que o gerou, ele é usado para poder colocar parte de um item em uma mesma bag
	public int item; // salva qual item é
	protected BufferedImage[] sprites;
	private Bag bag;
	private String frase;
	protected String item_name;
	public int x, y, z; // caso esteja no chão
	
	public Item(int idBD, int tipo, int total, int maxTotal) {
		item_origem = -1;
		this.item_idBD = idBD; // id do item no banco de dados do servidor, caso seja -1 representa que não está no banco de dados
		// se tiver mais que o maximo, o que vai acontecer?
		frase = "";
		item_name = "[Alguma coisa devia estar escrita aqui]";
		bag = null;
		setQuantidade(total);
		quantidade_maxima = maxTotal;
		item = tipo;
		index = 0;
	}
	
	public void setItem_origem(int item_origem) {
		this.item_origem = item_origem;
	}
	public int getItem_origem() {
		return item_origem;
	}
	
	public void setItemIdBD(int idBD) {
		this.item_idBD = idBD;
	}
	public int getItemIdBD() {
		return item_idBD;
	}
	
	public String getFrase() {
		return frase;
	}
	
	public void setFrase(String frase) {
		this.frase = frase;
	}
	
	public Bag getBag() {
		return bag;
	}
	
	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public void mouse_click(int button) { // clique com o do mouse (1 = left, 2 = middle, 3 = right
		if (button == 1) {
			Client.instance.setar_item_segurado(this);
			return;
		}else if (button == 3) {
			if (Client.instance.item_segurando != null && Client.instance.item_segurando.equals(this)) {
				// look
				look();
			}else {
				use();
			}
		}
	}

	protected void use() {
		// usando o item
		
	}

	protected void look() {
		Client.instance.ui.olhar(frase);
	}

	public int adicionar_quantidade(int quant) {
		if (quantidade + quant <= quantidade_maxima) {
			setQuantidade(quantidade+quant);
			return 0;
		}else {
			int sobra = quantidade + quant - quantidade_maxima;
			setQuantidade(quantidade_maxima);
			return sobra;
		}
	}
	
	public void render(int x, int y, Graphics g) {
		//g.setColor(new Color(i*Client.TI));
		//g.fillRect(x, y, Client.TI, Client.TI);
		if (sprites != null) g.drawImage(sprites[index], x, y, Client.TI, Client.TI, null);
		
		if (quantidade > 1) {
			int w1 = g.getFontMetrics().stringWidth(""+quantidade);
			g.setColor(Color.white);
			g.drawString(""+quantidade, x+Client.TI-w1, y+Client.TI);
		}
	}
	
	
	public int getQuantidade() {
		return quantidade;
	}
	public int getQuantidade_maxima() {
		return quantidade_maxima;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
		frase = "You see ";
		if (quantidade == 1) {
			frase += "a " + item_name;
		}else {
			frase += ""+quantidade+" "+item_name+"s";
		}
	}

	public void addSprites (int frames, int xI, int skin, SpriteSheet spriteSheet){
		sprites = new BufferedImage[frames];
		int yI = 0;
		// Percorre o SpriteSheet pegando frames sprites a partir da posição x e y
		xI += skin*spriteSheet.getSpritesPorSkin();
        for (int i = 0; i < frames; i++){
        	if (xI > spriteSheet.getQuadrados()-1){
                yI+= (int) (xI/spriteSheet.getQuadrados());
                xI = xI%spriteSheet.getQuadrados();
            }
            sprites[i] = spriteSheet.getAsset(xI, yI);
            xI++;
        }
    }

	public static Item criar_item(int id_item_bd, int tipo_item, int quantidade_item, int id_bag) {
		if (quantidade_item > 0) {
			switch (tipo_item) {
			// ver de acordo com a lista de itens
			case 1:
				return new Coin(id_item_bd, quantidade_item);
			case 2:
				return new DollarNote(id_item_bd, quantidade_item);
			case 3:
				return new HundredDollarNote(id_item_bd, quantidade_item);
			case 4:
				return new TenThousandDollarNote(id_item_bd, quantidade_item);
			case 5:
				return new MillionDollarNote(id_item_bd, quantidade_item);
			}
		}
		return Bag.criar_bag(id_bag, id_item_bd, tipo_item);
		// System.out.println("id bd: "+id_item_bd+" tipo: "+tipo_item+" quant: "+quantidade_item);
	}

}
