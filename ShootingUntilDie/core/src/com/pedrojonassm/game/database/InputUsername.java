package com.pedrojonassm.game.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pedrojonassm.game.control.Game;

import java.util.List;

import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.GdxFIRDatabase;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class InputUsername implements Input.TextInputListener {

    private static int p = -1;

    public InputUsername(){
        chamaInput();
    }

    private void chamaInput(){
        Gdx.input.getTextInput(this, "Insira seu username", "", "pensa aí num nome bom rapaz");
    }

    @Override
    public void input(String text) {
        if (text != null && text != ""){
            Game.salva_nickname(text);
        }else{
            chamaInput();
        }
    }

    @Override
    public void canceled() {
        Gdx.input.getTextInput(this, "Insira seu username", "", "é necessário preencher maninho");
    }

    public void ler_no_banco(){

    }

}
