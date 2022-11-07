package com.bergburg.bergburgdelivery.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.view.activity.ListarProdutosActivity;
import com.bumptech.glide.Glide;

public class CategoriaViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    ImageView imageView;
    TextView textViewTitulo;
    View view;
    public CategoriaViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
         cardView = itemView.findViewById(R.id.cardviewCategoria);
         imageView = itemView.findViewById(R.id.imageViewCategoria);
         textViewTitulo = itemView.findViewById(R.id.textViewTituloCategoria);

    }

    public  void bind(){};
    public void bind(Categoria categoria, Context context, OnListenerAcao<Categoria> listener){

        textViewTitulo.setText(categoria.getTitulo());
        Glide.with(view.getContext()).load(categoria.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.onClick(categoria);
            }
        });

    }
}
