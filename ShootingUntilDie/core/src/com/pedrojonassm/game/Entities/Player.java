package com.pedrojonassm.game.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;

public class Player extends Entity {
    /*
    O que fazer: Aumentar os Sprites dos bonectos, todos eles
    States:
    // pistola
    0 --> parado com a pistola
    1 --> andando com a pistola
    2 --> recarregando pistola
    3 --> atirando com a pistola
    // Metralhadora (Rifle)
    4 --> parado com o rifle
    5 --> andando com o rifle
    6 --> recarregando o rifle
    7 --> atirando com o rifle
    //espingarda
    8 --> parado com a espingarda
    9 --> andando com a espingarda
    10 --> recarregando a espingarda
    11 --> atirando com a espingarda
    state = (0-3) + gun*4
     */
    public int gun = 0, totalGuns = 3, angulo = 0, tempoJogado = 0;
    private int[] ammo, maxAmmo, totalAmmo;
    private double lastDisparo = 0;
    public boolean moving = false, reload = false, atordoado = false;
    public Player(int pX, int pY) {
        super(pX, pY, Game.sprites.getTamanho(), Game.sprites.getTamanho());
        life = maxLife = 100;
        maxFr = 3;
        ammo = new int[3];
        ammo[0] = 10;
        ammo[1] = 30;
        ammo[2] = 8;
        maxAmmo = new int[3];
        maxAmmo[0] = 10;
        maxAmmo[1] = 30;
        maxAmmo[2] = 8;
        totalAmmo = new int[3];
        totalAmmo[0] = 10;
        totalAmmo[1] = 30;
        totalAmmo[2] = 8;
        position.x = 200;
        position.y = 200;
        adicionarsprites();
        state = 0;
    }

    public void coletarMunicao(int tipo, int total){
        totalAmmo[tipo] += total;
    }

    private void adicionarsprites() {
        addSprites(20, 0, 0); // 0
        addSprites(20, 20, 0); // 1
        addSprites(15, 0, 1); // 2
        addSprites(3, 15, 1); // 3
        addSprites(20, 18, 1);// 4
        addSprites(20, 38, 1); // 5
        addSprites(20, 18, 2); // 6
        addSprites(3, 38, 2); // 7
        addSprites(20, 1, 3); // 8
        addSprites(20, 21, 3); // 9
        addSprites(20, 1, 4); // 10
        addSprites(3, 21, 4); // 11
    }

    public void tick(){
        if ((Game.getUi().aX != 0 || Game.getUi().aY != 0) && !atordoado){
            moving = true;
        }else{
            moving = false;
        }
        float moveX = 0, moveY = 0;
        if (moving) {
            moveX = (200 * Game.getUi().aX / (Game.getUi().maior.radius / 2)) * Gdx.graphics.getDeltaTime();
            moveY = (200 * Game.getUi().aY / (Game.getUi().maior.radius / 2)) * Gdx.graphics.getDeltaTime();
        }else if (atordoado){
            tempoJogado--;
            if (tempoJogado <= 0){
                atordoado = false;
            }
            moveX = MathUtils.cos(angulo) * Gdx.graphics.getDeltaTime() * 600;
            moveY = MathUtils.sin(angulo) * Gdx.graphics.getDeltaTime() * 600;
        }
        if (position.x + moveX > 0 && position.x + moveX + position.width < Game.background.getWidth()) {
            position.x += moveX;
        }
        if (position.y + moveY > 0 && position.y + moveY + position.height < Game.background.getHeight()) {
            position.y += moveY;
        }
        if (moving && !reload){
            state = 1+(gun*4);
        }else if (!reload) {
            state = gun*4;
        }else{
            state = 2 + gun*4; // recarregando
        }
        super.tick();
    }
    public void rotate(float lookX, float lookY){
        rotation = MathUtils.atan2(lookY - (position.y-(Game.camera.position.y- Game.camera.viewportHeight/2)), lookX - (position.x-(Game.camera.position.x- Game.camera.viewportWidth/2)))*MathUtils.radiansToDegrees;
    }

    public boolean colidindo(){
        for (Entity e : Game.entities){
            if (e.position.overlaps(position)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void endAnimation() {
        if (state > 0 && state % (2+(gun*4)) == 0){
            // se estiver recarregando
            int aux;
            if (ammo[gun] + totalAmmo[gun] >= maxAmmo[gun]){
                aux = maxAmmo[gun] - ammo[gun];
            }else{
                aux = totalAmmo[gun];
            }
            ammo[gun] += aux;
            totalAmmo[gun] -= aux;
            reload = false;
        }
    }

    public void jogar_para_longe(float AlienX, float AlienY){
        // quando o Alien acertar o salto no player, o jogador será jogado para longe.
        angulo = (int) (MathUtils.atan2(AlienY-position.y, AlienX-position.x)*MathUtils.radiansToDegrees);
        while (angulo< 0){
            angulo += 360;
        }
        while (angulo >= 360){
            angulo-=360;
        }
        atordoado = true;
        tempoJogado = 50;
    }

    public void atirar(){
        if (!atordoado){
            if (lastDisparo - System.currentTimeMillis() <= -20 && !Game.pause && ammo[gun] > 0){
                ammo[gun]--;
                lastDisparo = System.currentTimeMillis();
                if (gun != 2) { // 2 é a espingarda
                    Game.disparos.add(new Disparo(rotation, gun));
                }else{
                    for (int i = -15; i <= 15; i+=6){
                        Game.disparos.add(new Disparo(rotation+i, gun));
                    }
                }
            }else if (ammo[gun] == 0 && !reload){
                reload = true;
            }
        }
    }

    public int getAmmo() {
        return ammo[gun];
    }

    public int getTotalAmmo() {
        return totalAmmo[gun];
    }
}
