package com.pedrojonassm.game.Entities;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class TankBoss extends Entity {
    private int gunX, gunY, px2 = 40;
    public int rotateBase = 0, rotateGun = 0;
    public TankBoss() {
        super(126, 105, 252, 209);
        life = maxLife = 1;
        gunY = 50;
        gunX = 50;
        position.x = 400;
        position.y = 400;
        addSprites2("Tank\\Body_", ".png", 2, 1, 0, 0, tamanhoX, tamanhoY);
    }

    @Override
    public void tick() {
        lookAt(Game.getPlayer());
        rotation = (int) rotation;
        /*
        rotation:
        -180 até 180
        X está mais perto de -180 ou de 180
         */
        if (rotateGun != rotation) {
            int k = 0, soma = 0;
            do {
                k++;
                if (rotateGun+k == rotation || rotateGun+k-360 == rotation){
                    soma = 1;
                }else if (rotateGun-k == rotation|| rotateGun-k+360 == rotation){
                    soma = -1;
                }
            }while(soma == 0);
            rotateGun+=soma;
        }
        if (life <= 0){
            Game.entities.removeValue(this, true);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        /*
         0 -> (MathUtils.cosDeg(rotateBase)-1)*40
         90 -> (MathUtils.cosDeg(rotateBase)+1)*40
         180 -> (MathUtils.cosDeg(rotateBase)+3)*40
         270 -> (MathUtils.cosDeg(rotateBase)+1)*40
         */
        if (toRender()) {
            if (state < sprites.size){
                batch.draw(sprites.get(0)[0], position.x, position.y, pivoX, pivoY, tamanhoX, tamanhoY, 1, 1, rotateBase);
                batch.draw(sprites.get(0)[1], position.x + tamanhoX/2 - ((MathUtils.cosDeg(rotateBase)+1)*40), position.y - gunY-MathUtils.sinDeg(rotateBase)*51, pivoX - gunX-px2, pivoY + gunY, tamanhoX, tamanhoY, 1, 1, rotateGun);
            }
        }
    }
}
