package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class DisparoTanque extends Entity{
    // Fazer a colisão do disparo com os inimigos usando uma interface

    public DisparoTanque(float r, float px, float py) {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        speed = 400;
        maxLife = 500;
        position.x = px;
        position.y = py;
        rotation = r;
        addSprites(1, 12, 6);
    }

    @Override
    public void tick() {
        for (Entity e : Game.entities){
            if (e.position.overlaps(position) && e.state != e.morte && !(e instanceof TankBoss) && !e.invulneravel && !(e instanceof Heals)){
                Game.disparostanque.removeValue(this, true);
                e.life-=((maxLife-life)/5);
                return;
            }
        }
        if (position.overlaps(Game.getPlayer().position)){
            Game.disparostanque.removeValue(this, true);
            Game.getPlayer().life-=((maxLife-life)/20);
            return;
        }
        life++;
        if (life>=maxLife){
            Game.disparostanque.removeValue(this, true);
        }
        position.x += (speed* MathUtils.cos(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
        position.y += (speed* MathUtils.sin(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
    }
}
