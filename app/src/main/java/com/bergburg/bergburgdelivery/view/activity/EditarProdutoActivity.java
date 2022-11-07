package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityEditarProdutoBinding;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.viewmodel.EditarProdutoViewModel;
import com.bumptech.glide.Glide;

public class EditarProdutoActivity extends AppCompatActivity {
    private ActivityEditarProdutoBinding binding;
    private EditarProdutoViewModel viewModel;
    private Produto produtoAtual = new Produto();
    private Long idProduto;
    private ImageView imageViewProduto;
    private String produtoAtivado = "";
    private Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(EditarProdutoViewModel.class);
        imageViewProduto = binding.imageViewProduto;
        //recupero o id enviado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idProduto = bundle.getLong(Constantes.ID_PRODUTO);
            viewModel.buscarProduto(idProduto);
        }

         aSwitch = binding.switchProduto;

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    produtoAtivado = "SIM";
                    aSwitch.setText(getString(R.string.produto_ativo));
                    System.out.println("ativado");
                }else{
                    produtoAtivado = "NAO";
                    aSwitch.setText(getString(R.string.produto_desativado));
                    System.out.println("desativado");
                }
            }
        });

        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = binding.editNomeProduto.getText().toString();
                String descricao = binding.editDescricaoProduto.getText().toString();
              //  String urlImagem = binding.editNomeImagemProduto.getText().toString();
                String preco = binding.editPrecoPRoduto.getText().toString();

                viewModel.atualizarProduto(idProduto,nome,descricao,preco,produtoAtivado);
            }
        });

        observe();
    }

    private void observe() {
        viewModel.produto.observe(this, new Observer<Produto>() {
            @Override
            public void onChanged(Produto produto) {
                produtoAtual = produto;
                if(produto != null) {
                    if(produto.getAtivo().equalsIgnoreCase("SIM")){
                        aSwitch.setChecked(true);
                        aSwitch.setText(getString(R.string.produto_ativo));
                        produtoAtivado = "SIM";
                    }else{
                        produtoAtivado = "NAO";
                        aSwitch.setChecked(false);
                        aSwitch.setText(getString(R.string.produto_desativado));
                    }

                    binding.editNomeProduto.setText(produto.getTitulo());
                    binding.editPrecoPRoduto.setText("" + produto.getPreco());
                    binding.editNomeImagemProduto.setText(produto.getUrlImagem());
                    binding.editDescricaoProduto.setText(produto.getDescricao());
                    Glide.with(getApplicationContext()).load(produto.getUrlImagem()).placeholder(R.drawable.imagem_padrao).into(binding.imageViewProduto);
                }
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {

                if(resposta.getStatus()){
                    Toast.makeText(EditarProdutoActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditarProdutoActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}