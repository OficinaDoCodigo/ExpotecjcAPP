package com.ifrn.expotec.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ifrn.expotec.R;
import com.ifrn.expotec.adapters.AdapterAtividade;
import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.HttpConnections;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atividades, container, false);

        ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Controls.alert(getActivity(),"refresh");
                mSwipeRefreshLayout.setRefreshing(false);
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
                int height = recyclerView.getChildAt(0).getHeight()*(recyclerView.getChildCount()+1);
                if (height > screenHeight ){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (mList.size() == linearLayoutManager.findLastCompletelyVisibleItemPosition()+1){
                        List<Atividade> novos = new ArrayList<Atividade>();
                        Atividade ultimaAtividade = mList.get(mList.size()-1);
                        if (ultimaAtividade.getTipo().equals("mesa redonda")){
                            ultimaAtividade.setTipo("mesa_redonda");
                        }
                        mSwipeRefreshLayout.setRefreshing(true);
                        HttpProgress httpConnection = new HttpProgress("http://exporest.hol.es/v1/"+ultimaAtividade.getTipo()+"/starting/"+ultimaAtividade.getId(),"get",null);
                        httpConnection.execute();
                        mSwipeRefreshLayout.setRefreshing(false);
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


    private class HttpProgress extends AsyncTask<Void, Void, String> {
        String urlString;
        private String method;
        private ProgressDialog progress;
        private HashMap<String, String> data;
        public HttpProgress(String urlString, String method, HashMap<String, String> data){
            this.urlString = urlString;
            this.method = method;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("carregando");
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (method.equalsIgnoreCase("get")){
                return HttpConnections.getJson(urlString);
            }else{
                return HttpConnections.performPostCall(urlString,data);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (method.equals("get")){
                if (result.length() > 0) {
                    List<Atividade> novos = Controls.recarregar(result);
                    for (int i = 0; i <novos.size() ; i++) {
                        novos.get(i).setTitulo(novos.get(i).getTitulo());
                        adapterAtividade.addItem(novos.get(i),mList.size());
                    }
                }
            }
        }
    }
}
