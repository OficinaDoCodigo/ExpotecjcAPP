package com.ifrn.expotec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.User;

import java.util.ResourceBundle;

public class Detalhes extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private SimpleDraweeView fotoUser;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivPerfil;
    private ImageView ivEvent;
    private ImageView ivData;
    private ImageView ivDescricao;
    private TextView txtNome;
    private TextView txtData;
    private TextView txtDescricao;
    private TextView txtMiniCurriculo;
    private TextView txtDuracao;
    private TextView txtVagas;
    private TextView txtTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        getIntance();

        //mudar cor dos icones
        ivPerfil.setImageDrawable(Controls.setColor(getResources().getDrawable( R.drawable.ic_account_circle_white_24dp ),getResources().getColor(R.color.colorPrimary)));
        ivEvent.setImageDrawable(Controls.setColor(getResources().getDrawable( R.drawable.ic_event_white_24dp ),getResources().getColor(R.color.colorPrimary)));
        ivData.setImageDrawable(Controls.setColor(getResources().getDrawable( R.drawable.ic_watch_later_white_24dp ),getResources().getColor(R.color.colorPrimary)));
        ivDescricao.setImageDrawable(Controls.setColor(getResources().getDrawable( R.drawable.ic_description_white_24dp ),getResources().getColor(R.color.colorPrimary)));

        //pega atividade selecionada
        Atividade atividade = (Atividade) getIntent().getSerializableExtra("atividade");

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.AudioFileInfoOverlayText);
        collapsingToolbarLayout.setTitle(atividade.getTipo());

        fotoUser.setImageURI(Uri.parse(atividade.getUser().getPhoto()));
        txtNome.setText(atividade.getUser().getNome());
        txtDescricao.setText(atividade.getDescricao());
        txtDuracao.setText("Duração : "+atividade.getDuracao());
        txtVagas.setText("Vagas : "+atividade.getVagas());
        txtTitulo.setText(atividade.getTitulo());
    }
    public void getIntance(){
        toolbar = (Toolbar) findViewById(R.id.tbDetalhes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPerfil = (ImageView) findViewById(R.id.ivPerfil);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        fotoUser = (SimpleDraweeView)findViewById(R.id.fotoUserDetalhes);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        ivEvent = (ImageView)findViewById(R.id.ivEvent);
        ivData =  (ImageView)findViewById(R.id.ivData);
        ivDescricao =  (ImageView)findViewById(R.id.ivDescricao);

        txtData = (TextView)findViewById(R.id.txtData);
        txtDescricao = (TextView)findViewById(R.id.txtDescricao);
        txtMiniCurriculo = (TextView)findViewById(R.id.txtMiniCuriculo);
        txtNome = (TextView)findViewById(R.id.txtNome);
        txtDuracao = (TextView)findViewById(R.id.txtDuracao);
        txtVagas = (TextView)findViewById(R.id.txtVagas);
        txtTitulo = (TextView)findViewById(R.id.txtTitulo);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
