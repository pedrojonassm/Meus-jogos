package com.pedrojonassm.game.Entities;

import com.pedrojonassm.game.control.Game;

public class AlienBoss extends Entity {
    public AlienBoss() {
        super(120, 90, 240, 180);
        life = maxLife = 1;
        position.x = 128;
        position.y = 128;
        maxFr = 4;
        addSprites2("Alien\\IDLE (", ").png", 82, 1, 0, 0, 240, 180);
        //textura = 1;
    }

    @Override
    public void tick() {
        if (life <= 0){
            Game.entities.removeValue(this, true);
            return;
        }
        lookAt(Game.getPlayer());
        rotation -= 90;
        super.tick();
    }
}
