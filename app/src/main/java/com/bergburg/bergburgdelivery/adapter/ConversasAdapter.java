package com.bergburg.bergburgdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Conversas;
import com.bergburg.bergburgdelivery.viewholder.ConversasViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasViewHolder> {
    private Context context;
    private List<Conversas> conversas = new ArrayList<>();
    private OnListenerAcao<Conversas> onListenerAcao;
    private DadosPreferences preferences;

    public ConversasAdapter(Context context) {
        this.context = context;
        preferences = new DadosPreferences(context);
    }

    @NonNull
    @Override
    public ConversasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = null;

             layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversa,parent,false);

        return new ConversasViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversasViewHolder holder, int position) {
        holder.bind(conversas.get(position), onListenerAcao);

    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }



    public void attackConversas(List<Conversas> conversas){
        this.conversas = conversas;
        notifyDataSetChanged();

    }

    public void limpar(){
        this.conversas.clear();
       // notifyDataSetChanged();
    }
    public void attackListener(OnListenerAcao<Conversas> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }
}
