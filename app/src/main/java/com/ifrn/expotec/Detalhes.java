package com.ifrn.expotec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import java.lang.ref.WeakReference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.models.Atividade;

import java.util.ResourceBundle;

public class Detalhes extends AppCompatActivity {
    AppBarLayout appBarLayout;
    Animation fadeout;
    Animation fadein;
    Toolbar toolbar;
    Button b;
    SimpleDraweeView fotoUser;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        toolbar = (Toolbar) findViewById(R.id.tbDetalhes);
        setSupportActionBar(toolbar);

        Atividade atividade = (Atividade) getIntent().getSerializableExtra("atividade");
        Controls.alert(this,atividade.getTipo());
        fotoUser = (SimpleDraweeView)findViewById(R.id.fotoUserDetalhes);
        fotoUser.setImageURI(Uri.parse(atividade.getUser().getPhoto()));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });
    }
}
