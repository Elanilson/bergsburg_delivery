package com.bergburg.bergburgdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.databinding.ActivityMainBinding;
import com.bergburg.bergburgdelivery.helpers.Permissoes;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.notificacaoLocal.NotificationHelper;
import com.bergburg.bergburgdelivery.model.Conversas;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.view.activity.ChatActivity;
import com.bergburg.bergburgdelivery.view.activity.LoginActivity;
import com.bergburg.bergburgdelivery.view.fragment.ContaFragment;
import com.bergburg.bergburgdelivery.view.fragment.HomeFragment;
import com.bergburg.bergburgdelivery.view.fragment.PedidosFragment;
import com.bergburg.bergburgdelivery.view.fragment.SacolaFragment;
import com.bergburg.bergburgdelivery.viewmodel.ChatViewModel;
import com.bergburg.bergburgdelivery.viewmodel.ConversasViewModel;
import com.bergburg.bergburgdelivery.viewmodel.LoginViewModel;
import com.bergburg.bergburgdelivery.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LoginViewModel loginViewModel;
    private ConversasViewModel conversasViewModel;
    private BottomNavigationView bottomNavigationView;
    private MainViewModel mainViewModel;
    private UsuarioPreferences preferences;
    private Boolean logado = false;
    private  Menu menu;

    private List<Mensagem> mensagensLocal = new ArrayList<>();
    private ChatViewModel chatViewModel;
    private PendingIntent notifyPendingIntent;
    private Long idUsuario;
    private Usuario usuarioAtual = new Usuario();

    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private String tokenUsuario = "";
    private Boolean  btnDeslogado = false;
    public static Boolean statusActivity = false;


    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Home");

        //validar Permissao
        Permissoes.validarPermissoes(permissoes, MainActivity.this, 1);


        conversasViewModel = new ViewModelProvider(this).get(ConversasViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        preferences = new UsuarioPreferences(MainActivity.this);

        String status = preferences.recuperarStatus();
        System.out.println("Status recuperado: "+status);
        if(status != null){
            if(status.equalsIgnoreCase("Logado")){
                logado = true;
            }
        }else{
            logado = false;
        }

        idUsuario = preferences.recuperarID();
        System.out.println("Id_usuario.... "+idUsuario);
        bottomNavigationView = binding.bottomnavigation;
        if(idUsuario != null && status != null){
            if(idUsuario == Constantes.ADMIN && !status.equalsIgnoreCase(Constantes.DESLOGADO)){
                bottomNavigationView.getMenu().clear(); //clear old inflated items.
                bottomNavigationView.inflateMenu(R.menu.menu_admin);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new PedidosFragment()).commit();
            }else{
                bottomNavigationView.getMenu().clear(); //clear old inflated items.
                bottomNavigationView.inflateMenu(R.menu.menu_principal);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new HomeFragment()).commit();
            }
        }else{
            bottomNavigationView.getMenu().clear(); //clear old inflated items.
            bottomNavigationView.inflateMenu(R.menu.menu_principal);
            getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new HomeFragment()).commit();
        }


        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportActionBar().setTitle("Home");
                        fragment = new HomeFragment();
                        break;
                    case R.id.sacola:
                        getSupportActionBar().setTitle("Sacola");
                        fragment = new SacolaFragment();
                        break;
                    case R.id.conta:
                        getSupportActionBar().setTitle("Conta");
                        fragment = new ContaFragment();
                        break;
                    case R.id.pedidos:
                        getSupportActionBar().setTitle("Pedidos");
                        fragment = new PedidosFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
                return true;
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

                Long now = SystemClock.uptimeMillis();
                Long next = now + (1000 - (now % 1000));
                handler.postAtTime(runnable,next);


            }
        };
        this.runnable.run();
    }




    private void observe() {
        mainViewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    // Toast.makeText(MainActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    System.out.println("Token "+resposta.getMensagem());
                }else{
                    System.out.println("Token "+resposta.getMensagem());
                    //  Toast.makeText(MainActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        loginViewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioAtual = usuario;
                if(usuario != null){
                    if(usuario.getStatus() != null){
                        if(usuario.getStatus().equalsIgnoreCase("Deslogado")){
                            preferences.salvarContaAtivada("");
                            logado = false;
                        }else{
                            logado = true;
                        }
                        String status = preferences.recuperarStatus();
                        Long idUsuario = preferences.recuperarID();
                        if(status != null){
                            if(idUsuario != null){
                                if(status.equalsIgnoreCase("Deslogado")){
                                    if(!status.equalsIgnoreCase(usuario.getStatus())){
                                        System.out.println("Entrei para deslogar telefone-status "+status+" - online "+usuario.getStatus());
                                        loginViewModel.deslogar(idUsuario);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        loginViewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    if(resposta.getMensagem().equalsIgnoreCase(Constantes.DESLOGADO)){
                        preferences.limpar();
                        menu.setGroupVisible(R.id.deslogado,true);
                    }
                }else{
                    if(resposta.getMensagem().equalsIgnoreCase(Constantes.USUARIO_NAO_ENCONTRADO)){
                        preferences.limpar();
                        menu.setGroupVisible(R.id.deslogado,true);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                    //  snackbar(resposta.getMensagem());
                    Toast.makeText(MainActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error login "+resposta.getMensagem());

                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opcoes,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sair:
                btnDeslogado = true;
                deslogar();
                break;
            case R.id.login:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogar() {
        System.out.println("Quero deslogar");
        Long idUsuario = preferences.recuperarID();
        System.out.println("Deslogar: "+idUsuario);
        loginViewModel.deslogar(idUsuario);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new HomeFragment()).commit();
        //onRestart();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if(logado){
            menu.setGroupVisible(R.id.logado,true);
            menu.setGroupVisible(R.id.deslogado,false);
        }else{
            menu.setGroupVisible(R.id.deslogado,true);
            menu.setGroupVisible(R.id.logado,false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                // snackbar("Permissão de localização não aceita!");
                Toast.makeText(this, "Permissão de localização não aprovada", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private  void snackbar(String mensagem){
        Snackbar.make(binding.layoutMAin, mensagem, Snackbar.LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    public String recuperarToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                tokenUsuario = token;
                System.out.println("token recuperado: "+token);
            }
        });
        return tokenUsuario;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String status = preferences.recuperarStatus();
        System.out.println("Status recuperado: "+status);
        if(status != null){
            if(status.equalsIgnoreCase("Logado")){
                logado = true;
            }
        }else{
            logado = false;
        }


        if(logado){
            ticker = true;
          //  startClock();
        }else{
            ticker = false;
        }

        String logado = preferences.recuperarStatus();
        idUsuario = preferences.recuperarID();
        if(idUsuario != null){
            System.out.println("id verificado usuario: "+idUsuario);
            loginViewModel.verificarUsuarioLogado(idUsuario);
        }
        if(idUsuario != null && logado != null){
            //  chatViewModel.getMensagens(idUsuario);
            if(logado.equalsIgnoreCase(Constantes.LOGADO)){
                if(!btnDeslogado){
                    mainViewModel.enviarToken(idUsuario,recuperarToken());
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        statusActivity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ticker = false;
        statusActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticker = false;
    }
}