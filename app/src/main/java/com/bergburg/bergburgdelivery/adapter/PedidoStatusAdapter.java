package com.bergburg.bergburgdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.viewholder.PedidoStatusViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PedidoStatusAdapter extends RecyclerView.Adapter<PedidoStatusViewHolder> {
    private List<Status_pedido> status = new ArrayList<>();
    private OnListenerAcao<Status_pedido> onListenerAcao;
    @NonNull
    @Override
    public PedidoStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_status,parent,false);
        return new PedidoStatusViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoStatusViewHolder holder, int position) {
        holder.bind(status.get(position),onListenerAcao );

    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public  void attackStatusPedidos(List<Status_pedido> status){
        this.status = status;
        notifyDataSetChanged();
    }
    public  void attackListener(OnListenerAcao<Status_pedido> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }
}
