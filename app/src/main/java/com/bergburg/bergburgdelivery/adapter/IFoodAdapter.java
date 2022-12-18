package com.bergburg.bergburgdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.viewholder.IFoodViewHolder;

import java.util.ArrayList;
import java.util.List;

public class IFoodAdapter extends RecyclerView.Adapter<IFoodViewHolder> {
    private List<EventoPedido> eventos = new ArrayList<>();

    @NonNull
    @Override
    public IFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_evento_pedido,parent,false);

        return new IFoodViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull IFoodViewHolder holder, int position) {
        holder.bind(eventos.get(position));

    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public void attackEventos(List<EventoPedido> eventos){
        this.eventos = eventos;
        notifyDataSetChanged();

    }

    public void limparDado(){
        eventos.clear();
        notifyDataSetChanged();
    }
}
