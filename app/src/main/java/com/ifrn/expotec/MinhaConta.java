package com.ifrn.expotec;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.adapters.DataBase;
import com.ifrn.expotec.models.User;

import java.util.List;

public class MinhaConta extends AppCompatActivity {
    private ImageView ivPerfil;
    private ImageView ivEvent;
    private ImageView ivEvent2;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SimpleDraweeView fotoUser;
    private TextView txtEmail;
    private Toolbar tbMinhaConta;
    private LinearLayout llParticipante;
    private LinearLayout llMinistrante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        intance();
        setSupportActionBar(tbMinhaConta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User logged = (User) getIntent().getSerializableExtra("logged");

        ivPerfil.setImageDrawable(Controls.setColor(getResources().getDrawable( R.drawable.ic_account_circle_white_24dp ),getResources().getColor(R.color.colorPrimary)));
        Drawable eventoIcon = Controls.setColor(getResources().getDrawable( R.drawable.ic_event_white_24dp ),getResources().getColor(R.color.colorPrimary));
        ivEvent.setImageDrawable(eventoIcon);
        ivEvent2.setImageDrawable(eventoIcon);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.AudioFileInfoOverlayText);
        collapsingToolbarLayout.setTitle(logged.getNome());
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));

        fotoUser.setImageURI(Uri.parse(logged.getPhoto()));
        txtEmail.setText(logged.getEmail());

        DAO dao = new DAO(this);
        List<Integer> ids = dao.getMinhasAtividades(Integer.parseInt(dao.getLogged().getId()));
        dao.close();
    }
    public void intance(){
        tbMinhaConta = (Toolbar) findViewById(R.id.tbMinhaConta);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        ivEvent = (ImageView)findViewById(R.id.ivEvent);
        ivEvent2 = (ImageView)findViewById(R.id.ivEvent2);
        ivPerfil =  (ImageView)findViewById(R.id.ivPerfil);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        fotoUser = (SimpleDraweeView) findViewById(R.id.fotoUser);
        llParticipante = (LinearLayout) findViewById(R.id.llParticipante);
        llMinistrante = (LinearLayout) findViewById(R.id.llMinistrante);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            if (getIntent().getStringExtra("recarregar") != null){
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
                startActivity(intent);
            }else{
                onBackPressed();
            }
        }else if(id == R.id.itemSair){
            DAO dao = new DAO(this);
            dao.delete(DataBase.TABLE_LOGIN,"");
            dao.close();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.minha_conta, menu);
        return true;
    }
}
