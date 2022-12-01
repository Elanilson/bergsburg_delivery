package com.bergburg.bergburgdelivery.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Pedido;

public class PedidorViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewIdPedido,textViewDataPedido,textViewStatus,textViewHorarioStatus,textViewTotal,textViewlabelStatus;
    private CardView cardView;
    private LinearLayout layoutPedido;
    private DadosPreferences preferences;


    public PedidorViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewDataPedido = itemView.findViewById(R.id.textViewDataPedido);
        textViewIdPedido = itemView.findViewById(R.id.textViewNumeroPedido);
        textViewStatus = itemView.findViewById(R.id.textViewStatusPedido);
        textViewHorarioStatus = itemView.findViewById(R.id.textViewHorarioPedido);
        textViewTotal = itemView.findViewById(R.id.textViewTotalPedido);
        cardView = itemView.findViewById(R.id.cardViewPedido);
        layoutPedido = itemView.findViewById(R.id.layoutPedido);
        textViewlabelStatus = itemView.findViewById(R.id.textViewStatusLabel);
        preferences = new DadosPreferences(itemView.getContext());
    }

    public void bind(Pedido pedido, OnListenerAcao<Pedido> onListenerAcao){
        textViewDataPedido.setText(pedido.getData_pedido());
        textViewIdPedido.setText("N° "+pedido.getId());
        textViewStatus.setText(pedido.getStatus());
        textViewHorarioStatus.setText(pedido.getData_status());
        textViewTotal.setText("R$ "+String.format("%.2f",pedido.getTotal()));


        Long idUsuario = preferences.recuperarID();
        if(idUsuario != null){
            if(idUsuario == Constantes.ADMIN){
                if(pedido.getVisualizado().equalsIgnoreCase(Constantes.SIM)){
                    if(pedido.getStatus().equalsIgnoreCase("Cancelado")){
                        layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.cancelado));
                    }else{
                        layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.amarelo_pedido_lido));
                    }
                    //  textViewlabelStatus.setBackgroundColor(itemView.getResources().getColor(R.color.amarelo_pedido_lido));
                }else{
                    // textViewlabelStatus.setBackgroundColor(itemView.getResources().getColor(R.color.verde_Pedido_novo));
                    if(pedido.getStatus().equalsIgnoreCase("Cancelado")){
                        layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.cancelado));
                    }else{
                        layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.verde_Pedido_novo));
                    }

                }
            }else{ // para os usuários
                if(pedido.getStatus().equalsIgnoreCase("Cancelado")){
                    layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.cancelado));
                }else {
                    layoutPedido.setBackgroundColor(itemView.getResources().getColor(R.color.white));
                }
            }
        }



        System.out.println("Holder "+pedido.toString());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerAcao.onClick(pedido);
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onListenerAcao.onLongClick(pedido);
                return false;
            }
        });


    }
}
