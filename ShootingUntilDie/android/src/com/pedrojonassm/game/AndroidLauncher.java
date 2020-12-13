package com.pedrojonassm.game;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pedrojonassm.game.control.Game;

import java.io.File;

public class AndroidLauncher extends AndroidApplication {
	DBHelper dbHelper;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//dbHelper = new DBHelper(this, getFilesDir());
		//dbHelper.ler();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(getFilesDir()), config);
	}

	@Override
	protected void onStop() {
		//dbHelper.escrever();
		super.onStop();
	}
}
