package com.ifrn.expotec;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ifrn.expotec.adapters.AdapterMenu;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.adapters.HttpConnection;
import com.ifrn.expotec.interfaces.AsyncResponse;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AsyncResponse{
    private ActionBarDrawerToggle toggle;
    private Toolbar toobar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView ivNavigation;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private boolean conectado;
    private HashMap<String,List<Atividade>> atividadesPorTipo;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private FrameLayout flErro;
    private CoordinatorLayout clErro;
    private User logged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        toobar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toobar);
        getSupportActionBar().setTitle("Expotec-JC");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        flErro = (FrameLayout) findViewById(R.id.flErro);
        clErro = (CoordinatorLayout) findViewById(R.id.llErro);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout, toobar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
        clErro.setVisibility(View.GONE);
        Button btnRecarregar = (Button) findViewById(R.id.btnRecarregar);
        btnRecarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clErro.setVisibility(View.GONE);
                setData(null);
            }
        });
        if (savedInstanceState != null){
            atividadesPorTipo = (HashMap<String, List<Atividade>>) savedInstanceState.getSerializable("atividades");
            setData(atividadesPorTipo);
        }else{
            setData(null);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.itemHome).setChecked(true);
    }
    public void setData(HashMap<String,List<Atividade>> hashMap){
        DAO dao = new DAO(this);
        logged = dao.getLogged();
        navigationView.addHeaderView(getHeader());
        if (hashMap == null){
            conectado = false;
            if (Controls.isOnline(this)) {
                conectado = true;
                HttpConnection httpConnection = new HttpConnection(MainActivity.this,progressBar);
                httpConnection.delegate = this;
                httpConnection.execute("http://exporest.hol.es/v1/","get");
            }else{
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
                setConfigurationConponents(null);
            }
        }else{
            Iterator itt = hashMap.values().iterator();
            while (itt.hasNext()){
                List<Atividade> list = (List<Atividade>)itt.next();
                tabLayout.addTab(tabLayout.newTab().setText(list.get(0).getTipo()));
            }
            viewPager.setAdapter(new AdapterMenu(getSupportFragmentManager(),tabLayout,hashMap,this,true));
        }
    }
    public void setConfigurationConponents(String mensage){
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        clErro.setVisibility(View.VISIBLE);
        if (mensage != null){
            TextView txtMensage = (TextView) findViewById(R.id.txtShowMensage);
            txtMensage.setText(mensage);
        }
    }
    public View getHeader() {
        View view = getLayoutInflater().inflate(R.layout.drawer_header, null);
        TextView txtNome = (TextView) view.findViewById(R.id.txtNome);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtNome.setText(logged.getNome());
        txtEmail.setText(logged.getEmail());
        SimpleDraweeView  svHeader = (SimpleDraweeView) view.findViewById(R.id.svHeader);
        svHeader.setImageURI(Uri.parse(logged.getPhoto()));
        svHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                /*Intent it = new Intent(context, SetPhoto.class);
                it.putExtra("image", professor.getUriFoto());
                context.startActivity(it);*/
            }
        });
        return view;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("atividades",atividadesPorTipo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void processFinish(String data) {
        progressBar.setVisibility(View.GONE);
        if (data.length() > 0) {
            atividadesPorTipo = Controls.getData(data, tabLayout);
            if (atividadesPorTipo.size() == 0){
                setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
            }else{
                flErro.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setAdapter(new AdapterMenu(MainActivity.this.getSupportFragmentManager(),tabLayout,atividadesPorTipo,MainActivity.this,conectado));
            }
        }else{
            setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
        }

    }

    public void start(Class classe){
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(new Intent(this, classe));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setTitle("Sair");
            x.setMessage("Tem certeza que deseja sair?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            }).setNegativeButton("Não",null).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        switch (id){
            case R.id.itemHome:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.itemAgenda:
                //start( ShowTurmas.class, drawerLayout);
                break;
            case R.id.itemMinhaConta:
                start(Login.class);
                break;
            case R.id.itemSobre:
                break;
            case R.id.itemConfi:
                break;
        }
        return false;
    }
}
