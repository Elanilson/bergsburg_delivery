package com.bergburg.bergburgdelivery.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bumptech.glide.Glide;

public class ProdutosViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textViewTitulo, textViewDescricao, textViewPreco;
    private View view;
    private CardView cardViewProduto;

    public ProdutosViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        imageView =  view.findViewById(R.id.imageViewArtesanaisImagem);
        textViewTitulo =  view.findViewById(R.id.textViewArtesanaisTitulo);
        textViewDescricao =  view.findViewById(R.id.textViewArtesanaisDescricao);
        textViewPreco =  view.findViewById(R.id.textViewArtesanaisPreco);
        cardViewProduto =  view.findViewById(R.id.cardViewProduto);
    }

    public void bind(Produto produto, OnListenerAcao<Produto> onListenerAcao){
        textViewTitulo.setText(produto.getTitulo());
        textViewDescricao.setText(produto.getDescricao());
        textViewPreco.setText("R$ "+String.format("%.2f",produto.getPreco()));
        Glide.with(view.getContext()).load(produto.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(imageView);

        cardViewProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerAcao.onClick(produto);
            }
        });

    }
}
