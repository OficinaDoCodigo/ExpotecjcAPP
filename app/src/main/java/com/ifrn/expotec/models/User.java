package com.ifrn.expotec.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;

/**
 * Created by Fabiano on 11/04/2016.
 */
public class User implements Serializable {
    private String nome;
    private String id;
    private String sobreNome;
    private String password;
    private String email;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
