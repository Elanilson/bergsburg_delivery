package com.bergburg.bergburgdelivery.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;

public class IFoodViewHolder extends RecyclerView.ViewHolder {

    private Button btnCriarPedido,btnConfirmarPedido,btnAceitarCancelamento,btnNegarCancelamento,btnVerificarFrete,btnCancelarPedido;

    private TextView idPedido, statusPedido, dataPedido,idEntregador,nomeEntregador,telefoneEntregador,veiculoEntregador;
    private LinearLayout layoutEntregador;

    public IFoodViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(EventoPedido eventoPedido){

        idPedido = itemView.findViewById(R.id.textViewIDPedidoIfood);
        statusPedido = itemView.findViewById(R.id.textViewStatusPedidoIfood);
        dataPedido = itemView.findViewById(R.id.textViewDataPedidoIfood);
        idEntregador = itemView.findViewById(R.id.textViewIdEntregadorIfood);
        nomeEntregador = itemView.findViewById(R.id.textViewNomeEntregadorIfood);
        telefoneEntregador = itemView.findViewById(R.id.textViewTElefoneEntregador);
        veiculoEntregador = itemView.findViewById(R.id.textViewVeiculoEntregador);
        layoutEntregador = itemView.findViewById(R.id.linearLayoutEntregador);

        idPedido.setText(eventoPedido.getIdDoPedido());
        statusPedido.setText(eventoPedido.getCodigoCompleto());
        dataPedido.setText(eventoPedido.getDataDeCriacao());

        if(eventoPedido.getCodigoCompleto().equalsIgnoreCase("PLACED")){
            statusPedido.setText("Novo pedido");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("PREPARATION_STARTED")){
            statusPedido.setText("Preparando");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("CONFIRMED")){
            statusPedido.setText("Confirmado");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("ASSIGN_DRIVER")){
            statusPedido.setText("Entregador selecionado");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("GOING_TO_ORIGIN")){
            statusPedido.setText("Entregador a caminho do restaurente");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("ARRIVED_AT_ORIGIN")){
            statusPedido.setText("O entregador chegou ao restaurante");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("COLLECTED")){
            statusPedido.setText("Coletado");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("DISPATCHED")){
            statusPedido.setText("Despachou");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("ARRIVED_AT_DESTINATION")){
            statusPedido.setText("Chegou ao destino");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("CONCLUDED")){
            statusPedido.setText("Pedido entregue");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.verde));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("CANCELLATION_REQUESTED")){
            statusPedido.setText("Cancelamento solicitado");
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("CANCELLED")){
            statusPedido.setText("Cancelado");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.vermelho));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("CANCELLATION_REQUEST_FAILED")){
            statusPedido.setText("Falha no pedido de cancelamento");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.vermelho));
        }else if (eventoPedido.getCodigoCompleto().equalsIgnoreCase("READY_TO_PICKUP")){
            statusPedido.setText("Pronto para recolher");
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }else{
            statusPedido.setText(eventoPedido.getCodigoCompleto());
            statusPedido.setTextColor(itemView.getResources().getColor(R.color.grey31));
        }

        if(eventoPedido.getMetadata() != null){
            if(eventoPedido.getMetadata().getIdEntregador() != null){
                layoutEntregador.setVisibility(View.VISIBLE);
                idEntregador.setText(eventoPedido.getMetadata().getIdEntregador());
                nomeEntregador.setText(eventoPedido.getMetadata().getNomeEntregador());
                telefoneEntregador.setText(eventoPedido.getMetadata().getTelefoneEntregador());
                veiculoEntregador.setText(eventoPedido.getMetadata().getVeiiculoEntregador());
            }
        }else{

            layoutEntregador.setVisibility(View.GONE);
        }

       /* btnCriarPedido = itemView.findViewById(R.id.buttonCriarPedidoIfood);
        btnConfirmarPedido = itemView.findViewById(R.id.buttonConfirmarPedidoIfood);
        btnCancelarPedido = itemView.findViewById(R.id.buttonCancelarPedido);
        btnAceitarCancelamento = itemView.findViewById(R.id.buttonAceitarCancelamento);
        btnNegarCancelamento = itemView.findViewById(R.id.buttonNegaCAncelamento);
        btnVerificarFrete = itemView.findViewById(R.id.buttonVerificarFrete);*/

    }
}
