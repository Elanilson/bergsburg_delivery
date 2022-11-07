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
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bumptech.glide.Glide;

public class PedidoStatusViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewStatus,textViewData;

    public PedidoStatusViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewStatus = itemView.findViewById(R.id.textViewItemStatus);
        textViewData = itemView.findViewById(R.id.textViewHItemStatusData);
    }

    public  void bind(){};
    public void bind(Status_pedido status, OnListenerAcao<Status_pedido> onListenerAcao){
        textViewStatus.setText(status.getStatus());
        textViewData.setText(status.getData_create());
    }

}
