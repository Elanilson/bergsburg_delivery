package com.bergburg.bergburgdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.viewholder.CategoriaViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaViewHolder> {
    private List<Categoria> categorias = new ArrayList<>();
    private Context context;
    private OnListenerAcao<Categoria> listenerAcao ;

    public CategoriaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categoria,parent,false);
        return new CategoriaViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        holder.bind(categorias.get(position), context,listenerAcao);

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public void attackCategorias(List<Categoria> categorias){
        this.categorias = categorias;
        notifyDataSetChanged();
    }
    public void attackListener(OnListenerAcao<Categoria> listenerAcao){
        this.listenerAcao = listenerAcao;
    }
}
