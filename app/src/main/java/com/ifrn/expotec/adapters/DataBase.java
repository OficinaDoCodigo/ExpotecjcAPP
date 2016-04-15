package com.ifrn.expotec.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fabiano on 14/04/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    //banco
    public static final String NOME_DB = "expotec";
    public static final int VERSAO =1;
    //professor
    public static final String TABLE_LOGIN = "logged";
    public static final String ID_User = "_id_user";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";
    public static final String SENHA = "senha";
    public static final String URIFOTO= "uri_foto";

    public DataBase(Context context) {
        super(context, NOME_DB, null,VERSAO );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_LOGIN+" ("+
                ID_User+" INTEGER PRIMARY KEY,"+
                NOME+" text not null ,"+
                SENHA+" text not null,"+
                URIFOTO+" text not null,"+
                EMAIL+" text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN+";");
    }
}
