package com.ifrn.expotec;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
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
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.User;
import com.melnykov.fab.ObservableScrollView;

import java.util.List;
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
    private FloatingActionButton FAB;
    private com.melnykov.fab.FloatingActionButton FABLand;
    private boolean participando = false;
    private User logged;
    private CoordinatorLayout coordinatorLayout;
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
        String tipo = atividade.getTipo();
        collapsingToolbarLayout.setTitle(tipo.substring(0,1).toUpperCase()+tipo.substring(1,tipo.length()));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));

        fotoUser.setImageURI(Uri.parse(atividade.getUser().getPhoto()));
        txtNome.setText(atividade.getUser().getNome());
        txtDescricao.setText(atividade.getDescricao());
        txtDuracao.setText("Duração : "+atividade.getDuracao());
        txtVagas.setText("Vagas : "+atividade.getVagas());
        txtTitulo.setText(atividade.getTitulo());
        DAO dao = new DAO(Detalhes.this);
        logged = dao.getLogged();
        if (logged != null){
            if (logged.getId().equals(atividade.getUser().getId())){
                Snackbar.make(coordinatorLayout,"você é o ministrante dessa atividade!",Snackbar.LENGTH_LONG).show();
                FAB.setVisibility(View.GONE);
            }else{
                List<Integer> minhasAtividades = dao.getMinhasAtividades(Integer.parseInt(logged.getId()));
                for (int i = 0; i < minhasAtividades.size(); i++) {
                    if (minhasAtividades.get(i) == atividade.getId()){
                        participando = true;
                        FAB.setImageResource(R.drawable.ic_close_white_24dp);
                        Snackbar.make(coordinatorLayout,"você é um participante dessa atividade!",Snackbar.LENGTH_LONG).show();
                        break;
                    }else{
                        Snackbar.make(coordinatorLayout,"você não é participante dessa atividade!",Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
        dao.close();
        if (FAB != null){
            FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFAB();
                }
            });
        }else{
            FABLand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFAB();
                }
            });
        }
    }
    public void clickFAB(){
        AlertDialog.Builder alertParticipar = new AlertDialog.Builder(Detalhes.this);
        if (logged == null){
            alertParticipar.setMessage("Você não está logado, fazer loggin?").setNegativeButton("Não",null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Detalhes.this,Login.class));
                }
            }).setTitle("Participar").show();
        }else{
            if (participando){
                alertParticipar.setMessage("Cancelar participação?").setNegativeButton("Não",null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle("Participar").show();
            }else{
                alertParticipar.setMessage("Participar dessa atividade?").setNegativeButton("Não",null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle("Participar").show();
            }
        }
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
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        txtData = (TextView)findViewById(R.id.txtData);
        txtDescricao = (TextView)findViewById(R.id.txtDescricao);
        txtMiniCurriculo = (TextView)findViewById(R.id.txtMiniCuriculo);
        txtNome = (TextView)findViewById(R.id.txtNome);
        txtDuracao = (TextView)findViewById(R.id.txtDuracao);
        txtVagas = (TextView)findViewById(R.id.txtVagas);
        txtTitulo = (TextView)findViewById(R.id.txtTitulo);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ObservableScrollView observableScrollView = (ObservableScrollView)findViewById(R.id.observableScrollView);
            FABLand = (com.melnykov.fab.FloatingActionButton)findViewById(R.id.FABParticiparDaAtividade);
            FABLand.attachToScrollView(observableScrollView);
        }else{
            FAB = (FloatingActionButton) findViewById(R.id.FABParticiparDaAtividade);
        }
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
