package com.bergburg.bergburgdelivery.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Pedido;

public class PedidorViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewIdPedido,textViewDataPedido,textViewStatus,textViewHorarioStatus,textViewTotal;
    private CardView cardView;


    public PedidorViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewDataPedido = itemView.findViewById(R.id.textViewDataPedido);
        textViewIdPedido = itemView.findViewById(R.id.textViewNumeroPedido);
        textViewStatus = itemView.findViewById(R.id.textViewStatusPedido);
        textViewHorarioStatus = itemView.findViewById(R.id.textViewHorarioPedido);
        textViewTotal = itemView.findViewById(R.id.textViewTotalPedido);
        cardView = itemView.findViewById(R.id.cardViewPedido);
    }

    public void bind(Pedido pedido, OnListenerAcao<Pedido> onListenerAcao){
        textViewDataPedido.setText(pedido.getData_pedido());
        textViewIdPedido.setText("N° "+pedido.getId());
        textViewStatus.setText(pedido.getStatus());
        textViewHorarioStatus.setText(pedido.getData_status());
        textViewTotal.setText("R$ "+String.format("%.2f",pedido.getTotal()));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerAcao.onClick(pedido);
            }
        });


    }
}
