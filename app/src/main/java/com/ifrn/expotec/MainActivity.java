package com.ifrn.expotec;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ifrn.expotec.adapters.AdapterMenu;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.HttpConnection;
import com.ifrn.expotec.interfaces.AsyncResponse;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.HttpConnections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse{
    private ActionBarDrawerToggle toggle;
    private Toolbar toobar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView ivNavigation;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private boolean conectado;
    private HashMap<String,List<Atividade>> listHashMap;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
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
        toggle = new ActionBarDrawerToggle(this,drawerLayout, toobar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);

        Button btnRecarregar = (Button) findViewById(R.id.btnRecarregar);
        btnRecarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(null);
            }
        });
        if (savedInstanceState != null){
            setData((HashMap<String, List<Atividade>>) savedInstanceState.getSerializable("atividades"));
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

    }

    int count = 0;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(count == 0){
            count = 1;
            setData(null);
        }
    }
    public void setData(HashMap<String,List<Atividade>> hashMap){
        if (hashMap == null){
            HashMap<String,List<Atividade>> atividadesPorTipo = null;
            conectado = false;
            if (Controls.isOnline(this)) {
                conectado = true;
                HttpConnection httpConnection = new HttpConnection(MainActivity.this);
                httpConnection.delegate = this;
                httpConnection.execute("http://exporest.hol.es/v1/","get");
            }else{
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("carregando...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
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
        findViewById(R.id.llErro).setVisibility(View.VISIBLE);
        if (mensage != null){
            TextView txtMensage = (TextView) findViewById(R.id.txtShowMensage);
            txtMensage.setText(mensage);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("atividades",listHashMap);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void processFinish(String data) {
        if (data.length() > 0) {
            HashMap<String, List<Atividade>> atividadesPorTipo = Controls.getData(data, tabLayout);
            if (atividadesPorTipo.size() == 0){
                setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
            }else{
                findViewById(R.id.llErro).setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setAdapter(new AdapterMenu(MainActivity.this.getSupportFragmentManager(),tabLayout,atividadesPorTipo,MainActivity.this,conectado));
            }
        }else{
            setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
        }

    }
}
