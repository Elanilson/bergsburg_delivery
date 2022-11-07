package com.bergburg.bergburgdelivery.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bumptech.glide.Glide;

public class ViewItemPedioViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView textViewTitulo,textViewPreco,textViewQuantidade,textViewDescricao,textViewObservacao;


    public ViewItemPedioViewHolder(@NonNull View itemView) {
        super(itemView);


         textViewTitulo = itemView.findViewById(R.id.textViewItemSacolaTituloView);
        textViewDescricao = itemView.findViewById(R.id.textViewTituloDescricaoProduto);
         imageView = itemView.findViewById(R.id.imageViewItemSacolaImagem);
         textViewPreco = itemView.findViewById(R.id.textViewPrecoItemView);
         textViewQuantidade = itemView.findViewById(R.id.textViewItemaQuantidadeView);
        textViewObservacao = itemView.findViewById(R.id.textView29ObsercaoItemView);




    }

    public  void bind(){};
    public void bind(ItensPedido itensPedido){
        Glide.with(itemView.getContext()).load(itensPedido.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(imageView);
        textViewTitulo.setText(itensPedido.getTitulo());
        textViewDescricao.setText(itensPedido.getTitulo());
        textViewPreco.setText("R$ "+itensPedido.getPreco());
        textViewQuantidade.setText("Quantidade: "+itensPedido.getQuantidade());
        if(!itensPedido.getObservacao().isEmpty()){
            textViewObservacao.setText(itensPedido.getObservacao());
        }



    }

}
