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

    public static final String TABLE_USER = "user";
    public static final String TABLE_LOGIN = "logged";
    public static final String ID_USER = "_id_user";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";
    public static final String SENHA = "senha";
    public static final String URIFOTO= "uri_foto";


    public static final String TABLE_ATIVIDADE = "atividade";
    public static final String TITULO = "titulo" ;
    public static final  String TIPO = "tipo";
    public static final String DURACAO = "duracao";
    public static final String DESCRICAO = "descricao";
    public static final String VAGAS = "vagas" ;
    public static final String ID_ATIVIDADE = "_id_atividade";

    public static final String TABLE_MINHAS_ATIVIDADES = "minhas_atividades";
    public DataBase(Context context) {
        super(context, NOME_DB, null,VERSAO );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_LOGIN+" ("+
                ID_USER +" INTEGER PRIMARY KEY,"+
                NOME+" text not null ,"+
                SENHA+" text not null,"+
                URIFOTO+" text not null,"+
                EMAIL+" text not null);");

        db.execSQL("CREATE TABLE "+TABLE_USER+" ("+
                ID_USER +" INTEGER PRIMARY KEY,"+
                NOME+" text not null ,"+
                URIFOTO+" text,"+
                EMAIL+" text not null);");

        db.execSQL("CREATE TABLE "+TABLE_ATIVIDADE+" ("+
                ID_ATIVIDADE+"  INTEGER PRIMARY KEY ,"+
                ID_USER +"  REFERENCES "+TABLE_USER+" ("+ ID_USER +"), "+
                TITULO+" text not null ,"+
                DURACAO+" text,"+
                DESCRICAO+" text ,"+
                VAGAS+" text,"+
                TIPO+" text not null);");
        db.execSQL("CREATE TABLE "+TABLE_MINHAS_ATIVIDADES+" ("+
                ID_ATIVIDADE+"  INTEGER PRIMARY KEY ,"+
                ID_USER +"  REFERENCES "+TABLE_LOGIN+" ("+ ID_USER +"));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN+";");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER+";");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ATIVIDADE+";");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MINHAS_ATIVIDADES+";");
    }
}
