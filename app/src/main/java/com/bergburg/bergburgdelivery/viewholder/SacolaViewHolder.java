package com.bergburg.bergburgdelivery.viewholder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.viewmodel.SacolaViewModel;
import com.bumptech.glide.Glide;

public class SacolaViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ImageView imageView;
    private TextView textViewTitulo,textViewPreco,textViewQuantidade,textViewDescricao;
    private ImageButton imageButtonMenos,imageButtonMais;
    private View view;
    private LinearLayout layoutObservacao;
    private int quantidade = 1;
    private ItensSacola itensSacolaLocal = new ItensSacola();
    public SacolaViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
         textViewTitulo = itemView.findViewById(R.id.textViewItemSacolaTitulo);
         textViewDescricao = itemView.findViewById(R.id.textViewDescricaoItem);
         imageView = itemView.findViewById(R.id.imageViewItemSacolaImagem);
         textViewPreco = itemView.findViewById(R.id.textViewItemSacolaPreco);
         textViewQuantidade = itemView.findViewById(R.id.textViewItemSacolaQuantidade);
        imageButtonMenos = itemView.findViewById(R.id.imageButtonITemSacolaMenos);
        imageButtonMais = itemView.findViewById(R.id.imageButtonItemSacolaMais);
        layoutObservacao = itemView.findViewById(R.id.layoutObservacao);



    }

    public  void bind(){};
    public void bind(ItensSacola itensSacola, OnListenerAcao<ItensSacola> onListenerAcao, Context context){
        quantidade = itensSacola.getQuantidade();
        itensSacolaLocal = itensSacola;

        Glide.with(view.getContext()).load(itensSacola.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(imageView);
        textViewTitulo.setText(itensSacola.getTitulo());
        textViewDescricao.setText(itensSacola.getDescricao().replaceAll("\r\n"," "));
        textViewPreco.setText("R$ "+String.format("%.2f",itensSacola.getPreco()));
        textViewQuantidade.setText(String.valueOf(itensSacola.getQuantidade()));

        imageButtonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantidade <= 9){ // limite 10
                    quantidade++;
                    itensSacolaLocal.setQuantidade(quantidade);
                    System.out.println("Quantidade: "+itensSacola.getQuantidade());
                    onListenerAcao.onClick(itensSacolaLocal);
                }else{
                    Toast.makeText(context, context.getString(R.string.voce_atingiu), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButtonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantidade > 0){
                    quantidade--;
                    itensSacolaLocal.setQuantidade(quantidade);
                    onListenerAcao.onClick(itensSacolaLocal);
                }
                System.out.println("quantidade: "+quantidade);
            }
        });

        layoutObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerAcao.onClickObservacao(itensSacola);
            }
        });



    }



    private void dialogRemoverItem(OnListenerAcao<ItensSacola> onListenerAcao,ItensSacola itensSacola){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Remoção");
        alertDialog.setMessage("O item será removido");
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // viewModel.deletarItemDaSacola(itensSacola.getId());
                onListenerAcao.onClick(itensSacola);
                dialog.cancel();

            }
        });
        alertDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}
