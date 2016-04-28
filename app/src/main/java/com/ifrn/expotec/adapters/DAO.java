package com.ifrn.expotec.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ifrn.expotec.models.Atividade;
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
    public User insert(User user, boolean loggin){
        ContentValues contentValues = new ContentValues();
        if (loggin){
            delete(DataBase.TABLE_LOGIN,"");
            contentValues.put(DataBase.SENHA, user.getPassword());
        }
        contentValues.put(DataBase.ID_USER, user.getId());
        contentValues.put(DataBase.NOME, user.getNome());
        contentValues.put(DataBase.URIFOTO,user.getPhoto());
        contentValues.put(DataBase.EMAIL,user.getEmail());
        if (loggin){
            db.insert(DataBase.TABLE_LOGIN, null, contentValues);
        }else{
            if (!exist(DataBase.TABLE_USER,"where "+DataBase.ID_USER+" = "+user.getId())){
                db.insert(DataBase.TABLE_USER, null, contentValues);
                return user;
            }else{
                return null;
            }
        }
        return user;
    }
    public Atividade insert(Atividade atividade){
        if (!exist(DataBase.TABLE_ATIVIDADE,"where "+DataBase.ID_ATIVIDADE+" = "+atividade.getId())){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBase.ID_ATIVIDADE, atividade.getId());
            contentValues.put(DataBase.TITULO, atividade.getTitulo());
            contentValues.put(DataBase.TIPO, atividade.getTipo());
            contentValues.put(DataBase.DURACAO, atividade.getDuracao());
            contentValues.put(DataBase.DESCRICAO,atividade.getDescricao());
            contentValues.put(DataBase.VAGAS,atividade.getVagas());
            contentValues.put(DataBase.ID_USER,atividade.getUser().getId());

            db.insert(DataBase.TABLE_ATIVIDADE, null, contentValues);
            return atividade;
        }else{
            return null;
        }
    }
    public List<Atividade> getAtividades(String tipo){
        User user = null;
        Cursor cursor = db.rawQuery("select * from "+DataBase.TABLE_ATIVIDADE
                +" join "+ DataBase.TABLE_USER+" on "
                +DataBase.TABLE_ATIVIDADE+"."+DataBase.ID_USER+" = "+DataBase.TABLE_USER+"."+DataBase.ID_USER
                +" where "+DataBase.TIPO +" = "+tipo+";",null);
        List<Atividade> atividadeList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(DataBase.ID_USER)));
                user.setNome(cursor.getString(cursor.getColumnIndex(DataBase.NOME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DataBase.EMAIL)));
                user.setPhoto(cursor.getString(cursor.getColumnIndex(DataBase.URIFOTO)));

                Atividade atividade = new Atividade();
                atividade.setUser(user);
                atividade.setTipo(cursor.getString(cursor.getColumnIndex(DataBase.TIPO)));
                atividade.setDescricao(cursor.getString(cursor.getColumnIndex(DataBase.DESCRICAO)));
                atividade.setDuracao(cursor.getString(cursor.getColumnIndex(DataBase.DURACAO)));
                atividade.setTitulo(cursor.getString(cursor.getColumnIndex(DataBase.TITULO)));
                atividade.setVagas(cursor.getString(cursor.getColumnIndex(DataBase.VAGAS)));
                atividade.setId(cursor.getInt(cursor.getColumnIndex(DataBase.ID_ATIVIDADE)));

                atividadeList.add(atividade);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return atividadeList;
    }
    public boolean exist(String dataBase, String where){
        Cursor cursor = db.rawQuery("select * from "+dataBase+" "+where+" ;",null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }
    public User getLogged(){
        User user = null;
        Cursor cursor = db.rawQuery("select * from "+DataBase.TABLE_LOGIN+" limit 1;",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(DataBase.ID_USER)));
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

