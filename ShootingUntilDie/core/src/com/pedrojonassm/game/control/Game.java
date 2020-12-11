package com.pedrojonassm.game.control;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.pedrojonassm.game.Entities.AlienBoss;
import com.pedrojonassm.game.Entities.Disparo;
import com.pedrojonassm.game.Entities.Entity;
import com.pedrojonassm.game.Entities.Insect;
import com.pedrojonassm.game.Entities.Player;
import com.pedrojonassm.game.Entities.TankBoss;
import com.pedrojonassm.game.Graficos.spritesSheet;
import com.pedrojonassm.game.Graficos.Ui;

import java.awt.DisplayMode;

import javax.security.auth.login.Configuration;

public class Game extends ApplicationAdapter {
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static spritesSheet sprites;
	public static Game jogo;
	public static Array<Entity> entities;
	private static Player player;
	public static Texture background;
	private static Ui ui;
	private boolean analogic;
	public static boolean pause;
	public static Array<Disparo> disparos;
	private static Spawner spawner;
	private float moveX, moveY;



	/*
	Ao usar a metraladora, se mover o dedo para fora ele se move
	moveX e moveY vai guardar a posição ao clicar para evitar que isso aconteça
	 */
	@Override
	public void create () {
		spawner = new Spawner();
		disparos = new Array<>();
		pause = false;
		entities = new Array<Entity>();
		jogo = this;

		camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		background = new Texture("background.png");
		sprites = new spritesSheet(new Texture("Assets.png"), 64);
		ui = new Ui();
		// criando player
		player = new Player(26, 34);
		entities.add(new TankBoss());
	}

	public static void bossMorto(){
		spawner.levelUp();
	}

	@Override
	public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

		// testando a alteração das armas
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			player.gun++;
			if (player.gun == player.totalGuns){
				player.gun = 0;
			}
		}
		if (player.position.y > camera.viewportHeight/2 && player.position.y + camera.viewportHeight/2 < background.getHeight()){
			camera.position.y = player.position.y;
		}
		if (player.position.x > camera.viewportWidth/2 && player.position.x + camera.viewportWidth/2 < background.getWidth()){
			camera.position.x = player.position.x;
		}

		// tell the camera to update its matrices.
		camera.update();

		// Spawner para spawnar monstros
		spawner.tick();

		if(Gdx.input.isTouched()) {
			float mx = Gdx.input.getX(), my = camera.viewportHeight-Gdx.input.getY();

			if (ui.maior.contains(mx, my)){
				// está tocando no analógico?
				analogic = true;
				ui.aX = (int) mx - ui.posX;
				ui.aY = (int) my - ui.posY;
				moveX = mx;
				moveY = my;
			}if (!player.atordoado){
				if (player.gun == 1){
					if (Entity.distancia(mx, my, moveX, moveY) <= 20) {
					/*
					verifica se é o dedo que estava no analogico e saiu dele
					isso evita o gasto de munições com a metralhadora
					 */
						moveY = my;
						moveX = mx;
					}else{
						player.rotate(mx, my);
						player.atirar();
					}
				}else if (Gdx.input.justTouched()){
					if (ui.reload.contains(mx, my)) {
						player.reload = true;
					}else {
						player.rotate(mx, my);
						player.atirar();
					}
				}
			}
			if (analogic){
				if (ui.maior.contains(ui.maior.x, my)){
					ui.aY = (int) my - ui.posY;
				}
				if (ui.maior.contains(mx, ui.maior.y)){
					ui.aX = (int) mx - ui.posX;
				}
			}
		}else if (analogic){
			analogic = false;
			ui.aX = 0;
			ui.aY = 0;
		}

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(background, 0, 0);
		for (Entity e : entities){
			if (!pause){
				e.tick();
			}
			e.render(batch);
		}
		if (!pause){
			for (Disparo e : disparos){
				e.tick();
				e.render(batch);
			}
			player.tick();
		}
		player.render(batch);
		ui.tick();
		ui.render(batch);
		batch.end();
	}

	public static Ui getUi() {
		return ui;
	}

	public static Player getPlayer() {
		return player;
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	public static float getTelaHeight() {
		return camera.viewportHeight;
	}

	public static float getTelaWidth() {
		return camera.viewportWidth;
	}
}
