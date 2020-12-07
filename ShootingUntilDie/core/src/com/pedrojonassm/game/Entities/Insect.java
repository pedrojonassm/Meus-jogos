package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class Insect extends Entity {
    /*
    0 -- andando
    1 -- atacando
    2 -- morrendo
     */
    public Insect() {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        life = maxLife = 300;
        addSprites(8, 13, 7);// andando
        addSprites(8, 21, 7); // atacando
        addSprites(8, 29, 7); // morrendo
        morte = 2;
        speed = 100;
        maxFr = 3;
        state = ferido * 2;
        spawnar();
    }

    @Override
    public void tick(){
        if (life > 0) {
            lookAt(Game.getPlayer());
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
            state = 2;
            atacar = false;
        }
        super.tick();
    }

    @Override
    public void endAnimation() {
        if (atacar){
            if (distancia(position.x, position.y, Game.getPlayer().position.x, Game.getPlayer().position.y) > Game.sprites.getTamanho()){
                state = ferido*2;
                atacar = false;
            }else{
                Game.getPlayer().life-= 5;
            }
        }else if (state == 2){
            index = maxIndex.get(state)-1;
            Game.entities.removeValue(this, true);
        }
    }
}
