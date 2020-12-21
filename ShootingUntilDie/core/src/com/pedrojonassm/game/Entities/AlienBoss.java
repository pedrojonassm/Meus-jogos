package com.pedrojonassm.game.Entities;

import com.pedrojonassm.game.control.Game;
import com.pedrojonassm.game.control.Spawner;

public class AlienBoss extends Entity {

    private boolean jump = false;
    private int pulos, tempoSalto = -1;
    private double movex = 0, movey = 0;
    private long ultimoSalto;

    public AlienBoss() {
        super(120, 90, 240, 180);
        valor_em_pontos = 2000;
        life = maxLife = 1500*Spawner.difficult;
        position.x = 256;
        position.y = 256;
        maxFr = 4;
        addSprites2("Alien\\IDLE (", ").png", 82, 1, 0, 0, 240, 180);
        spawnar();
        // Ao ter 2 contra-barras em uma string, a primeira é anulada
    }

    public void sumonarBicho(){
        Scorpion escorpiao = new Scorpion();
        escorpiao.position.x = position.x + tamanhoX/2;
        escorpiao.position.y = position.y + tamanhoY/2;
        Game.entities.add(escorpiao);
    }

    @Override
    public void tick() {
        if (life <= 0){
            Game.entities.removeValue(this, true);
            Insect inseto = new Insect();
            inseto.position.x = position.x + tamanhoX/2;
            inseto.position.y = position.y + tamanhoY/2;
            Game.entities.add(inseto);
            Game.bossMorto();
            return;
        }
        if (pulos == 0 && Spawner.rand.nextInt(100) <= 20){
            pulos = Spawner.rand.nextInt(maxLife/life) + 1;
            if (pulos > 8){
                pulos = 8;
            }
        }
        if (pulos > 0 && ultimoSalto+3000 < System.currentTimeMillis() && Spawner.rand.nextInt(100) <= 5){
            tempoSalto = 100;
            jump = true;
            ultimoSalto = System.currentTimeMillis();
            movex = (Game.getPlayer().position.x - Game.getPlayer().tamanhoX/2 - position.x)/tempoSalto;
            movey = (Game.getPlayer().position.y - Game.getPlayer().tamanhoY/2 - position.y)/tempoSalto;
        }
        if (tempoSalto == 0){
            tempoSalto--;
            jump = false;
            if (position.contains(Game.getPlayer().position)){
                pulos = 0;
                Game.getPlayer().life -= 30;
                Game.getPlayer().jogar_para_longe(position.x, position.y);
                ultimoSalto = System.currentTimeMillis()+5000;
            }
        }
        if (jump){
            if (tempoSalto%50 == 0){
                sumonarBicho();
            }
            tempoSalto--;
            if (tempoSalto >= 50){
                escalaX+=0.04;
                escalaY+=0.04;
            }else{
                escalaX-=0.04;
                escalaY-=0.04;
            }
            position.x += movex;
            position.y += movey;
        }
            lookAt(Game.getPlayer());
            rotation -= 90; // no sprite ele está olhando para cima, logo para reparar deve-se subtrair 90 graus

        super.tick();
    }

    public boolean isJumping() {
        return jump;
    }
}
