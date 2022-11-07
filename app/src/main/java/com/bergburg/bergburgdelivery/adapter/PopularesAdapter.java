package com.bergburg.bergburgdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.viewholder.PopularesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PopularesAdapter extends RecyclerView.Adapter<PopularesViewHolder> {
    private List<Produto> produtos = new ArrayList<>();
    private OnListenerAcao<Produto> listenerAcao;
    @NonNull
    @Override
    public PopularesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_artesanais,parent,false);
        return new PopularesViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularesViewHolder holder, int position) {
        holder.bind(produtos.get(position),listenerAcao);

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public  void attackProdutos(List<Produto> produtos){
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    public void attackListener(OnListenerAcao<Produto> onListenerAcao){
        this.listenerAcao = onListenerAcao;
    }
}
