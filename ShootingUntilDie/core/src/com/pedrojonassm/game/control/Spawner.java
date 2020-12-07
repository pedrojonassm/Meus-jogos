package com.pedrojonassm.game.control;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pedrojonassm.game.Entities.Heals;
import com.pedrojonassm.game.Entities.Insect;
import com.pedrojonassm.game.Entities.Scorpion;
import com.pedrojonassm.game.Entities.Troll;

import java.util.Random;

public class Spawner {
    // Essa é a classe que irá spawnar os monstros
    private int difficult = 2;
    public static Random rand;
    private long lastspawn, lastheal;
    public Spawner(){
        rand = new Random();
        lastspawn = System.currentTimeMillis();
        lastheal = System.currentTimeMillis();
    }
    public void spawnarMonstro(){
        // scorpion, insect, troll
        int k = rand.nextInt(difficult+1);
        if (k == 0){
            Game.entities.add(new Scorpion());
        }else if (k == 1){
            Game.entities.add(new Insect());
        }else{
            Game.entities.add(new Troll());
        }
    }
    public void levelUp(){
        difficult++;
    }
    public void tick(){
        if (System.currentTimeMillis()-lastheal >= 3000){
            lastheal = System.currentTimeMillis();
            Game.entities.add(new Heals());
        }
        if (System.currentTimeMillis()-lastspawn >= 6000){
            lastspawn = System.currentTimeMillis();
            spawnarMonstro();
        }
    }
}