package com.bergburg.bergburgdelivery.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MensagensViewHolder extends RecyclerView.ViewHolder {

    TextView textMensagemTexto,textViewDataa;
    public MensagensViewHolder(@NonNull View itemView) {
        super(itemView);
        textMensagemTexto = itemView.findViewById(R.id.textMensagemTexto);
        textViewDataa = itemView.findViewById(R.id.textViewData);

    }


    public void bind(Mensagem mensagem){
        String texto = mensagem.getTexto();
        texto = texto.trim();
        System.out.println("Texto-"+texto+"-fim");
        textMensagemTexto.setText(texto);
        textViewDataa.setText(mensagem.getData_create());


    }
}
