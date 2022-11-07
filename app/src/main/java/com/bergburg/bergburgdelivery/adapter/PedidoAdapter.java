package com.bergburg.bergburgdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.viewholder.CategoriaViewHolder;
import com.bergburg.bergburgdelivery.viewholder.PedidorViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidorViewHolder> {
    private List<Pedido> pedidos = new ArrayList<>();
    private OnListenerAcao<Pedido> onListenerAcao;
    @NonNull
    @Override
    public PedidorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pedido,parent,false);
        return new PedidorViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidorViewHolder holder, int position) {
        holder.bind(pedidos.get(position),onListenerAcao );

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public  void attackPedidos(List<Pedido> pedidos){
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }
    public  void attackListener(OnListenerAcao<Pedido> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }
}
