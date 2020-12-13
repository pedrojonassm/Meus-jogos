package com.pedrojonassm.game.control;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.pedrojonassm.game.Entities.AlienBoss;
import com.pedrojonassm.game.Entities.Disparo;
import com.pedrojonassm.game.Entities.DisparoTanque;
import com.pedrojonassm.game.Entities.Entity;
import com.pedrojonassm.game.Entities.Heals;
import com.pedrojonassm.game.Entities.Insect;
import com.pedrojonassm.game.Entities.Player;
import com.pedrojonassm.game.Entities.Scorpion;
import com.pedrojonassm.game.Entities.SuperDisparo;
import com.pedrojonassm.game.Entities.TankBoss;
import com.pedrojonassm.game.Entities.Troll;
import com.pedrojonassm.game.Graficos.spritesSheet;
import com.pedrojonassm.game.Graficos.Ui;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	public static Array<DisparoTanque> disparostanque;
	public static Array<SuperDisparo> superdisparostanque;
	private static Spawner spawner;
	private float moveX, moveY;
	public static int pontos = 0;
	private static int ultimo_boss;
	private static long tempo_boss;
	public static boolean boss_spawnado = false;
	private File dir;

	public Game(File dir){
		this.dir = dir;
	}

	@Override
	public void create () {
		tempo_para_spawnar_boss();
		spawner = new Spawner();
		disparos = new Array<>();
		disparostanque = new Array<>();
		superdisparostanque = new Array<>();
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
		ler_no_txt();
	}

	public static long tempo(){
		return 1000*60*2;
	}

	private static void tempo_para_spawnar_boss() {
		tempo_boss = System.currentTimeMillis() + tempo(); // 2 min no max para spawnar um boss
	}

	public static void bossMorto(){
		boss_spawnado = false;
		spawner.levelUp();
		tempo_para_spawnar_boss();
	}

	@Override
	public void render () {
		spawnarBoss();
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

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
			}else if (!player.atordoado){
				if (player.gun == 1 && !ui.trocar_armas.contains(mx, my)){
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
					}else if (ui.trocar_armas.contains(mx, my)){
						if (my >= ui.trocar_armas.height*2/3){
							player.gun = 0; // pistola
						}else if (my < ui.trocar_armas.height/3){
							player.gun = 2; // espingarda
						}else{
							player.gun = 1; // Metralhadora
						}
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
			for (Disparo d : disparos){
				d.tick();
				d.render(batch);
			}
			for (DisparoTanque d : disparostanque){
				d.tick();
				d.render(batch);
			}
			for (SuperDisparo d : superdisparostanque){
				d.tick();
				d.render(batch);
			}
			player.tick();
		}
		player.render(batch);
		ui.tick();
		ui.render(batch);
		batch.end();
	}

	private void spawnarBoss() {
		if ((tempo_boss < System.currentTimeMillis() || (pontos%1500 == 0 && pontos > 0)) && !boss_spawnado){
			boss_spawnado = true;
			if (ultimo_boss == 0){
				ultimo_boss++;
				Game.entities.add(new AlienBoss());
			}else{
				ultimo_boss--;
				Game.entities.add(new TankBoss());
			}
		}
	}

	public static Ui getUi() {
		return ui;
	}

	public static Player getPlayer() {
		return player;
	}

	@Override
	public void dispose () {
		// Chamado quando o jogo é fechado
		batch.dispose();
		background.dispose();
		escrever_no_txt();
	}

	private int tem_ultimo_jogo(){
		File file = new File(dir,"salvamento_rapido.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			if (reader.readLine() != null){
				reader.close();
				return 1;
			}else{
				reader.close();
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	private void ler_no_txt(){
		File file = new File(dir,"salvamento_rapido.txt");
		if (!file.exists()){
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String linha = null;
			while ((linha = reader.readLine()) != null){
				String[] str = linha.split(":");
				if (Integer.parseInt(str[0]) == 0){
					player.position.x = Float.parseFloat(str[1]);
					player.position.y = Float.parseFloat(str[2]);
					player.state = Integer.parseInt(str[4]);
					System.out.println(tempo()-Long.parseLong(str[5]));
					tempo_boss -= tempo()-Long.parseLong(str[5]);
					player.index =  Integer.parseInt(str[6]);
					player.life =  Integer.parseInt(str[7]);
					pontos = Integer.parseInt(str[8]); // Player não tem ferido
				}else{
					Entity e = null;
					switch (Integer.parseInt(str[0])){
						case 1:
							e = new Scorpion();
							break;
						case 2:
							e = new Insect();
							break;
						case 3:
							e = new Troll();
							break;
						case 4:
							e = new Heals();
							break;
						case 5:
							e = new AlienBoss();
							break;
						case 6:
							e = new TankBoss();
							break;
					}
					e.position.x = Float.parseFloat(str[1]);
					e.position.y = Float.parseFloat(str[2]);
					e.speed = Integer.parseInt(str[3]);
					e.state = Integer.parseInt(str[4]);
					e.fr = Integer.parseInt(str[5]);
					e.index = Integer.parseInt(str[6]);
					e.life = Integer.parseInt(str[7]);
					e.ferido = Integer.parseInt(str[8]);
					Game.entities.add(e);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void escrever_no_txt() {
		// Salva os dados no arquivo txt, então é carregado para o banco de dados
		File file = new File(dir,"salvamento_rapido.txt");
		BufferedWriter bufferedWriter;
		try {
			file.createNewFile();
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			String str = "";
			str += "0:";
			str += player.position.x + ":";
			str += player.position.y + ":";
			str += player.speed + ":";
			str += player.state + ":";
			str += (tempo_boss-System.currentTimeMillis()) + ":"; // Salva o tempo que já passou para spawnar o boss, deve-se subtrair isso do tempo_boss ao recarregar
			str += player.index + ":";
			str +=  player.life + ":";
			str += pontos+"\n";
			bufferedWriter.write(str);
			for (Entity e : entities) {
				str = "";
				if (e instanceof Scorpion) {
					str += "1:";
				} else if (e instanceof Insect) {
					str += "2:";
				} else if (e instanceof Troll) {
					str += "3:";
				} else if (e instanceof Heals) {
					str += "4:";
				} else if (e instanceof AlienBoss) {
					str += "5:";
				} else if (e instanceof TankBoss) {
					str += "6:";
				}
				str += e.position.x + ":";
				str += e.position.y + ":";
				str += e.speed + ":";
				str += e.state + ":";
				str += e.fr + ":";
				str += e.index + ":";
				str += e.life + ":";
				str += e.ferido + "\n";
				bufferedWriter.write(str);
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static float getTelaHeight() {
		return camera.viewportHeight;
	}

	public static float getTelaWidth() {
		return camera.viewportWidth;
	}

}
