package com.ifrn.expotec;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ifrn.expotec.adapters.AdapterMenu;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.HttpProgress;
import com.ifrn.expotec.models.Atividade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private Toolbar toobar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView ivNavigation;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    HashMap<String,List<Atividade>> listHashMap;
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
    public HashMap<String,List<Atividade>> setData(HashMap<String,List<Atividade>> hashMap){
        if (hashMap == null){
            HttpProgress httpConnection = new HttpProgress(this,"http://192.168.0.107/exporest/v1/","get",null);
            String data = "";
            HashMap<String,List<Atividade>> atividadesPorTipo = null;
            boolean conectado = false;
            if (Controls.isOnline(this)) {
                conectado = true;
                try {
                    data = httpConnection.execute().get();
                    if (data.length() > 0) {
                        findViewById(R.id.llErro).setVisibility(View.GONE);
                        atividadesPorTipo = Controls.getData(data,tabLayout);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                    }else{
                        setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setConfigurationConponents("Desculpe, mas ocorreu um erro, tente novamente!");
                }
            }else{
                setConfigurationConponents(null);
            }

            viewPager.setAdapter(new AdapterMenu(getSupportFragmentManager(),tabLayout,atividadesPorTipo,this,conectado));
            /*HashMap<String,String> names = new HashMap<>();
            names.put("nome","fabiano");
            names.put("email","fabiano@gmail.com");
            names.put("senha","gmail.com098087");*/
            return atividadesPorTipo;
        }else{
            Iterator itt = hashMap.values().iterator();
            while (itt.hasNext()){
                List<Atividade> list = (List<Atividade>)itt.next();
                tabLayout.addTab(tabLayout.newTab().setText(list.get(0).getTipo()));
            }
            viewPager.setAdapter(new AdapterMenu(getSupportFragmentManager(),tabLayout,hashMap,this,true));
            return hashMap;
        }
    }
    public void setConfigurationConponents(String mensage){
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        findViewById(R.id.recyclerView).setVisibility(View.GONE);
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
}
