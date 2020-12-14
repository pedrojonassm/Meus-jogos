package com.pedrojonassm.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.pedrojonassm.game.control.Game;

public class AndroidLauncher extends AndroidApplication {
	//FirebaseDatabase database = FirebaseDatabase.getInstance();
	//DatabaseReference myRef = database.getReference("message");
	//DBHelper dbHelper;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		//myRef.setValue("Hello World!");
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
