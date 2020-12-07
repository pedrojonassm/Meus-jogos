package com.pedrojonassm.game.Graficos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class spritesSheet {
    Texture fullImage;
    private int quadrados, tamanho;

    public spritesSheet(Texture full, int tamanho){
        fullImage = full;
        this.tamanho = tamanho;
        quadrados = full.getWidth()/tamanho;
    }

    public TextureRegion getAsset(int x, int y){
        return new TextureRegion(fullImage, x*tamanho, y*tamanho, tamanho, tamanho);
    }

    public TextureRegion getAsset2(int x, int y, int width, int heigh){
        return new TextureRegion(fullImage, x, y, width, heigh);
    }

    public int getQuadrados() {
        return quadrados;
    }

    public int getTamanho() {
        return tamanho;
    }
}
