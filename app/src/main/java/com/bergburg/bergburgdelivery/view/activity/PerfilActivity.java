package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.ActivityPerfilBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.viewmodel.PerfilViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PerfilActivity extends AppCompatActivity {
    private ActivityPerfilBinding binding;
    private PerfilViewModel viewModel;
    private DadosPreferences preferences;
    private Long idUsuario;
    private Usuario usuarioAtual = new Usuario();
    private Endereco enderecoAtual = new Endereco();
    private  Dialog dialogAlterarSenha;
    private  Dialog dialogAlterarDadosUsuario;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarPersonalizadaPerfil.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        preferences = new DadosPreferences(this);


        binding.layoutAlterarDadosUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaAlerarDadosUsuario();
            }
        });

        binding.layoutAlteraDadosEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaDadosEndereco();
            }
        });

        binding.layoutAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaAlterarSenha();
            }
        });

        observe();

    }

    private void alertaAlerarDadosUsuario(){
        dialogAlterarDadosUsuario = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialogAlterarDadosUsuario.setContentView(R.layout.layout_alterar_dados_usuario);
        dialogAlterarDadosUsuario.setCancelable(false);
        EditText editNome = dialogAlterarDadosUsuario.findViewById(R.id.editNome);
        EditText editDDD = dialogAlterarDadosUsuario.findViewById(R.id.editDDD);
        EditText editTelefone = dialogAlterarDadosUsuario.findViewById(R.id.editTextTelefone);
        Button btnConfirmar = dialogAlterarDadosUsuario.findViewById(R.id.buttonConfirmarObservacao);
        Button btnCancelar = dialogAlterarDadosUsuario.findViewById(R.id.buttonCAncelarObservacao);
        if(usuarioAtual.getNome() != null && usuarioAtual.getDdd() != 0 && usuarioAtual.getTelefone() != null){
            editNome.setText(usuarioAtual.getNome());
            editDDD.setText(""+usuarioAtual.getDdd());
            editTelefone.setText(usuarioAtual.getTelefone());
        }
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editNome.getText().toString();
                String ddd = editDDD.getText().toString();
                String telefone = editTelefone.getText().toString();

                viewModel.alterarDadosUsuario(usuarioAtual.getEmail(),nome,ddd+"-"+telefone);

                dialogAlterarDadosUsuario.dismiss();
            }
        });

        btnCancelar.setOnClickListener( v -> dialogAlterarDadosUsuario.dismiss());

        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAlterarDadosUsuario.show();

    }

    private void alertaDadosEndereco(){
        Dialog dialog = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout_alterar_dados_endereco);
        dialog.setCancelable(false);
        EditText editRua = dialog.findViewById(R.id.editTextTextRua3);
        EditText editCep = dialog.findViewById(R.id.editTextTextCadatroCepCadastro3);
        EditText editnumeroCasa = dialog.findViewById(R.id.editTextTextNumeroCasaCadastro3);
        EditText editBairro = dialog.findViewById(R.id.editTextTextBairro3);
        EditText editComplemento = dialog.findViewById(R.id.editTextTextComplementoCadastro3);
        Button btnConfirmar = dialog.findViewById(R.id.buttonConfirmar);
        Button btnCancelar = dialog.findViewById(R.id.buttonCAncelar);

        if(enderecoAtual.getRua() != null && enderecoAtual.getCep() != null && enderecoAtual.getNumeroCasa() != null && enderecoAtual.getBairro() != null && enderecoAtual.getComplemento() != null ){
            editBairro.setText(enderecoAtual.getBairro());
            editRua.setText(enderecoAtual.getRua());
            editnumeroCasa.setText(enderecoAtual.getNumeroCasa());
            editCep.setText(enderecoAtual.getCep());
            editComplemento.setText(enderecoAtual.getComplemento());
        }
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rua = editRua.getText().toString();
                String numeroCasa = editnumeroCasa.getText().toString();
                String cep = editCep.getText().toString();
                String bairro = editBairro.getText().toString();
                String complemento = editComplemento.getText().toString();

                if(rua != null && numeroCasa != null && cep != null && bairro != null){
                    if(!rua.equalsIgnoreCase("") && !numeroCasa.equalsIgnoreCase("") &&
                           !cep.equalsIgnoreCase("") && !bairro.equalsIgnoreCase("")){
                        if(!rua.equalsIgnoreCase(" ") &&
                                !numeroCasa.equalsIgnoreCase(" ") &&
                                !cep.equalsIgnoreCase(" " ) &&
                                !bairro.equalsIgnoreCase(" ")){

                            if(buscarLozalização(numeroCasa+"-"+rua+"-"+bairro+"-"+enderecoAtual.getCidade()+"-"+enderecoAtual.getEstado()+"-"+cep)){
                                enderecoAtual.setRua(rua);
                                enderecoAtual.setNumeroCasa(numeroCasa);
                                enderecoAtual.setCep(cep);
                                enderecoAtual.setComplemento(complemento);
                                enderecoAtual.setLatitude(latitude);
                                enderecoAtual.setLongitude(longitude);
                                viewModel.alterarEndereco(usuarioAtual,enderecoAtual);

                            }else{
                                Toast.makeText(PerfilActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                                Toast.makeText(PerfilActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                                Toast.makeText(PerfilActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();

                    }
                }else{
                                Toast.makeText(PerfilActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();

                }



                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener( v -> dialog.dismiss());

        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    private void alertaAlterarSenha(){
        dialogAlterarSenha = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialogAlterarSenha.setContentView(R.layout.layout_dados_alterar_senha);
        dialogAlterarSenha.setCancelable(false);
        EditText editSenha1 = dialogAlterarSenha.findViewById(R.id.editTextSenha2);
        EditText editSenha2 = dialogAlterarSenha.findViewById(R.id.editTConfirmarSenha2);
        Button btnConfirmar = dialogAlterarSenha.findViewById(R.id.buttonConfirmarObservacao);
        Button btnCancelar = dialogAlterarSenha.findViewById(R.id.buttonCAncelarObservacao);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senha1 = editSenha1.getText().toString();
                String senha2 = editSenha2.getText().toString();
                viewModel.alterarSenha(usuarioAtual.getEmail(), senha1,senha2);

            }
        });

        btnCancelar.setOnClickListener( v -> dialogAlterarSenha.dismiss());

        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAlterarSenha.show();

    }

    public Boolean buscarLozalização(String stringEndereco){
        Boolean localizacaoRecuperada = false;
        // busca o a localização pelo endereco
        Geocoder geocoder = new Geocoder(PerfilActivity.this, Locale.getDefault());
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
                localizacaoRecuperada = true;

            }else{
                System.out.println("Nenhum endereco fornecido");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error "+e.getMessage());
        }
        return  localizacaoRecuperada;
    }

    private void observe() {

        viewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                try{
                    String telefoneComDD = String.valueOf(usuario.getTelefone());
                    if(telefoneComDD.contains("-")){
                    String[] numeroTelefone = telefoneComDD.split("-");
                    usuarioAtual.setTelefone(numeroTelefone[1]);
                    usuarioAtual.setDdd(Integer.parseInt(numeroTelefone[0]));

                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                    if(usuario.getNome() != null){
                        preferences.salvarNome(usuario.getNome());
                        preferences.salvarIdUsuario(usuario.getId());
                    }
                    usuarioAtual.setEmail(usuario.getEmail());
                    usuarioAtual.setId(usuario.getId());
                    usuarioAtual.setNome(usuario.getNome());
                    usuarioAtual.setContaAtiva(usuario.getContaAtiva());
                    usuarioAtual.setStatus(usuario.getStatus());


                    binding.textViewNome.setText(usuario.getNome());
                    binding.textViewTelefone.setText(usuario.getTelefone());
                    binding.textView3Email.setText(usuario.getEmail());

            }
        });
        viewModel.endereco.observe(this, new Observer<Endereco>() {
            @Override
            public void onChanged(Endereco endereco) {
                enderecoAtual.setEstado(endereco.getEstado());
                enderecoAtual.setBairro(endereco.getBairro());
                enderecoAtual.setCidade(endereco.getCidade());
                enderecoAtual.setCep(endereco.getCep());
                enderecoAtual.setDdd(endereco.getDdd());
                enderecoAtual.setRua(endereco.getRua());
                enderecoAtual.setComplemento(endereco.getComplemento());
                enderecoAtual.setLatitude(endereco.getLatitude());
                enderecoAtual.setLongitude(endereco.getLongitude());
                enderecoAtual.setNumeroCasa(endereco.getNumeroCasa());

                binding.textCep.setText(endereco.getCep());
                binding.textViewRua.setText(endereco.getCep());
                binding.textViewBairro.setText(endereco.getBairro());
                binding.textViewCidade.setText(endereco.getCidade());
                binding.textViewEstado.setText(endereco.getEstado());
                binding.textViewNumeroCasa.setText(endereco.getNumeroCasa());
                binding.editTextTextComplementoCadastro2.setText(endereco.getComplemento());


            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    if(resposta.getMensagem().equalsIgnoreCase(Constantes.ALTERADO_SUCESSO)){
                        if(dialogAlterarDadosUsuario != null){
                            dialogAlterarDadosUsuario.dismiss();
                        }

                        if(dialogAlterarSenha != null){
                              dialogAlterarSenha.dismiss();
                        }
                    }

                    if(idUsuario != null){
                        viewModel.buscarUsuario(idUsuario);
                        viewModel.buscarEnderecoSalvo(idUsuario);
                    }
                    Toast.makeText(PerfilActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PerfilActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();

                }
            }
        });
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
                System.out.println("Perfil-Milisegundos: "+System.currentTimeMillis());

                idUsuario = preferences.recuperarID();
                if(idUsuario != null){
                    viewModel.buscarUsuario(idUsuario);
                    viewModel.buscarEnderecoSalvo(idUsuario);
                }


                Long now = SystemClock.uptimeMillis();
                Long next = now + (1000 - (now % 1000));
                handler.postAtTime(runnable,next);

            }
        };
        this.runnable.run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();
    }

    @Override
    public void onPause() {
        super.onPause();
        ticker = false;

    }

    @Override
    public void onStart() {
        super.onStart();
        ticker = false;

    }

    @Override
    protected void onStop() {
        super.onStop();
        ticker = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RetrofitClient.CancelarRequisicoes();
        ticker = false;
    }
}