package com.pedrojonassm.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.pedrojonassm.game.control.Game;

//import io.hasura.sdk.Hasura;
//import io.hasura.sdk.ProjectConfig;

public class AndroidLauncher extends AndroidApplication {
	//ProjectConfig config = new ProjectConfig.Builder().setProjectName("ShootingUntilDie").build();
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		//Hasura.setProjectConfig(config).enableLogs().initialise(this);
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(getFilesDir()), config);
	}

	@Override
	protected void onStop() {
		//dbHelper.escrever();
		super.onStop();
	}
}
