package com.pedrojonassm.game.Entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.pedrojonassm.game.control.Game;
import com.pedrojonassm.game.control.Spawner;

public class TankBoss extends Entity {
    private int gunX, gunY, px2 = 40, acao, giros = 0, super_disparos = 0, inicio_maluquice;
    private float alvoX, alvoY;
    private int rotateBase = 0, rotateGun = 0;
    private long ultimo_Tiro = 0, ultimo_movimento = 0;
    public TankBoss() {
        super(126, 105, 252, 209);
        life = maxLife = 3000;
        valor_em_pontos = 6000;
        gunY = 50;
        gunX = 50;
        position.x = 400;
        position.y = 400;
        acao = 1;
        addSprites2("Tank\\Body_", ".png", 2, 1, 0, 0, tamanhoX, tamanhoY);
        spawnar();
    }

    @Override
    public void tick() {
        lookAt(Game.getPlayer());
        rotation = (int) rotation;
        if  (acao == 0 && System.currentTimeMillis() > ultimo_movimento){
            int sorteado = Spawner.rand.nextInt(100);
            if (sorteado < 40){ // 40

                acao = 1;
                // movendo de lugar
                do{
                    alvoX = Spawner.rand.nextInt(Game.background.getWidth());
                    alvoY = Spawner.rand.nextInt(Game.background.getHeight());
                }while(((alvoX > tamanhoX && alvoX < Game.background.getWidth() - tamanhoX) || (alvoY > tamanhoY && alvoY < Game.background.getHeight() - tamanhoY)) && distancia(alvoX, alvoY, position.x, position.y) > 300);
                //
            }
            else if (sorteado < 70){ // 70
                // disparos giratórios
                acao = 2;
            }else{
                // SUper disparo que spawna insetos
                acao = 3;
            }
        }

        switch (acao){
            case 1:
                locomover();
                break;
            case 2:
                disparo_maluco();
                break;
            case 3:
                super_disparo();
        }

        if (!(rotateGun - rotation <= 1 && rotateGun - rotation >= -1) && acao != 2) {
            int k = 0, soma = 0;
            do {
                k++;
                if (rotateGun+k == rotation || (rotateGun+k-360 - rotation <= 2 && rotateGun+k-360 - rotation >= -2) ){
                    soma = 1;
                }else if (rotateGun-k == rotation|| (rotateGun-k-360 - rotation <= 2 && rotateGun-k-360 - rotation >= -2)){
                    soma = -1;
                }
            }while(soma == 0);
            rotateGun+=soma;
        }else{
            atirar_normal(false);
        }
        if (life <= 0){
            Game.entities.removeValue(this, true);
            Game.bossMorto();
            Troll troll = new Troll();
            troll.position.x = position.x + tamanhoX/2;
            troll.position.y = position.y + tamanhoY/2;
            Game.entities.add(troll);
        }
    }

    private void locomover() {
        int tolook = (int) (MathUtils.atan2((alvoY)-(position.y+pivoY), (alvoX)-(position.x+pivoX))*MathUtils.radiansToDegrees);
        //System.out.println("AX:" + alvoX + " AY: "+alvoY+" posX: "+position.x + " PosY: "+position.y+ " TL: "+tolook+" RB: "+rotateBase);
        if (rotateBase != tolook){
            int k = 0, soma = 0;
            do {
                k++;
                if (rotateBase+k == tolook || rotateBase+k-360 == tolook){
                    soma = 1;
                }else if (rotateBase-k == tolook|| rotateBase-k+360 == tolook){
                    soma = -1;
                }
            }while(soma == 0);
            rotateBase+=soma;
            while (rotateBase < -180){
                rotateBase += 360;
            }
            while (rotateBase >= 180){
                rotateBase -= 360;
            }
            return;
        }else{
            position.x += (200* MathUtils.cos(tolook*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
            position.y += (200* MathUtils.sin(tolook*MathUtils.degreesToRadians)) * Gdx.graphics.getDeltaTime();
        }
        if (distancia(alvoX, alvoY, position.x, position.y) <= 200){
            alvoX = position.x;
            alvoY = position.y;
            acao = 0;
            ultimo_movimento = System.currentTimeMillis() + 2000;
        }
    }

    private void disparo_maluco() {
        if (giros == 0) {
            inicio_maluquice = rotateGun;
            rotateGun++;
            giros = (Spawner.rand.nextInt(maxLife/life)+1)*5;
            if (giros  > 100){
                giros = 100;
            }
        }else{
            if ((rotateGun-inicio_maluquice)%360 <= 30){
                giros--;
            }
            for (int i = 0; i < 30; i+=10){
                rotateGun+=10;
                atirar_normal(true);
            }
            if (giros == 0){
                acao = 0;
                rotateGun = inicio_maluquice;
                ultimo_movimento = System.currentTimeMillis() + 5000;
            }
        }
    }

    private void super_disparo() {
        if (super_disparos == 0) {
            super_disparos = (Spawner.rand.nextInt(maxLife/life)+1);
            if (giros  > 10){
                giros = 10;
            }
        }else{
            if (ultimo_Tiro < System.currentTimeMillis() && Spawner.rand.nextInt(100) <= 20){
                super_disparos--;
                if (super_disparos == 0){
                    acao = 0;
                    ultimo_Tiro = System.currentTimeMillis() + 5000;
                    ultimo_movimento = System.currentTimeMillis() + 8000;
                }else{
                    ultimo_Tiro = System.currentTimeMillis() + 10000;
                }
                Game.superdisparostanque.add(new SuperDisparo(rotateGun, pegarTiroX(), pegarTiroY()));
            }
        }
    }

    public float pegarTiroX(){
        return position.x + tamanhoX / 2 - ((MathUtils.cosDeg(rotateBase) + 1) * 40) + sprites.get(0)[0].getRegionWidth() * MathUtils.cos(rotateGun * MathUtils.degreesToRadians);
    }
    public float pegarTiroY(){
        return position.y + tamanhoY / 2 - gunY / 2 - MathUtils.sinDeg(rotateBase) * 51 + sprites.get(0)[0].getRegionHeight() * MathUtils.sin(rotateGun * MathUtils.degreesToRadians);
    }

    public void atirar_normal(boolean forcar){
        if (System.currentTimeMillis() - 200 > ultimo_Tiro || forcar) {
            ultimo_Tiro = System.currentTimeMillis();
            Game.disparostanque.add(new DisparoTanque(rotateGun, pegarTiroX(), pegarTiroY()));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (toRender()) {
            if (state < sprites.size){
                batch.draw(sprites.get(0)[0], position.x, position.y, pivoX, pivoY, tamanhoX, tamanhoY, 1, 1, rotateBase);
                batch.draw(sprites.get(0)[1], position.x + tamanhoX/2 - ((MathUtils.cosDeg(rotateBase)+1)*40), position.y - gunY-MathUtils.sinDeg(rotateBase)*51, pivoX - gunX-px2, pivoY + gunY, tamanhoX, tamanhoY, 1, 1, rotateGun);
            }
        }
    }
}
