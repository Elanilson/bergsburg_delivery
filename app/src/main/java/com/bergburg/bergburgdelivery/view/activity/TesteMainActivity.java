package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityTesteMainBinding;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.PedidoResposta;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
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
            viewModel.verificarEvento();
        });

        observe();
    }

    private void observe() {
        viewModel.autenticacao.observe(this, new Observer<Autenticacao>() {
            @Override
            public void onChanged(Autenticacao autenticacao) {
                System.out.println("apkdoandroid: "+autenticacao.toString());
            }
        });
        viewModel.pedidoResposta.observe(this, new Observer<List<PedidoResposta>>() {
            @Override
            public void onChanged(List<PedidoResposta> pedidoResposta) {
                System.out.println("apkdoandroid: "+pedidoResposta.toString());
            }
        });
        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("apkdoandroid: "+resposta.getMensagem());
            }
        });
    }
}