package com.ifrn.expotec.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ifrn.expotec.R;
import com.ifrn.expotec.adapters.AdapterAtividade;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.adapters.Progress;
import com.ifrn.expotec.models.Atividade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabFragment extends Fragment{
    private List<Atividade> mList = new ArrayList<>();
    private TextView txtMensage;
    AdapterAtividade adapterAtividade;
    boolean conectado;
    ViewGroup viewGroup;
    Atividade ultimaAtividade;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    @SuppressLint("ValidFragment")
    public TabFragment(boolean conectado,List<Atividade> mList){
        this.mList = mList;
        this.conectado = conectado;
    }
    public TabFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atividades, container, false);

        ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        txtMensage = (TextView) view.findViewById(R.id.txtShowMensage);
        if(savedInstanceState != null){
            mList = (List<Atividade>) savedInstanceState.getSerializable("atividades");
        }else{
            if (mList == null && conectado){
                txtMensage.setText("Desculpe, mas ocorreu um erro!");
            }else{
                if(mList.size() == 0){
                    txtMensage.setText("Nenhum resultado encontrado para essa categoria!");
                }
            }
        }
        try{
            if (mList.size() < 1){
                recyclerView.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }else{
                scrollView.setVisibility(View.GONE);
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DAO dao = new DAO(getActivity());
                            for (int i = 0; i <mList.size() ; i++) {
                                dao.insert(mList.get(i).getUser(),false);
                                dao.insert(mList.get(i));
                            }
                            dao.close();
                        }
                    }).start();
                    adapterAtividade = new AdapterAtividade(getActivity(),mList);
                    recyclerView.setAdapter(adapterAtividade);
                }catch (Exception e){}
            }
        }catch (Exception e){}
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorBlue,
                R.color.colorGreen);
       mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recarregar();
            }
        });


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int screenHeight = displayMetrics.heightPixels;
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int sizeAntigo = mList.size();
                if (mList.size() > 4){
                    if (mList.size() == linearLayoutManager.findLastCompletelyVisibleItemPosition()+1){
                        ultimaAtividade = mList.get(mList.size()-1);
                        if (ultimaAtividade.getTipo().equals("mesa redonda")){
                            ultimaAtividade.setTipo("mesa_redonda");
                        }
                        if (Controls.isOnline(getActivity())){
                            Progress progress = new Progress(getContext(),adapterAtividade,mList.size(),mSwipeRefreshLayout,recyclerView);
                            progress.execute("http://exporest.hol.es/v1/"+ultimaAtividade.getTipo()+"/starting/"+ultimaAtividade.getId(),"get");
                        }else {
                            Snackbar.make(recyclerView,"Você está desconectado!",Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        Button btnRecarregar = (Button)view.findViewById(R.id.btnRecarregar);
        btnRecarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recarregar();
            }
        });
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("atividades", (Serializable) mList);
        super.onSaveInstanceState(outState);
    }
    public void recarregar(){
        if(Controls.isOnline(getActivity())){
            try {
                ultimaAtividade = mList.get(0);
                if (ultimaAtividade.getTipo().equals("mesa redonda")){
                    ultimaAtividade.setTipo("mesa_redonda");
                }
                adapterAtividade.removeAll();
            }catch (Exception e){}
            Progress progress = new Progress(getContext(),adapterAtividade,mList.size(),mSwipeRefreshLayout,recyclerView);
            progress.execute("http://exporest.hol.es/v1/"+ultimaAtividade.getTipo()+"/starting/"+ultimaAtividade.getId()+2,"get");
        }else{
            Snackbar.make(recyclerView,"Você está desconectado!",Snackbar.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
