package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class SuperDisparo extends Entity{
    // Fazer a colisão do disparo com os inimigos usando uma interface
    private int angulo_disparo;
    private long ultimo_disparo = 0;

    public SuperDisparo(float r, float px, float py) {
        super(32, 32, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        speed = 400;
        maxLife = 500;
        position.x = px;
        position.y = py;
        rotation = r;
        addSprites(1, 39, 7); // sprite de bala gigante
    }

    @Override
    public void tick() {
        if (speed > 0){
            speed-=2;
        }
        if (ultimo_disparo < System.currentTimeMillis()) {
            ultimo_disparo = System.currentTimeMillis() + 50;
            DisparoTanque d = new DisparoTanque(rotation + angulo_disparo, position.x, position.y);
            d.speed = 200;
            Game.disparostanque.add(d);
            angulo_disparo += 45;
            if (angulo_disparo > 180) {
                angulo_disparo -= 360;
            }
        }
        for (Entity e : Game.entities){
            if (e.position.overlaps(position) && e.state != e.morte && !(e instanceof TankBoss) && !e.invulneravel){
                Game.superdisparostanque.removeValue(this, true);
                Game.entities.removeValue(e, true);
                explodir();
                return;
            }
        }
        if (position.overlaps(Game.getPlayer().position)){
            sumonarBicho();
            Game.superdisparostanque.removeValue(this, true);
            Game.getPlayer().life-=((maxLife-life)/20);
            return;
        }
        life++;
        if (life>=maxLife){
            explodir();
        }

        // Fazer a velocidade diminuir com o tempo
        position.x += (speed* MathUtils.cos(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
        position.y += (speed* MathUtils.sin(rotation*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
    }

    public void sumonarBicho(){
        Insect inseto = new Insect();
        inseto.position.x = position.x + tamanhoX/2;
        inseto.position.y = position.y + tamanhoY/2;
        Game.entities.add(inseto);
    }

    public void explodir(){
        sumonarBicho();
        Game.superdisparostanque.removeValue(this, true);
    }
}
