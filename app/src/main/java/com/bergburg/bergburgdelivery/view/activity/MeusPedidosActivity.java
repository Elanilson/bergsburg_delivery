package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.PedidoAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityMeusPedidosBinding;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.PedidosViewModel;

import java.util.List;

public class MeusPedidosActivity extends AppCompatActivity {
    private ActivityMeusPedidosBinding binding;
    private PedidoAdapter pedidoAdapter = new PedidoAdapter();
    private PedidosViewModel viewModel;
    private Long idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeusPedidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarPersonalizadaMeus.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        viewModel = new  ViewModelProvider(this).get(PedidosViewModel.class);

        //recupero o id enviado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idUsuario = bundle.getLong(Constantes.ID_USUARIO);
            viewModel.listarPedido(idUsuario);
        }

        binding.recyclerViewMeusPedidos.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerViewMeusPedidos.setAdapter(pedidoAdapter);

        OnListenerAcao<Pedido> onListenerAcao = new OnListenerAcao<Pedido>() {
            @Override
            public void onClick(Pedido obj) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_PEDIDO, obj.getId());
                Intent intent = new Intent(MeusPedidosActivity.this, ExibirPedidoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Pedido obj) {

            }

            @Override
            public void onClickObservacao(Pedido obj) {

            }
        };

        pedidoAdapter.attackListener(onListenerAcao);

        observe();
    }

    private void observe() {
        viewModel.pedidos.observe(this, new Observer<List<Pedido>>() {
            @Override
            public void onChanged(List<Pedido> pedidos) {
                try {
                    if(pedidos != null){
                        if(pedidos.size() > 0){
                            binding.textViewSemPedidos.setVisibility(View.GONE);
                            pedidoAdapter.attackPedidos(pedidos);
                        }else{

                            binding.textViewSemPedidos.setVisibility(View.VISIBLE);
                        }
                    }else{
                            binding.textViewSemPedidos.setVisibility(View.VISIBLE);

                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RetrofitClient.CancelarRequisicoes();
    }
}