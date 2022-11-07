package com.bergburg.bergburgdelivery.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ItemPedidoViewAdapter;
import com.bergburg.bergburgdelivery.adapter.PedidoStatusAdapter;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.model.Usuario;

import java.util.List;

public class ViewPedidorViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewNumeroPedido,textViewDataPedido,textViewCep,textViewNumeroCasa,textViewTotal,textViewTaxa_entrega,textViewSubTotal;
    private RecyclerView recyclerViewStatusPedido, recyclerViewItensPedido;
    private Button buttonFechar;
    private LinearLayout layoutTaxa;
    private ProgressBar progressBarUsuario, progressBarStatusPedido;
    private  LinearLayout layoutNome, layoutTelefone,layoutCep, layoutNCasa,layoutEndereco,layoutNPEdi,layout_st_total,layoutSub,layoutTotal;



    private TextView textViewNome,textViewTelefone,textEndereco;

    public ViewPedidorViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewNome = itemView.findViewById(R.id.textViewNomeView);
        textViewTelefone = itemView.findViewById(R.id.textViewTelefoneView);
        textEndereco = itemView.findViewById(R.id.textViewEnderecoView);
        textViewCep = itemView.findViewById(R.id.textViewCepView);
        textViewNumeroCasa = itemView.findViewById(R.id.textViewNumeroCasaView);

        textViewDataPedido = itemView.findViewById(R.id.textViewPedidoDataPedido);
        textViewNumeroPedido = itemView.findViewById(R.id.textViewPedidoNumeroPedido);
        recyclerViewStatusPedido = itemView.findViewById(R.id.recyclerviewViewPedidostatus);
        recyclerViewItensPedido = itemView.findViewById(R.id.recyclerviewViewPedidoProdutos);
        textViewTotal = itemView.findViewById(R.id.textViewTotalView);
        layoutTaxa = itemView.findViewById(R.id.layout_taxaEntrega);
        textViewTaxa_entrega = itemView.findViewById(R.id.textViewTaxa_entregaView);
        textViewSubTotal = itemView.findViewById(R.id.textViewSubTotalView);

        progressBarUsuario = itemView.findViewById(R.id.progressBarDadosUsuario);
        progressBarStatusPedido = itemView.findViewById(R.id.progressBarSatusPedido);

        layoutNome = itemView.findViewById(R.id.layout_nome);
        layoutTelefone = itemView.findViewById(R.id.layout_telefone);
        layoutCep = itemView.findViewById(R.id.layout_cep);
        layoutNCasa = itemView.findViewById(R.id.layout_N_Casa);
        layoutEndereco = itemView.findViewById(R.id.layout_End);
        layoutNPEdi = itemView.findViewById(R.id.layout_N_Ped);
        layout_st_total = itemView.findViewById(R.id.layout_st_total);
        layoutSub = itemView.findViewById(R.id.layout_sub);
        layoutTotal = itemView.findViewById(R.id.layout_total);

        //buttonFechar = itemView.findViewById(R.id.buttonViewPedidoFechar);
    }

    public void bind(Pedido pedido, PedidoStatusAdapter pedidoStatusAdapter, ItemPedidoViewAdapter itemPedidoViewAdapter, OnListenerAcao<Pedido> onListenerAcao, Context context, Usuario usuario, Endereco endereco){

        if(pedido != null){
            layoutNome.setVisibility(View.VISIBLE);
            layoutTelefone.setVisibility(View.VISIBLE);
            layoutCep.setVisibility(View.VISIBLE);
            layoutNCasa.setVisibility(View.VISIBLE);
            layoutEndereco.setVisibility(View.VISIBLE);
            textEndereco.setVisibility(View.VISIBLE);
            progressBarStatusPedido.setVisibility(View.GONE);
        }else{
            layoutNome.setVisibility(View.GONE);
            layoutTelefone.setVisibility(View.GONE);
            layoutCep.setVisibility(View.GONE);
            layoutNCasa.setVisibility(View.GONE);
            layoutEndereco.setVisibility(View.GONE);
            textEndereco.setVisibility(View.GONE);
            progressBarStatusPedido.setVisibility(View.VISIBLE);
        }

        textViewDataPedido.setText(pedido.getData_pedido());
        textViewNumeroPedido.setText("Pedido n° "+pedido.getId());
        textViewTotal.setText("R$ "+String.format("%.2f",pedido.getTotal()));
        textViewSubTotal.setText("R$ "+String.format("%.2f",pedido.getSubtotal()));
        System.out.println("Pedido "+pedido.toString());

        if(pedido.getTaxa_entrega() != null && pedido.getTaxa_entrega() != 0){
            layoutTaxa.setVisibility(View.VISIBLE);
            textViewTaxa_entrega.setText("R$ "+String.format("%.2f",pedido.getTaxa_entrega()));
        }




        recyclerViewStatusPedido.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewStatusPedido.setAdapter(pedidoStatusAdapter);

        recyclerViewItensPedido.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewItensPedido.setAdapter(itemPedidoViewAdapter);


        if(usuario!= null && endereco != null){
            progressBarUsuario.setVisibility(View.GONE);
            layoutNPEdi.setVisibility(View.VISIBLE);
            layout_st_total.setVisibility(View.VISIBLE);
            layoutSub.setVisibility(View.VISIBLE);
            layoutTotal.setVisibility(View.VISIBLE);
            recyclerViewStatusPedido.setVisibility(View.VISIBLE);



            textViewNome.setText(usuario.getNome());
            textViewTelefone.setText(usuario.getTelefone());
            textEndereco.setText(
                    endereco.getRua()+","+
                    endereco.getNumeroCasa()+", "+
                    endereco.getBairro()+", "+
                    endereco.getCidade()+", Complemento: "+
                    endereco.getComplemento()
            );
            textViewCep.setText(endereco.getCep());
            textViewNumeroCasa.setText(""+endereco.getNumeroCasa());
        }else{
            layoutNPEdi.setVisibility(View.GONE);
            layout_st_total.setVisibility(View.GONE);
            layoutSub.setVisibility(View.GONE);
            layoutTotal.setVisibility(View.GONE);
            recyclerViewStatusPedido.setVisibility(View.GONE);
            progressBarUsuario.setVisibility(View.VISIBLE);
        }





    }

}
