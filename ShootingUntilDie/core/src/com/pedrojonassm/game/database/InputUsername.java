package com.pedrojonassm.game.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class inputEmail implements Input.TextInputListener {
    private String text = "";

    public inputEmail(){
        Gdx.input.getTextInput(this, "Insira seu username", text, "pensa aí num nome bom rapaz");
    }

    @Override
    public void input(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void canceled() {
        Gdx.input.getTextInput(this, "Insira seu username", text, "é necessário preencher maninho");
    }
}
