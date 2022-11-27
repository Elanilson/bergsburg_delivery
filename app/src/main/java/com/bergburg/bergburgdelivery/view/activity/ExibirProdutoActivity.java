package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityExibirProdutoBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.ExibirProdutoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class ExibirProdutoActivity extends AppCompatActivity {
    private ActivityExibirProdutoBinding binding;
    private ExibirProdutoViewModel viewModel;
    private Long idProduto = 0L;
    private Long idSacola;
    private int quantidade = 1;
    private String observacao = "";
    private DadosPreferences preferences;
    private ImageView imageViewProduto;
    private Produto produtoAtual = new Produto();
    private  Dialog dialogInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExibirProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(ExibirProdutoViewModel.class);
        preferences = new DadosPreferences(getApplicationContext());
        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imageButtonItemSacolaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantidade <= 9){ // limite 10
                    quantidade++;
                }else{
                    Toast.makeText(ExibirProdutoActivity.this, getString(R.string.voce_atingiu), Toast.LENGTH_SHORT).show();
                }
                    binding.textViewItemSacolaQuantidade.setText(""+quantidade);
            }
        });
        binding.imageButtonITemSacolaMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantidade > 1){
                    quantidade--;
                }
                    binding.textViewItemSacolaQuantidade.setText(""+quantidade);
            }
        });

        String statusLogado = preferences.recuperarStatus();
        //se nao tiver logado troca o nome do button e direcionar pro login
        System.out.println("IdSacola "+idSacola);
        System.out.println("status "+statusLogado);
        if(idSacola == null && statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.logado))  ){
           binding.texAdicionar.setText(getString(R.string.entrarOuCadastrar));
        }
            binding.layoutAdicionarProdutoSacola.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 if(idSacola != null && !statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado)) ){
                    observacao = binding.editTextObservacaoProduto.getText().toString();
                    String quantidadeString = binding.textViewItemSacolaQuantidade.getText().toString().trim();
                    if(quantidadeString != null && !quantidadeString.isEmpty() ){
                        quantidade = Integer.parseInt(quantidadeString);
                    }
                    //    Toast.makeText(ExibirProdutoActivity.this, "Cliclandoo idSacola: "+idSacola+" - idProduto: "+idProduto, Toast.LENGTH_SHORT).show();
                    binding.progressBarEnvioProduto.setVisibility(View.VISIBLE);
                    viewModel.adicionarItemSacola(idSacola,idProduto, produtoAtual.getPreco(), quantidade,observacao);
                    }else{
                     startActivity(new Intent(ExibirProdutoActivity.this,LoginActivity.class));
                     finish();
                     //   Toast.makeText(ExibirProdutoActivity.this, getString(R.string.faca_login), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        observe();
    }

    private void observe() {
        viewModel.produto.observe(this, new Observer<Produto>() {
            @Override
            public void onChanged(Produto produto) {
                if(produto != null){
                    binding.progressBarExebirPRoduto.setVisibility(View.GONE);
                    produtoAtual = produto;
                    imageViewProduto = binding.imageViewProduto;
                    binding.textViewTituloProduto.setText(produto.getTitulo());
                    binding.textViewDescricaoProduto.setText(produto.getDescricao());
                    binding.textViewPrecoProduto.setText("R$ "+String.format("%.2f",produto.getPreco()));
                    Glide.with(getApplicationContext()).load(produto.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(imageViewProduto);
                }
            }
        });
        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    binding.progressBarEnvioProduto.setVisibility(View.GONE);
                    binding.progressBarExebirPRoduto.setVisibility(View.GONE);
                  //  Toast.makeText(ExibirProdutoActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    binding.progressBarExebirPRoduto.setVisibility(View.GONE);
                    binding.progressBarEnvioProduto.setVisibility(View.GONE);
                    Toast.makeText(ExibirProdutoActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    private void recuperar(){
        //recupero id da sacola do usuário
        idSacola = preferences.recuperarIDSacola();
        System.out.println("Id_sacola recuperado: "+idSacola);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idProduto = bundle.getLong(Constantes.ID_PRODUTO);
            viewModel.buscarProduto(idProduto);
        }

    }
    private  void snackbar(String mensagem){
        Snackbar.make(binding.LinearLayoutExibir, mensagem, Snackbar.LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        recuperar();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RetrofitClient.CancelarRequisicoes();
    }

    @Override
    public void onPause() {
        super.onPause();


    }
}