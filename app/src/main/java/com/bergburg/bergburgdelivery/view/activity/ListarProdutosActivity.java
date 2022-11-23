package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bergburg.bergburgdelivery.Constantes.Constantes;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ProdutdoAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityListarProdutosBinding;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.ListarProdutosViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListarProdutosActivity extends AppCompatActivity {
    private ActivityListarProdutosBinding binding;
    private ProdutdoAdapter produtdoAdapter = new ProdutdoAdapter();
    private ListarProdutosViewModel viewModel;
    private Long idCategoria = 0L;
    private  Dialog dialogInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListarProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ListarProdutosViewModel.class);

        LinearLayoutManager manager3 = new LinearLayoutManager(binding.getRoot().getContext());
        manager3.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewListarItens.setLayoutManager(manager3);
        binding.recyclerviewListarItens.setAdapter(produtdoAdapter);

        OnListenerAcao<Produto> listener = new OnListenerAcao<Produto>() {
            @Override
            public void onClick(Produto obj) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_PRODUTO,obj.getId());
                Intent intent = new Intent(ListarProdutosActivity.this, ExibirProdutoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Produto obj) {

            }

            @Override
            public void onClickObservacao(Produto obj) {

            }
        };

        produtdoAdapter.attackListener(listener);

        observer();
    }

    private void recuperar(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idCategoria = bundle.getLong(Constantes.ID_CATEGORIA);
        }
    }

    private void observer() {
        viewModel.produtos.observe(this, new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> produtos) {
                if(produtos != null){
                    if(produtos.size() > 0){
                        binding.progressBarProdutos.setVisibility(View.GONE);
                         produtdoAdapter.attackProdutos(produtos);
                    }else{
                        binding.progressBarProdutos.setVisibility(View.VISIBLE);
                    }
                }else{
                    binding.progressBarProdutos.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private  void snackbar(String mensagem){
        Snackbar.make(binding.LinearLayoutListaPro, mensagem, Snackbar.LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        recuperar();
        viewModel.listarProdutosPorCategoria(idCategoria);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
        RetrofitClient.CancelarRequisicoes();


    }
}