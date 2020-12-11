package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class Disparo extends Entity{
    private int power;
    // Fazer a colisão do disparo com os inimigos usando uma interface

    public Disparo(float r, int s) {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        r-=10;
        power = (s+1);
        maxLife = 300/power;
        position.x = Game.getPlayer().position.x+40*MathUtils.cos(r*MathUtils.degreesToRadians);
        position.y = Game.getPlayer().position.y + 40*MathUtils.sin(r*MathUtils.degreesToRadians);
        rotation = r;
        addSprites(1, 12, 6);
    }

    @Override
    public void tick() {
        for (Entity e : Game.entities){
            if (e.position.overlaps(position) && e.state != e.morte){

                if (!(e instanceof AlienBoss && ((AlienBoss) e).isJumping())){
                    Game.disparos.removeValue(this, true);
                    e.life-=((maxLife-life)/5)*power;
                    return;
                }
            }
        }
        life++;
        if (life>=maxLife){
            Game.disparos.removeValue(this, true);
        }
        position.x += (400* MathUtils.cos(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
        position.y += (400* MathUtils.sin(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
