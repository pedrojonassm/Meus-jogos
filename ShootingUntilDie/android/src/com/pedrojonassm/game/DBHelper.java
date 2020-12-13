package com.pedrojonassm.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DBHelper extends SQLiteOpenHelper {
    private static final String nome_banco = "entidades";
    private static final int versao_banco = 1;
    private Context context;
    private SQLiteDatabase dbinstancia = null;
    private File dir;

    public DBHelper(Context context, File dir) {
        super(context, nome_banco, null, versao_banco);
        this.dir = dir;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+nome_banco+"(tipo integer,posicaoX integer,posicaoY integer,speed integer,state integer,fr integer, indice integer,life integer,ferido integer);");
    }

    public ContentValues getContentValues(String[] str){
        ContentValues cv = new ContentValues();
        cv.put("tipo", str[0]);
        cv.put("posicaoX", str[1]);
        cv.put("posicaoY", str[2]);
        cv.put("speed", str[3]);
        cv.put("state", str[4]);
        cv.put("fr", str[5]);
        cv.put("indice", str[6]);
        cv.put("life", str[7]);
        cv.put("ferido", str[8]);
        System.out.println(str.toString());
        return cv;
    }

    public void escrever(){

        File file = new File(dir,"salvamento_rapido.txt");
        try {
            if (!file.exists()){
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String linha = null;
            abrirDB();
            while ((linha = reader.readLine()) != null){
                dbinstancia.insert(nome_banco, null, getContentValues(linha.split(":")));
            }
            fecharDB();
            reader.close();
        } catch (Exception e) {
        }
        file.delete();

    }

    public void abrirDB() throws SQLException {
        if (this.dbinstancia == null){
            this.dbinstancia = this.getWritableDatabase();
        }
    }
    public void fecharDB() throws SQLException{
        if (this.dbinstancia != null && this.dbinstancia.isOpen()) {
            this.dbinstancia.close();
            dbinstancia = null;
        }
    }

    public void ler(){
        abrirDB();
        Cursor c = dbinstancia.rawQuery("select * from "+nome_banco, null);
        File file = new File(dir,"salvamento_rapido.txt");
        BufferedWriter bufferedWriter;
        try {
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));

            while (c.moveToNext()) {
                String str = "";
                str += c.getColumnIndex("tipo")+":";
                str += c.getColumnIndex("posicaoX")+":";
                str += c.getColumnIndex("posicaoY")+":";
                str += c.getColumnIndex("speed")+":";
                str += c.getColumnIndex("state")+":";
                str += c.getColumnIndex("fr")+":";
                str += c.getColumnIndex("indice")+":";
                str += c.getColumnIndex("life")+":";
                str += c.getColumnIndex("ferido")+"\n";
                bufferedWriter.write(str);
            }
            bufferedWriter.close();
            dbinstancia.execSQL("delete from "+nome_banco);
        }catch (Exception e) {
            return;
        }
        fecharDB();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
