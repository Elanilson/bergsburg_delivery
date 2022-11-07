package com.bergburg.bergburgdelivery.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Conversas;
import com.bergburg.bergburgdelivery.model.Mensagem;

public class ConversasViewHolder extends RecyclerView.ViewHolder {

    TextView textNome,textViewDataa;
    CardView cardView;
    public ConversasViewHolder(@NonNull View itemView) {
        super(itemView);
        textNome = itemView.findViewById(R.id.textViewNomeUsuario);
        textViewDataa = itemView.findViewById(R.id.textViewDataConversa);
        cardView = itemView.findViewById(R.id.cardConversa);

    }


    public void bind(Conversas conversa, OnListenerAcao<Conversas> onListenerAcao){

        textNome.setText(conversa.getNome());
        textViewDataa.setText(conversa.getData_create());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerAcao.onClick(conversa);
            }
        });


    }
}
