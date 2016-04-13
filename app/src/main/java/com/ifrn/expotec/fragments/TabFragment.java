package com.ifrn.expotec.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ifrn.expotec.R;
import com.ifrn.expotec.adapters.AdapterAtividade;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.HttpProgress;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TabFragment extends Fragment {
    private List<Atividade> mList = new ArrayList<>();
    private TextView txtMensage;
    AdapterAtividade adapterAtividade;
    boolean conectado;
    HashMap<String,List<Atividade>> atividadesPorTipo;
    String key;
    @SuppressLint("ValidFragment")
    public TabFragment(boolean conectado,HashMap<String,List<Atividade>> atividadesPorTipo,String key){
        this.atividadesPorTipo = atividadesPorTipo;
        this.conectado = conectado;
        this.key = key;
    }
    public TabFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atividades, container, false);

        ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        txtMensage = (TextView) view.findViewById(R.id.txtShowMensage);
        if(savedInstanceState != null){
            mList = (List<Atividade>) savedInstanceState.getSerializable("atividades");
        }else{
            if (atividadesPorTipo == null && conectado){
                txtMensage.setText("Desculpe, mas ocorreu um erro!");
            }else{
                mList = atividadesPorTipo.get(key);
                if(!atividadesPorTipo.containsKey(key)){
                    txtMensage.setText("Nenhum resultado encontrado para essa categoria!");
                }
            }
        }
        try{
            if (mList.size() < 1){
                recyclerView.setVisibility(View.GONE);
            }else{
                scrollView.setVisibility(View.GONE);
                try{
                    adapterAtividade = new AdapterAtividade(getActivity(),mList);
                    recyclerView.setAdapter(adapterAtividade);
                }catch (Exception e){}
            }
        }catch (Exception e){

        }
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (mList.size() == linearLayoutManager.findLastCompletelyVisibleItemPosition()+1){
                    List<Atividade> novos = new ArrayList<Atividade>();
                    Atividade ultimaAtividade = mList.get(mList.size()-1);
                    if (ultimaAtividade.getTipo().equals("mesa redonda")){
                        ultimaAtividade.setTipo("mesa_redonda");
                    }
                    HttpProgress httpConnection = new HttpProgress(getActivity(),"http://192.168.0.107/exporest/v1/"+ultimaAtividade.getTipo()+"/starting/"+ultimaAtividade.getId(),"get",null);
                    String data = "";
                    try {
                        data= httpConnection.execute().get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        novos = Controls.recarregar(data);
                        for (int i = 0; i <novos.size() ; i++) {
                            novos.get(i).setTitulo(novos.get(i).getTitulo());
                            adapterAtividade.addItem(novos.get(i),mList.size());
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
        Button btnRecarregar = (Button)view.findViewById(R.id.btnRecarregar);
        btnRecarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("atividades", (Serializable) mList);
        super.onSaveInstanceState(outState);
    }
}
