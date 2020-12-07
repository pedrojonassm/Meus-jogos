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
    public Circle maior, troca, reload;
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
        maior = new Circle(posX, posY, (int) ((posX+posY)/6));
        reload = new Circle(px-posX-recarregarArma.getRegionWidth() + Game.getTelaWidth(), py+posY- Game.getTelaHeight(), (int) ((posX+posY)/6));
        // acrescentar botão para troca das armase
    }
    public void tick(){
    }
    public void render(SpriteBatch batch){
        float px = Game.camera.position.x - Game.camera.viewportWidth/2, py = Game.camera.position.y + Game.camera.viewportHeight/2;

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

        // troca:
        /*
        shape.setColor(Color.GRAY);
        shape.circle(reload.x, reload.y, maior.radius);
        //*/
        shape.end();

        batch.begin();

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
