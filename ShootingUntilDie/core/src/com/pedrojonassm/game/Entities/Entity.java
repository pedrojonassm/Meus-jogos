package com.pedrojonassm.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.pedrojonassm.game.control.Game;

public class Entity {
    protected Array<TextureRegion[]> sprites;
    protected int index, pivoX, pivoY, state, fr = 0, maxFr = 0, speed, tamanhoX, tamanhoY, Z = 0, morte = -1;
    public int life = 0, maxLife;
    public int ferido = 0;
    protected Array<Integer> maxIndex;
    protected float rotation;
    public Rectangle position;
    protected boolean atacar = false;

    public Entity(int pX, int pY, int tX, int tY){
        tamanhoX = tX;
        tamanhoY = tY;
        position = new Rectangle(0, 0, tamanhoX, tamanhoY);
        life = 1;
        pivoX = pX;
        pivoY = pY;
        sprites = new Array<TextureRegion[]>();
        maxIndex = new Array<Integer>();
        state = 0;
    }

    public void spawnar(){
        do{
            position.x = MathUtils.random(tamanhoX, Game.background.getWidth()-tamanhoX);
            position.y = MathUtils.random(tamanhoY, Game.background.getHeight()-tamanhoY);
        }while(distancia(position.x, position.y, Game.getPlayer().position.x, Game.getPlayer().position.y) < 500);
    }

    public void addSprites (int frames, int xI, int yI){
        maxIndex.add(frames);
        TextureRegion[] t = new TextureRegion[frames];
        for (int i = 0; i < frames; i++){
            t[i] = Game.sprites.getAsset(xI, yI);
            xI++;
            if (xI >= Game.sprites.getQuadrados()){
                xI = 0;
                yI++;
            }
        }
        sprites.add(t);
    }

    public void addSprites2(String s1, String s2, int total, int add, int x, int y, int width, int height){
        maxIndex.add(total);
        TextureRegion[] t = new TextureRegion[total];
        for (int i = 0; i < total; i++){
            t[i] = new TextureRegion(new Texture(s1+(i+add)+s2), x, y, width, height);

        }
        sprites.add(t);
    }

    public void tick(){
        /*if (life <= 0){
            Game.entities.removeValue(this, true);
            return;
        }*/
        fr++;
        if (fr >= maxFr){
            fr = 0;
            index++;
        }
        if (index >= maxIndex.get(state)){
            index = 0;
            endAnimation();
        }
    }

    public void endAnimation(){}

    public void render(SpriteBatch batch){
        // Exemplo de renderização dos sprites


        //batch.draw(sprites.get(index), position.x, position.y);

        if (toRender()) {
            if (state < sprites.size){
                batch.draw(sprites.get(state)[index], position.x, position.y, pivoX, pivoY, tamanhoX, tamanhoY, 1, 1, rotation);
            }
        }
    }

    public boolean toRender(){
        float py = position.y-(Game.jogo.camera.position.y-Game.jogo.camera.viewportHeight/2) + Z;
        float px = position.x-(Game.jogo.camera.position.x-Game.jogo.camera.viewportWidth/2);
        if (py > -tamanhoY && py < Game.jogo.camera.viewportHeight && px > -tamanhoX && px < Game.jogo.camera.viewportWidth){
            return true;
        }
        return false;
    }

    public void lookAt(Entity toLook){
        rotation = MathUtils.atan2((toLook.position.y+toLook.pivoY)-(position.y+pivoY), (toLook.position.x+toLook.pivoX)-(position.x+pivoX))*MathUtils.radiansToDegrees;
    }

    public static double distancia(float x1, float y1, float x2, float y2){
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }


}
