package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.bergburg.bergburgdelivery.databinding.ActivityTesteMainBinding;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.viewmodel.TesteMainViewModel;

import java.util.List;

public class TesteMainActivity extends AppCompatActivity {
    private ActivityTesteMainBinding binding;
    private TesteMainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTesteMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        viewModel = new ViewModelProvider(this).get(TesteMainViewModel.class);

        binding.buttonAutenticar.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.autenticar();
        });

        binding.buttonBuscarEventos.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.verificarEvento();
        });
        binding.buttonLimparEvento.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.reconhecerLimparEnventos();
        });

        binding.buttonCriarPedido.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.criarPedidoIfood();
        });

        binding.buttonVerificarFrete.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.verificarFreteIfood();
        });

        observe();
    }

    private void observe() {
        viewModel.freteIfood.observe(this, new Observer<RespostaDisponibilidadeDeEntrega>() {
            @Override
            public void onChanged(RespostaDisponibilidadeDeEntrega respostaDisponibilidadeDeEntrega) {
                System.out.println("apkdoandroid: "+respostaDisponibilidadeDeEntrega.toString());
            }
        });
        viewModel.respostaPedido.observe(this, new Observer<RespostaPedido>() {
            @Override
            public void onChanged(RespostaPedido respostaPedido) {
                System.out.println("apkdoandroid: "+respostaPedido.toString());
            }
        });
        viewModel.autenticacao.observe(this, new Observer<Autenticacao>() {
            @Override
            public void onChanged(Autenticacao autenticacao) {
                System.out.println("apkdoandroid: "+autenticacao.toString());
            }
        });
        viewModel.eventoPedido.observe(this, new Observer<List<EventoPedido>>() {
            @Override
            public void onChanged(List<EventoPedido> eventoPedido) {
                if(eventoPedido != null){
                    System.out.println("apkdoandroid: "+ eventoPedido.toString());
                }
            }
        });
        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("apkdoandroid: status "+resposta.getStatus());
                System.out.println("apkdoandroid: "+resposta.getMensagem());
            }
        });
    }
}