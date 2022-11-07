package com.bergburg.bergburgdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.viewholder.SacolaViewHolder;
import com.bergburg.bergburgdelivery.viewholder.ViewItemPedioViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoViewAdapter extends RecyclerView.Adapter<ViewItemPedioViewHolder> {
    private List<ItensPedido> itensPedido = new ArrayList<>();
    private OnListenerAcao<ItensPedido> onListenerAcao;




    @NonNull
    @Override
    public ViewItemPedioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_item_pedido,parent,false);
        return new ViewItemPedioViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewItemPedioViewHolder holder, int position) {
        holder.bind(itensPedido.get(position));

    }

    @Override
    public int getItemCount() {
        return itensPedido.size();
    }

    public void attackItens(List<ItensPedido> itensPedido){
        if(itensPedido.size() != 0){
            limpar();
            this.itensPedido = itensPedido;
            notifyDataSetChanged();
        }
    }
    public void limpar(){
        this.itensPedido.clear();
       // notifyDataSetChanged();
    }
    public void attackListener(OnListenerAcao<ItensPedido> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }
}
