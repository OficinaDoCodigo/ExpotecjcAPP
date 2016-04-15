package com.ifrn.expotec.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ifrn.expotec.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabiano on 15/04/2016.
 */
public class DAO {
    private SQLiteDatabase db;
    private Context context;

    public DAO(Context context){
        db = new DataBase(context).getWritableDatabase();
        this.context = context;
    }
    public User insert(User logged){
        boolean save;
        delete(DataBase.TABLE_LOGIN,"");
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.NOME, logged.getId());
        contentValues.put(DataBase.NOME, logged.getNome());
        contentValues.put(DataBase.SENHA, logged.getPassword());
        contentValues.put(DataBase.URIFOTO,logged.getPhoto());
        contentValues.put(DataBase.EMAIL,logged.getEmail());
        db.insert(DataBase.TABLE_LOGIN, null, contentValues);
        return logged;
    }
    public User getLogged(){
        User user = null;
        Cursor cursor = db.rawQuery("select * from "+DataBase.TABLE_LOGIN+" limit 1;",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(DataBase.ID_User)));
            user.setNome(cursor.getString(cursor.getColumnIndex(DataBase.NOME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DataBase.EMAIL)));
            user.setPhoto(cursor.getString(cursor.getColumnIndex(DataBase.URIFOTO)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(DataBase.SENHA)));
        }
        cursor.close();
        return user;
    }
    public void close(){
        db.close();
    }
    public void delete(String table,String where){
        db.execSQL("delete from " + table + " " + where);
    }
}

