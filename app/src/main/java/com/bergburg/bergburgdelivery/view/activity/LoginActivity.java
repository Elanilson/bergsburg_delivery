package com.bergburg.bergburgdelivery.view.activity;

import static android.graphics.Color.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.MainActivity;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewmodel.LoginViewModel;
import com.bergburg.bergburgdelivery.databinding.ActivityLoginBinding;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLOutput;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private String login = "";
    private String senha = "";
    private UsuarioPreferences preferences;
    private Dialog dialog;
    private Dialog dialogaTIVA_Conta;
    private Boolean btnLogin = false;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private Usuario usuarioAtual = new Usuario();
    private  Dialog dialogInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.buttonLogin.setTextColor(BLACK);
        preferences = new UsuarioPreferences(LoginActivity.this);

        String email = preferences.recuperarEmail();
        String senha = preferences.recuperarSenha();
        if(email != null && senha != null){
            if(!email.trim().isEmpty() && !senha.trim().isEmpty()){
                binding.checkBoxLembrarDeMin.setChecked(true);
            }
            binding.editCampoLogin.setText(email);
            binding.editCampoSenha.setText(senha);
        }


        binding.textViewEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaEsqueciSenha();
            }
        });
        viewModel = new  ViewModelProvider(this).get(LoginViewModel.class);
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.checkBoxLembrarDeMin.isChecked()){
                   String email = binding.editCampoLogin.getText().toString();
                   String senha = binding.editCampoSenha.getText().toString();
                   if(email != null && senha != null){
                       if(!email.trim().isEmpty() && !senha.trim().isEmpty()){
                           preferences.salvarEmailSenha(email,senha);
                       }
                   }
                }else{
                    preferences.salvarEmailSenha("","");
                }
                btnLogin = true;
                login();

            }
        });

        binding.textViewCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        verificarUsuarioLogado();
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
                System.out.println("usuario autal "+usuarioAtual);
                if(usuarioAtual.getContaAtiva() != null){
                    if(usuarioAtual.getContaAtiva().equalsIgnoreCase(Constantes.NAO)){
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        System.out.println("Milisegundos: "+System.currentTimeMillis());

                        btnLogin = false;
                        Long idUsuario = preferences.recuperarID();
                        if(idUsuario != null){
                            viewModel.buscarUsuario(idUsuario);
                        }

                        Long now = SystemClock.uptimeMillis();
                        Long next = now + (1000 - (now % 1000));
                        handler.postAtTime(runnable,next);
                    }

                }


            }
        };
        this.runnable.run();
    }

    private void verificarUsuarioLogado(){
        //recupero o id e busco o usuario para verificar o status de usuario
        Long idRecuperado = preferences.recuperarID();
        if(idRecuperado != null ){
            System.out.println("IDRecuperado: "+idRecuperado);
            viewModel.verificarUsuarioLogado(idRecuperado);
        }

    }

    private void alertaEsqueciSenha(){

         dialog = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.laytout_esqueci_minha_senha);
        dialog.setCancelable(false);
        EditText editEmail = dialog.findViewById(R.id.editTextTextEmail);
        Button btnConfirmar = dialog.findViewById(R.id.buttonConfirmar);
        Button btnCancelar = dialog.findViewById(R.id.buttonCancelar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                if(email != null && !email.trim().isEmpty() && email.trim().contains("@")){
                    viewModel.enviarEmailDeRecuperacao(email);

                }else{
                    Toast.makeText(LoginActivity.this, "E-mail invalido", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void alertaConfirmacaoEmail(String email){
        ticker = true;
        startClock();
        viewModel.enviarEmailDeConfirmacao(email);
        dialogaTIVA_Conta = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialogaTIVA_Conta.setContentView(R.layout.layout_enviar_email);
        dialogaTIVA_Conta.setCancelable(false);
        TextView textEmail = dialogaTIVA_Conta.findViewById(R.id.textViewEMailUsuario);
        Button btnConfirmar = dialogaTIVA_Conta.findViewById(R.id.buttonReiviarEmail);
        TextView textViewCancelar = dialogaTIVA_Conta.findViewById(R.id.textViewCancelar);
        textEmail.setText(email);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.enviarEmailDeConfirmacao(email);

                dialogaTIVA_Conta.dismiss();
            }
        });

        textViewCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // finish();
                dialogaTIVA_Conta.dismiss();
            }
        });

        dialogaTIVA_Conta.show();

    }

    private void observe() {

        viewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario != null){
                    usuarioAtual.setId(usuario.getId());
                    usuarioAtual.setNome(usuario.getNome());
                    usuarioAtual.setTelefone(usuario.getTelefone());
                    usuarioAtual.setEmail(usuario.getEmail());
                    usuarioAtual.setLogin(usuario.getLogin());
                    usuarioAtual.setStatus(usuario.getStatus());
                    usuarioAtual.setContaAtiva(usuario.getContaAtiva());
                    if(usuario.getContaAtiva() != null){
                        if(usuario.getContaAtiva().equalsIgnoreCase(Constantes.NAO)){
                            preferences.salvarContaAtivada(Constantes.NAO);
                            if(btnLogin){
                                alertaConfirmacaoEmail(usuario.getEmail());
                            }
                        }
                    }

                    String contaAtivada = preferences.recuperarContaAtivada();
                    if(contaAtivada != null){
                        if(contaAtivada.equalsIgnoreCase(Constantes.NAO) && usuario.getContaAtiva().equalsIgnoreCase(Constantes.SIM) && usuario.getStatus().equalsIgnoreCase(Constantes.DESLOGADO)){
                            // fecha a layout de confirmação e apresenta o alerta de conta ativa
                            //  Toast.makeText(CadastroActivity.this, "Conta ativada", Toast.LENGTH_SHORT).show();
                            preferences.salvarContaAtivada(Constantes.SIM);
                            //  dialog.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(Constantes.MENSAGEM)
                                    .setCancelable(false)
                                    .setMessage("Conta ativada com sucesso")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ticker = true;
                                            login();
                                        }
                                    })
                                    .setIcon(R.drawable.ic_baseline_cloud_done_24)
                                    .show();
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
                         Toast.makeText(LoginActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                        if(dialog != null){
                            dialog.dismiss();
                        }
                    }else if(resposta.getMensagem().equalsIgnoreCase(Constantes.LOGIN_SUCESSO)){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }else{
                   // snackbar(resposta.getMensagem());
                   Toast.makeText(LoginActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void login(){
        login = binding.editCampoLogin.getText().toString();
        senha = binding.editCampoSenha.getText().toString();
        viewModel.login(login,senha);
    }
    private  void snackbar(String mensagem){
       Snackbar snackbar = Snackbar.make(binding.LinearLayoutLogin, mensagem, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
       LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
       params.gravity = Gravity.TOP;
       view.setLayoutParams(params);
        snackbar
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogInternet = new Dialog(binding.getRoot().getContext(),android.R.style.Theme_Material_Light_Dialog_Presentation);
        if(!VerificadorDeConexao.isConnectionAvailable(binding.getRoot().getContext())){
            dialogInternet.setContentView(R.layout.layout_sem_conexao);
            Button btn = dialogInternet.findViewById(R.id.buttonAtualizar);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInternet.dismiss();
                }
            });

           // dialogInternet.show();
        }else{
            dialogInternet.dismiss();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialogInternet != null){
            dialogInternet.dismiss();
        }
        ticker = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dialogInternet != null){
            dialogInternet.dismiss();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialogInternet != null){
            dialogInternet.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticker = false;
    }
}