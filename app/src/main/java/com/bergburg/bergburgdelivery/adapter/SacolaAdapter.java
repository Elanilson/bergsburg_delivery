package com.bergburg.bergburgdelivery.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.viewholder.CategoriaViewHolder;
import com.bergburg.bergburgdelivery.viewholder.SacolaViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SacolaAdapter extends RecyclerView.Adapter<SacolaViewHolder> {
    private List<ItensSacola> itensSacola = new ArrayList<>();
    private OnListenerAcao<ItensSacola> onListenerAcao;
    private Context context;

    public SacolaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SacolaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_sacola,parent,false);
        return new SacolaViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SacolaViewHolder holder, int position) {
        holder.bind(itensSacola.get(position),onListenerAcao,context);

    }

    @Override
    public int getItemCount() {
        return itensSacola.size();
    }

    public void attackProdutos(List<ItensSacola> itensSacola){
        if(itensSacola.size() != 0){
          //  limpar();
            this.itensSacola = itensSacola;
            notifyDataSetChanged();
        }
    }
    public void limpar(){
        this.itensSacola.removeAll(itensSacola);
        this.itensSacola.clear();
        notifyDataSetChanged();
    }
    public void attackListener(OnListenerAcao<ItensSacola> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }

}
