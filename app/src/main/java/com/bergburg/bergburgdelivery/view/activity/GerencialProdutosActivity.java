package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ProdutdoAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityGerencialProdutosBinding;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.GerencialProdutosViewModel;

import java.util.List;

public class GerencialProdutosActivity extends AppCompatActivity {
    private ActivityGerencialProdutosBinding binding;
    private GerencialProdutosViewModel viewModel;
    private ProdutdoAdapter produtdoAdapter = new ProdutdoAdapter();
    private int tentativa = 5;
    private int contador_de_tentativa = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGerencialProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarPersonalizadaGerecial.textViewTituloToolbar.setText(getString(R.string.gerencial_produtos));
        binding.toolbarPersonalizadaGerecial.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(GerencialProdutosViewModel.class);

        LinearLayoutManager manager3 = new LinearLayoutManager(binding.getRoot().getContext());
        manager3.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerViewGerencialProdutos.setLayoutManager(manager3);
        binding.recyclerViewGerencialProdutos.setAdapter(produtdoAdapter);

        OnListenerAcao<Produto> listener = new OnListenerAcao<Produto>() {
            @Override
            public void onClick(Produto obj) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_PRODUTO,obj.getId());
                Intent intent = new Intent(GerencialProdutosActivity.this, EditarProdutoActivity.class);
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

        binding.editTextPesquisaProdutos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.pesquisarProduto(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        produtdoAdapter.attackListener(listener);

        observe();
    }

    private void observe() {
        viewModel.produtos.observe(this, new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> produtos) {
              if(produtos != null){
                  if(produtos.size() > 0){
                      binding.textViewSemprodutos.setVisibility(View.GONE);
                      produtdoAdapter.attackProdutos(produtos);
                  }else{
                      binding.textViewSemprodutos.setVisibility(View.VISIBLE);
                      if(  contador_de_tentativa <= tentativa){
                         viewModel.getProdutos();
                          System.out.println("Tentativas de buscas: "+contador_de_tentativa);
                      }
                  }
              }
                contador_de_tentativa++;
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(!resposta.getStatus()){
                    Toast.makeText(GerencialProdutosActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getProdutos();
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