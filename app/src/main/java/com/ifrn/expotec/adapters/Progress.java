package com.ifrn.expotec.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ifrn.expotec.MainActivity;
import com.ifrn.expotec.R;
import com.ifrn.expotec.interfaces.AsyncResponse;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.HttpConnections;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabiano on 13/04/2016.
 */
public class Progress extends AsyncTask<String, Void, String>{
    private ProgressDialog progress;
    private HashMap<String, String> data;
    public AsyncResponse delegate=null;
    private Context context;
    private AdapterAtividade adapterAtividade;
    private int sizeList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;

    public Progress(Context context, HashMap<String, String> data){
        this.data = data;
        this.context = context;
    }
    public Progress(Context context){
        this.context = context;
    }

    public Progress(Context context, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.context = context;
    }

    public Progress(Context context, AdapterAtividade adapterAtividade, int sizeList, SwipeRefreshLayout msSwipeRefreshLayout, RecyclerView recyclerView) {
        this.context = context;
        this.adapterAtividade = adapterAtividade;
        this.sizeList =sizeList;
        this.mSwipeRefreshLayout = msSwipeRefreshLayout;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        if (progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            if (mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(true);
            }else{
                progress.setMessage("Carregando...");
                progress.show();
            }
        }
    }
    @Override
    protected String doInBackground(String... params) {
        if (params[1].equalsIgnoreCase("get")){
            return HttpConnections.getJson(params[0]);
        }else{
            return HttpConnections.performPostCall(params[0],data);
        }
    }
    @Override
    protected void onPostExecute(String result) {
        if (progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
        try {
            if (mSwipeRefreshLayout != null){
                if (result.length() > 0) {
                    final List<Atividade> novos = Controls.recarregar(result);
                    if (novos.size() == 0){
                        snack();
                    }
                    for (int i = 0; i <novos.size() ; i++) {
                        adapterAtividade.addItem(novos.get(i),sizeList);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DAO dao = new DAO(context);
                            for (int i = 0; i <novos.size() ; i++) {
                                dao.insert(novos.get(i).getUser(),false);
                                dao.insert(novos.get(i));
                                System.out.println(novos.get(i).getTipo());
                            }
                            dao.close();
                        }
                    }).start();
                }else{
                    snack();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }else{
                delegate.processFinish(result);
                progress.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void snack(){
        Snackbar.make(recyclerView,"Não há mais atividades!!",Snackbar.LENGTH_LONG).show();
    }
}
