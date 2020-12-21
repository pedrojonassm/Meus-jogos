package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;
import com.pedrojonassm.game.control.Spawner;

public class Troll extends Entity{
    /*
     // normal
     0 --> andando
     1 --> atacando
     // machucado
     2 --> andando
     3 --> machucado

     4 --> morrendo
     */
    public Troll() {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        life = maxLife = 400*Spawner.difficult;
        valor_em_pontos = 15;
        speed = 150*Spawner.difficult;
        maxFr = 5;
        addSprites(8,24,4); // 0
        addSprites(8, 32, 4);// 1
        addSprites(8, 0, 5); // 2
        addSprites(8, 8, 5); // 3
        addSprites(31,16, 5); // 4
        morte = 4;
        state = ferido * 2;
        spawnar();
    }

    public void tick(){
        if (life > 0) {
            lookAt(Game.getPlayer());
            if (life < maxLife / 4) {
                ferido = 1;
            }
            if (distancia(position.x, position.y, Game.getPlayer().position.x, Game.getPlayer().position.y) > Game.sprites.getTamanho()) {
                position.x += MathUtils.cosDeg(rotation)*speed * Gdx.graphics.getDeltaTime();
                position.y += MathUtils.sinDeg(rotation)*speed * Gdx.graphics.getDeltaTime();
            } else {
                if (!atacar) {
                    atacar = true;
                    index = 0;
                    state = 1 + (ferido * 2);
                } else if (state != 1 + (ferido * 2)) {
                    state = 1 + (ferido * 2);
                }
            }
            rotation -= 90;
        }else{
            state = 4;
            atacar = false;
        }
        super.tick();
    }

    @Override
    public void endAnimation() {
        if (atacar){
            if (distancia(position.x, position.y, Game.getPlayer().position.x, Game.getPlayer().position.y) > Game.sprites.getTamanho()){
                // atacou o jogador, mas ele desviou
                state = ferido*2;
                atacar = false;
            }else{
                Game.getPlayer().life-= 10;
            }
        }else if (state == 4){
            index = maxIndex.get(state)-1;
            Game.entities.removeValue(this, true);
        }
    }
}
