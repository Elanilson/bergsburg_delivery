package com.bergburg.bergburgdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewholder.PedidorViewHolder;
import com.bergburg.bergburgdelivery.viewholder.ViewPedidorViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ViewPedidoAdapter extends RecyclerView.Adapter<ViewPedidorViewHolder> {
    private Pedido pedido = new Pedido();
    private Endereco endereco = new Endereco();
    private List<ItensPedido> itensPedido = new ArrayList<>();
    private List<Status_pedido> status_pedido = new ArrayList<>();
    private Usuario usuario;
    private OnListenerAcao<Pedido> onListenerAcao;
    private PedidoStatusAdapter pedidoStatusAdapter = new PedidoStatusAdapter();
    private ItemPedidoViewAdapter itemPedidoViewAdapter = new ItemPedidoViewAdapter();
    private Context context;


    public ViewPedidoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewPedidorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exibir_pedido,parent,false);
        return new ViewPedidorViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPedidorViewHolder holder, int position) {
        holder.bind(pedido,  pedidoStatusAdapter, itemPedidoViewAdapter,onListenerAcao,context,usuario,endereco );

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public  void attackPedido(Pedido pedido){
        this.pedido = pedido;
        notifyDataSetChanged();
    }
    public void attackEnderede(Endereco endereco){
        this.endereco = endereco;
    }
    public  void attackItensPedidos(List<ItensPedido> itensPedido){
        this.itensPedido = itensPedido;
        itemPedidoViewAdapter.attackItens(itensPedido);
        System.out.println("ItensPedido total: "+itensPedido.size());
        notifyDataSetChanged();
    }
    public  void attackstatusPedido(List<Status_pedido> status_pedido){
        this.status_pedido = status_pedido;
        pedidoStatusAdapter.attackStatusPedidos(status_pedido);
        notifyDataSetChanged();
    }

    public  void attackUsuario(Usuario usuario){
        this.usuario = usuario;
        notifyDataSetChanged();
    }
    public  void attackListener(OnListenerAcao<Pedido> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }


}
