package com.ifrn.expotec.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ifrn.expotec.Detalhes;
import com.ifrn.expotec.R;
import com.ifrn.expotec.models.Atividade;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fabiano on 11/04/2016.
 */
public class AdapterAtividade extends RecyclerView.Adapter<AdapterAtividade.MyViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Atividade> mList;
    private Context context;
    public AdapterAtividade(Context c,List<Atividade> mList){
        this.mList = mList;
        context = c;
        mLayoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public AdapterAtividade.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_atividade,parent,false);
        MyViewHolder mvh =  new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(AdapterAtividade.MyViewHolder holder, int position) {
        final Atividade atividade = mList.get(position);
        holder.txtNome.setText("Por "+atividade.getUser().getNome());
        if(atividade.getDescricao().length() > 80){
            holder.txtDescricao.setText(atividade.getDescricao().substring(0,80)+"...");
        }else{
            holder.txtDescricao.setText(atividade.getDescricao());
        }
        holder.txtTitulo.setText(atividade.getTitulo());
        Uri uri = Uri.parse(atividade.getUser().getPhoto());
        holder.fotoUser.setImageURI(uri);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, Detalhes.class);
                it.putExtra("atividade", (Serializable) atividade);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void addItem(Atividade atividade, int position){
        mList.add(atividade);
        notifyItemInserted(position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView fotoUser;
        public TextView txtTitulo;
        public TextView txtDescricao;
        public TextView txtNome;
        public View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            fotoUser = (SimpleDraweeView) itemView.findViewById(R.id.fotoUser);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            view = itemView;
        }
    }
}
