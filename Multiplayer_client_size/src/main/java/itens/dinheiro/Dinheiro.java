package itens.dinheiro;

import itens.Item;
import main.Client;

public class Dinheiro extends Item{

	public Dinheiro(int idbd, int tipo, int total, int maxTotal) {
		super(idbd, tipo, total, maxTotal);
	}
	
	
	
	@Override
	public int adicionar_quantidade(int quant) {
		int k = super.adicionar_quantidade(quant);
		setar_sprite();
		return k;
	}
	// fazer a transformação de um dinheiro em outro e enviar para o Banco de dados
	// lembrar de quando uma bag estiver cheia o item aparecer no inventário ou em outra bag
	@Override
	protected void use() {
		if (quantidade == 100 && item < 5) {
			Item novo = Item.criar_item(item_idBD, item+1, 1, 0);
			novo.setBag(this.getBag());
			getBag().remover_item(this);
			novo.getBag().addItem(novo, true);
			return;
		}else if (item > 1) {
			Item novo = Item.criar_item(-1, item-1, 100, 0);
			novo.setBag(this.getBag());
			if (quantidade == 1) {
				novo.setItemIdBD(getItemIdBD());
				getBag().remover_item(this);
			}
			if (!novo.getBag().addItem(novo, true)) {
				Client.instance.player.bag_estava_lotada(novo);
			}
			
			setQuantidade(quantidade-1);
		}
		super.use();
	}
	
	@Override
	public void setQuantidade(int quantidade) {
		super.setQuantidade(quantidade);
		if (sprites != null) {
			setar_sprite();
		}
	}

	protected void setar_sprite() {
		if (quantidade > 0)
		if (quantidade < sprites.length-2) {
			index = quantidade-1;
		}else if (quantidade < 30) {
			index = sprites.length-2;
		}else {
			index = sprites.length-1;
		}
	}
}
