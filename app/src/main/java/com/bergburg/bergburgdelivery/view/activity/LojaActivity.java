package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityLojaBinding;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.LojaViewModel;

public class LojaActivity extends AppCompatActivity {
    private ActivityLojaBinding binding;
    private LojaViewModel viewModel;
    private Long idLoja;
    private  Switch  aSwitchAbrir_FecharLoja;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarPersonalizada.textViewTituloToolbar.setText(getString(R.string.gerencial_produtos));
        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(LojaViewModel.class);

        aSwitchAbrir_FecharLoja = binding.switchAbrirFecharLoja;

        aSwitchAbrir_FecharLoja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_ABERTA);
                }else{
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_FECHADO);
                }
            }
        });

        binding.buttonSalvarDados.setOnClickListener(v -> atualizar());

        observe();
    }

    private void atualizar(){
        binding.progressBarLoja.setVisibility(View.VISIBLE);
        String nome = binding.editTextNomeEmpresa.getText().toString();
        String ramo = binding.editTextRamoEmpresa.getText().toString();
        String endereco = binding.editTextEnderecoEmpresa.getText().toString();
        String tempoEntrega = binding.editTextTempoEntregaEmpresa.getText().toString();
        String valor_minimo = binding.editTextValorMinimoEmpresa.getText().toString();
        String telefone = binding.editTextTelefoneEmpresa.getText().toString();
         status = binding.switchAbrirFecharLoja.isChecked() == true ? "Aberto" : "Fechado";

        viewModel.atualizarLoja(idLoja,nome,status,ramo,endereco,tempoEntrega,valor_minimo,telefone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getEstabelicimento();
    }

    private void observe() {

        viewModel.estabelicimento.observe(this, new Observer<Estabelicimento>() {
            @Override
            public void onChanged(Estabelicimento estabelicimento) {
                binding.editTextNomeEmpresa.setText(estabelicimento.getNome());
                binding.editTextRamoEmpresa.setText(estabelicimento.getRamo());
                binding.editTextEnderecoEmpresa.setText(estabelicimento.getEndereco());
                binding.editTextTempoEntregaEmpresa.setText(estabelicimento.getTempoEntrega());
                binding.editTextValorMinimoEmpresa.setText(""+estabelicimento.getValorMinimo());
                binding.editTextTelefoneEmpresa.setText(estabelicimento.getTelefone());

                idLoja = estabelicimento.getId();



                if(estabelicimento.getStatus().equalsIgnoreCase(Constantes.ABERTA)){
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_ABERTA);
                    aSwitchAbrir_FecharLoja.setChecked(true);
                }else{
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_FECHADO);
                    aSwitchAbrir_FecharLoja.setChecked(false);
                }

            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
              //  if(resposta.getStatus()){
                    Toast.makeText(LojaActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                binding.progressBarLoja.setVisibility(View.GONE);
              //  }
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