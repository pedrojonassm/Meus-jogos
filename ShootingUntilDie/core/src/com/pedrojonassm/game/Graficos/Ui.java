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
    public Rectangle trocar_armas;
    public Ui(){
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
        maior = new Circle(posX, posY, (int) ((posX+posY)/6));
        reload = new Circle(px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth(), py+posY- Game.getTelaHeight(), (int) ((posX+posY)/6));
        // acrescentar botão para troca das armase
    }
    public void tick(){
    }
    public void render(SpriteBatch batch){
        float px = Game.camera.position.x - Game.getTelaWidth()/2, py = Game.camera.position.y + Game.getTelaHeight()/2;

        // munição
        String str = "Ammo: "+Game.getPlayer().getAmmo()+"/"+Game.getPlayer().getTotalAmmo();
        GlyphLayout glyphLayout = new GlyphLayout(font, str); // Isso serve apenas para pegar o tamanho da String
        font.draw(batch, str, px + Game.camera.viewportWidth-glyphLayout.width, py);

        // vida
        batch.end();
        ShapeRenderer shape = new ShapeRenderer();
        shape.setProjectionMatrix(batch.getProjectionMatrix());
        shape.setAutoShapeType(true);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        shape.rect(px,py-20,120,20);
        shape.setColor(Color.GREEN);
        shape.rect(px,py-20,(int) ((Game.getPlayer().life*120)/Game.getPlayer().maxLife),20);

        // Analógico:
        shape.setColor(Color.GRAY);
        shape.circle(px+posX, py +posY - Game.getTelaHeight(), maior.radius);
        shape.setColor(Color.RED);
        shape.circle(px+posX+aX, py+posY - Game.getTelaHeight()+aY, maior.radius/2);

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
        // armas (clicar para trocar)
        for (int i = 0; i < 3; i++){
            // trocar_armas.y é 0, logo não é preciso colocar aqui
            batch.draw(armas[2-i], px+trocar_armas.x, py - Game.getTelaHeight() - (trocar_armas.height/3)*(3-i) + trocar_armas.height, 32, 32, armas[2-i].getRegionWidth(), armas[2-i].getRegionHeight(), Game.getTelaWidth()/800, Game.getTelaHeight()/480, 0);
        }

        // Recarregar arma
        if (Game.getPlayer().reload){
            /*
            shape.setColor(Color.GREEN);
            shape.circle(px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth(), py +posY- Game.getTelaHeight(), maior.radius);
            */
            recarregar+=15;
            batch.draw(recarregarArma, px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth()-maior.radius, py +posY- Game.getTelaHeight()-maior.radius, 32, 32, recarregarArma.getRegionWidth(), recarregarArma.getRegionHeight(), Game.getTelaWidth()/800, Game.getTelaHeight()/480, recarregar);
        }else {
            batch.draw(recarregarArma, px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth()-maior.radius, py +posY- Game.getTelaHeight()-maior.radius, 32, 32, recarregarArma.getRegionWidth(), recarregarArma.getRegionHeight(), Game.getTelaWidth()/800, Game.getTelaHeight()/480, 0);
        }

    }
}
