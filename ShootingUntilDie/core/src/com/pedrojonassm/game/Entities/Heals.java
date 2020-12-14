package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pedrojonassm.game.control.Game;
import com.pedrojonassm.game.control.Spawner;

public class Heals extends Entity {
    private long spawn;
    public Heals() {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        spawn = System.currentTimeMillis();
        TextureRegion[] curas = new TextureRegion[3], ammo = new TextureRegion[3];
        // pílula
        curas[0] = Game.sprites.getAsset2(38*64, 7*64+37, 11, 27);
        // pilulas
        curas[1] = Game.sprites.getAsset2(38*64+53, 7*64, 11, 24);
        // bandagem
        curas[2] = Game.sprites.getAsset2(38*64, 7*64, 53, 37);

        // munição de Pistola
        ammo[0] = Game.sprites.getAsset2(38*64+11, 7*64+37, 8, 24);
        // munição de metralhadora
        ammo[1] = Game.sprites.getAsset2(38*64+19, 7*64+37, 25, 28);
        // munição de shotgun
        ammo[2] = Game.sprites.getAsset2(38*64+44, 7*64+37, 9, 25);

        sprites.add(curas);
        sprites.add(ammo);
        index = Spawner.rand.nextInt(3);
        state = 1;
        if (Game.getPlayer().life < Game.getPlayer().maxLife){
            // Caso a vida do player não esteja cheia, pode spawnar + vida
            state = Spawner.rand.nextInt(2);
        }
        if (state == 0){
            life = (index+1)*30;
            if (index == 0){
                pivoX = 26;
                pivoY = 15;
                tamanhoX = 53;
                tamanhoY = 37;
            }else if (index == 1){
                pivoX = 6;
                pivoY = 12;
                tamanhoX = 11;
                tamanhoY = 24;
            }else{
                pivoX = 6;
                pivoY = 13;
                tamanhoX = 11;
                tamanhoY = 27;
            }
        }else{
            life = 24/(index+1);
            if (index == 0){
                pivoX = 4;
                pivoY = 12;
                tamanhoX = 8;
                tamanhoY = 24;
            }else if (index == 1){
                pivoX = 12;
                pivoY = 14;
                tamanhoX = 25;
                tamanhoY = 28;
            }else{
                pivoX = 4;
                pivoY = 13;
                tamanhoX = 9;
                tamanhoY = 25;
            }
        }
        spawnar();
    }

    @Override
    public void tick() {
        if (distancia(position.x, position.y, Game.getPlayer().position.x, Game.getPlayer().position.y) < Game.sprites.getTamanho()) {
            if (state == 0){
                // cura
                if (Game.getPlayer().life + life > Game.getPlayer().maxLife) {
                    Game.getPlayer().life = Game.getPlayer().maxLife;
                } else {
                    Game.getPlayer().life += life;
                }
            }else{
                // munição
                Game.getPlayer().coletarMunicao(index, life);
            }
            Game.entities.removeValue(this, true);
        }else if (System.currentTimeMillis()-spawn >= 20000){ // depois de 20 segundos, ele some
            Game.entities.removeValue(this, true);
        }
    }
}
