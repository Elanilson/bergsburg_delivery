package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityCadastroBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewmodel.CadastroViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CadastroActivity extends AppCompatActivity  {
    private ActivityCadastroBinding binding;
    private CadastroViewModel viewModel;
    private DadosPreferences preferences;
    private Endereco enderecoUsuario = new Endereco();
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Boolean contaCriadoComSucesso = false;
    private Boolean enderecoSalvaComSucesso = false;
    private Dialog dialog;
    private String email = "";
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        preferences = new DadosPreferences(this);
        setContentView(binding.getRoot());

        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       

        binding.editCampoEMailCadastro.setText("elanilsonpp@hotmail.com");
        binding.editCampoLoginCadastro.setText("xxt");
        binding.editCampoSenha1Cadastro.setText("123");
        binding.editCampoSenha2Cadastro.setText("123");
        binding.editTextTextRua.setText("santa maria");
        binding.editTextTextBairro.setText("sacramenta");
        binding.editTextTextCidade.setText("belem");
        binding.editTextTextEstado.setText("para");
        binding.editTextTextCadatroCepCadastro.setText("66120300");
        binding.editTextTextNumeroCasaCadastro.setText("526");
        binding.editTextTextCadatroDDDCadastro.setText("91");
        binding.editTextTextNumeroTelefoneCadastro.setText("555");
        binding.editTextTextComplementoCadastro.setText("555");

        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);
        binding.layoutCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBarEnvioProduto.setVisibility(View.VISIBLE);
                String complemento = binding.editTextTextComplementoCadastro.getText().toString();
                String nome = binding.editCampoLoginCadastro.getText().toString();
                 email = binding.editCampoEMailCadastro.getText().toString();
                String senha1 = binding.editCampoSenha1Cadastro.getText().toString();
                String senha2 = binding.editCampoSenha2Cadastro.getText().toString();
                String rua = binding.editTextTextRua.getText().toString();
                String bairro = binding.editTextTextBairro.getText().toString();
                String cidade = binding.editTextTextCidade.getText().toString();
                String estado = binding.editTextTextEstado.getText().toString();
                String cep = binding.editTextTextCadatroCepCadastro.getText().toString();
                String numeroCasa = binding.editTextTextNumeroCasaCadastro.getText().toString();
                String telefone = binding.editTextTextCadatroDDDCadastro.getText().toString()+"-"+binding.editTextTextNumeroTelefoneCadastro.getText().toString();

                 //buscar cordenadas apartir do endereço
                // cordernadas encontradas com sucesso, liberação de salvamento do usuario
               if( buscarLozalização(numeroCasa+"-"+rua+"-"+bairro+"-"+cidade+"-"+estado+"-"+cep) && numeroCasa != null && !numeroCasa.isEmpty() && numeroCasa != " "){
                   enderecoUsuario.setRua(rua);
                   enderecoUsuario.setBairro(bairro);
                   enderecoUsuario.setCidade(cidade);
                   enderecoUsuario.setEstado(estado);
                   enderecoUsuario.setCep(cep);
                   enderecoUsuario.setNumeroCasa(numeroCasa);
                   enderecoUsuario.setComplemento(complemento);
                   enderecoUsuario.setLatitude(latitude);
                   enderecoUsuario.setLongitude(longitude);
                   System.out.println("Latitude:xxxx "+latitude+" - "+longitude);
                   //salvando cordendas localmente também
                   preferences.salvarCordenadas(String.valueOf(latitude),String.valueOf(longitude));
                   viewModel.salvarUsuario(nome,email,senha1,senha2,telefone,enderecoUsuario);

               }else{
                 //  snackbar("Preencha os campos corretamente");
                   Toast.makeText(CadastroActivity.this, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show();
                   binding.progressBarEnvioProduto.setVisibility(View.GONE);
               }

            }
        });


        observe();
    }

    private void startClock(){
        final Calendar calendar = Calendar.getInstance();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                if(!ticker){
                    return;
                }

                        calendar.setTimeInMillis(System.currentTimeMillis());
                        System.out.println("Cadastro -Milisegundos: "+System.currentTimeMillis());

                        Long idUsuario = preferences.recuperarID();
                        if(idUsuario != null){
                            viewModel.buscarUsuario(idUsuario);
                        }

                        Long now = SystemClock.uptimeMillis();
                        Long next = now + (1000 - (now % 1000));
                        handler.postAtTime(runnable,next);





            }
        };
        this.runnable.run();
    }

    private void observe() {
        viewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario != null){

                    //salvar o id do usuario quando a conta é criada
                    if(usuario.getContaAtiva() != null && usuario.getId() != null){
                        if(usuario.getContaAtiva().equalsIgnoreCase(Constantes.NAO)){

                              preferences.salvarIdUsuario(usuario.getId());
                        }
                        //local
                        String contaAtivada = preferences.recuperarContaAtivada();

                        if(contaAtivada != null){
                            System.out.println("Entrei: contaAtiva: "+contaAtivada);
                           if(contaAtivada.equalsIgnoreCase(Constantes.NAO) && usuario.getContaAtiva().equalsIgnoreCase(Constantes.SIM) && usuario.getStatus().equalsIgnoreCase(Constantes.DESLOGADO)){
                               // fecha a layout de confirmação e apresenta o alerta de conta ativa
                                   preferences.salvarContaAtivada(Constantes.SIM);
                                   //  dialog.dismiss();
                                   new AlertDialog.Builder(CadastroActivity.this)
                                           .setTitle(Constantes.MENSAGEM)
                                           .setCancelable(false)
                                           .setMessage("Conta ativada com sucesso")
                                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int which) {
                                                   ticker = false;
                                                   finish();
                                               }
                                           })
                                           .setIcon(R.drawable.ic_baseline_cloud_done_24)
                                           .show();
                           }
                        }
                    }
                }
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    if(resposta.getMensagem().equalsIgnoreCase(Constantes.EMAIL_ENVIADO)){
                          Toast.makeText(CadastroActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    }else if(resposta.getMensagem().equalsIgnoreCase(Constantes.CADASTRO_SUCESSO)){
                        preferences.salvarContaAtivada(Constantes.NAO);
                        alertaConfirmacaoEmail();
                    }

                }else{
                    Toast.makeText(CadastroActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }

                binding.progressBarEnvioProduto.setVisibility(View.GONE);
            }
        });
    }


    private void alertaConfirmacaoEmail(){
        ticker = true;
        startClock();
        viewModel.enviarEmailDeConfirmacao(email);
         dialog = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout_enviar_email);
        dialog.setCancelable(false);
        TextView textEmail = dialog.findViewById(R.id.textViewEMailUsuario);
        Button btnConfirmar = dialog.findViewById(R.id.buttonReiviarEmail);
        TextView textViewCancelar = dialog.findViewById(R.id.textViewCancelar);

        textEmail.setText(email);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.enviarEmailDeConfirmacao(email);
               // dialog.dismiss();
            }
        });

        textViewCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public Boolean buscarLozalização(String stringEndereco){
        // busca o a localização pelo endereco
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
          //  String stringEndereco = "6120300 - 526 - santa maria - sacramenta - belém - pa";
            List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco,1);
           // List<Address> listaEndereco = geocoder.getFromLocation(latitude,longitude,1);
            if(listaEndereco != null && listaEndereco.size() > 0){
                Address endereco = listaEndereco.get(0);
                System.out.println("localização: "+endereco.getLatitude()+","+endereco.getLongitude());
                latitude = endereco.getLatitude();
                longitude = endereco.getLongitude();

                preferences.salvarCordenadas(String.valueOf(latitude),String.valueOf(longitude));
               return true;
            }else{
                System.out.println("Nenhum endereco fornecido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error "+e.getMessage());
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();

    }

    @Override
    protected void onStop() {
        super.onStop();
       // ticker = false;

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
    protected void onDestroy() {
        super.onDestroy();
        ticker = false;
    }
}