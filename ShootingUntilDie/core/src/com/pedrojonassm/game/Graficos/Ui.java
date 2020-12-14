package com.pedrojonassm.game.Graficos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.pedrojonassm.game.control.Game;

public class Ui {
    BitmapFont font;
    TextureRegion[] armas, analogico;
    TextureRegion recarregarArma;
    public int aX, aY, posX, posY, recarregar = 0;
    public Circle maior, reload;
    public Rectangle trocar_armas, continuar, novo_jogo, vida;
    public boolean ultimo_jogo = false, recorde = false;
    public Ui(boolean tem_ultimo_jogo){
        ultimo_jogo = tem_ultimo_jogo;
        float px = Game.camera.position.x - Game.camera.viewportWidth/2, py = Game.camera.position.y + Game.camera.viewportHeight/2;
        recarregarArma = Game.sprites.getAsset(37, 7);
        armas = new TextureRegion[3];
        analogico = new TextureRegion[2];
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(1.5f);
        armas[0] = Game.sprites.getAsset(7, 6); // pistola
        armas[1] = Game.sprites.getAsset(8, 6); // metralhadora
        armas[2] = Game.sprites.getAsset(9, 6); // espingarda
        analogico[0] = Game.sprites.getAsset(10, 6); // circulo maior
        analogico[1] = Game.sprites.getAsset(11, 6); // circulo menor

        posX = (int) (Game.getTelaWidth()/6);
        posY = (int) (Game.getTelaHeight()/6);
        trocar_armas = new Rectangle(px+Game.getTelaWidth()-posX, 0, Game.getTelaWidth()/10, Game.getTelaHeight()/3);
        vida = new Rectangle(posX, Game.getTelaHeight()/2, 120*Game.getTelaWidth()/500, 20*Game.getTelaHeight()/500);
        novo_jogo = new Rectangle(posX, Game.getTelaHeight()/2, Game.getTelaWidth()/7, Game.getTelaHeight()/10);
        continuar = new Rectangle(posX, Game.getTelaHeight()/2-Game.getTelaHeight()/8, Game.getTelaWidth()/7, Game.getTelaHeight()/10);
        maior = new Circle(posX, posY, (int) ((posX+posY)/6));
        reload = new Circle(px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth(), py+posY- Game.getTelaHeight(), (int) ((posX+posY)/6));
        // acrescentar botão para troca das armase
    }
    public void render(SpriteBatch batch){
        float px = Game.camera.position.x - Game.getTelaWidth()/2, py = Game.camera.position.y + Game.getTelaHeight()/2;
        GlyphLayout glyphLayout = new GlyphLayout();
        if (!Game.pause) {

            // munição
            String str = "Ammo: " + Game.getPlayer().getAmmo() + "/" + Game.getPlayer().getTotalAmmo();
            glyphLayout.setText(font, str);
            font.draw(batch, str, px + Game.camera.viewportWidth - glyphLayout.width, py);

            // vida
            batch.end();
            ShapeRenderer shape = new ShapeRenderer();
            shape.setProjectionMatrix(batch.getProjectionMatrix());
            shape.setAutoShapeType(true);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.RED);
            shape.rect(px, py - vida.height, vida.width, vida.height);
            shape.setColor(Color.GREEN);
            shape.rect(px, py - vida.height, (int) (((Game.getPlayer().life * 120) / Game.getPlayer().maxLife)*Game.getTelaWidth()/500), vida.height);

            // Analógico:
            shape.setColor(Color.GRAY);
            shape.circle(px + posX, py + posY - Game.getTelaHeight(), maior.radius);
            shape.setColor(Color.RED);
            shape.circle(px + posX + aX, py + posY - Game.getTelaHeight() + aY, maior.radius / 2);

            // Recarga:
        /*
        shape.setColor(Color.BLUE);
        shape.circle(reload.x, reload.y, maior.radius);
        //*/

            //Trocar armas
        /*
        shape.setColor(Color.BLUE);
        shape.rect(trocar_armas.x, trocar_armas.y, trocar_armas.width, trocar_armas.height);
        //*/
            shape.end();
            //*/
            batch.begin();

            //Mostrar tempo para spawnar o Boss

            str = "" + Game.tempo_boss;
            glyphLayout.setText(font, str);
            font.setColor(Color.YELLOW);
            font.draw(batch, str, px + Game.getTelaWidth()/2 - glyphLayout.width/2, py - Game.getTelaHeight()+glyphLayout.height);

            // armas (clicar para trocar)
            for (int i = 0; i < 3; i++) {
                // trocar_armas.y é 0, logo não é preciso colocar aqui
                batch.draw(armas[2 - i], px + trocar_armas.x, py - Game.getTelaHeight() - (trocar_armas.height / 3) * (3 - i) + trocar_armas.height, 32, 32, armas[2 - i].getRegionWidth(), armas[2 - i].getRegionHeight(), Game.getTelaWidth() / 800, Game.getTelaHeight() / 480, 0);
            }

            // Recarregar arma
            if (Game.getPlayer().reload) {
            /*
            shape.setColor(Color.GREEN);
            shape.circle(px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth(), py +posY- Game.getTelaHeight(), maior.radius);
            */
                recarregar += 15;
                batch.draw(recarregarArma, px - posX - recarregarArma.getRegionWidth() + Game.getTelaWidth() - maior.radius, py + posY - Game.getTelaHeight() - maior.radius, 32, 32, recarregarArma.getRegionWidth(), recarregarArma.getRegionHeight(), Game.getTelaWidth() / 800, Game.getTelaHeight() / 480, recarregar);
            } else {
                batch.draw(recarregarArma, px - posX - recarregarArma.getRegionWidth() + Game.getTelaWidth() - maior.radius, py + posY - Game.getTelaHeight() - maior.radius, 32, 32, recarregarArma.getRegionWidth(), recarregarArma.getRegionHeight(), Game.getTelaWidth() / 800, Game.getTelaHeight() / 480, 0);
            }
        }else{
            /*
            //Vendo os quadrados dos botões
            batch.end();
            ShapeRenderer shape = new ShapeRenderer();
            shape.setProjectionMatrix(batch.getProjectionMatrix());
            shape.setAutoShapeType(true);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (ultimo_jogo){
                // Desenhando "botão" de continuar
                shape.setColor(Color.BLUE);
                shape.rect(px+continuar.x, py+continuar.y-Game.getTelaHeight(), continuar.width, continuar.height);
            }

            // Desenhando "botão" de novo jogo
            shape.setColor(Color.RED);
            shape.rect(px+novo_jogo.x, py+novo_jogo.y-Game.getTelaHeight(), novo_jogo.width, novo_jogo.height);
            shape.end();
            batch.begin();
            //*/
            String str;
            if (ultimo_jogo){
                str = "Continuar";
                font.setColor(Color.BLUE);
                font.draw(batch, str, px + continuar.x, py+continuar.y - Game.getTelaHeight()+continuar.height/2);
            }

            str = "Pontuação Máxima: " + Game.pontuacaoMaxima;
            glyphLayout.setText(font, str);
            font.setColor(Color.GREEN);
            font.draw(batch, str, px + Game.getTelaWidth() - novo_jogo.x - glyphLayout.width, py+novo_jogo.y - Game.getTelaHeight()+novo_jogo.height/2);

            str = "Novo Jogo";
            font.setColor(Color.RED);
            font.draw(batch, str, px + novo_jogo.x, py+novo_jogo.y - Game.getTelaHeight()+novo_jogo.height/2);

            font.getData().setScale(4f*Game.getTelaWidth()/1000);
            str = "Shooting Until Die";
            glyphLayout.setText(font, str);
            font.setColor(Color.RED);
            font.draw(batch, str, px + Game.getTelaWidth()/2 - glyphLayout.width/2, py-glyphLayout.height);
            font.getData().setScale(1.5f*Game.getTelaWidth()/900);
        }

    }
}
