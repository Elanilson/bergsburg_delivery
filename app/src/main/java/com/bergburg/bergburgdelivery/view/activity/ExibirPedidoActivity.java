package com.bergburg.bergburgdelivery.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ViewPedidoAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityExibirPedidoBinding;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewmodel.ExibirPedidoViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExibirPedidoActivity extends AppCompatActivity {
    private ActivityExibirPedidoBinding binding;
    private ExibirPedidoViewModel viewModel;
    private ViewPedidoAdapter adapter ;
    private UsuarioPreferences preferences;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private  Dialog dialogInternet;
    private Usuario usuarioAtual = new Usuario();
    private Boolean primeiroCarregamento = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExibirPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = new UsuarioPreferences(this);
        viewModel = new ViewModelProvider(this).get(ExibirPedidoViewModel.class);

        setSupportActionBar(binding.toolbarPersonalizada.toolbar);

        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ViewPedidoAdapter(ExibirPedidoActivity.this);



        binding.recyclerviewViewDelhatesPedido.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerviewViewDelhatesPedido.setAdapter(adapter);


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
                            System.out.println("ExibirPedido -Milisegundos: "+System.currentTimeMillis());

                            Handler handler = new Handler();
                            Bundle bundle = getIntent().getExtras() ;
                            if(bundle != null){
                                Long idPedido = bundle.getLong(Constantes.ID_PEDIDO);
                                System.out.println("idPedido: "+idPedido);
                                viewModel.listarPedido(idPedido);
                            }


                            Long now = SystemClock.uptimeMillis();
                            Long next = now + (1000 - (now % 1000));
                            handler.postAtTime(runnable,next);

            }
        };
        this.runnable.run();
    }

    private void observe() {
        viewModel.endereco.observe(this, new Observer<Endereco>() {
            @Override
            public void onChanged(Endereco endereco) {

                adapter.attackEnderede(endereco);
            }
        });
        viewModel.pedido.observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(Pedido pedido) {
                System.out.println("listarPedido: "+pedido.toString());
                viewModel.getStatusPedido(pedido.getId());
                viewModel.getItensPedido(pedido.getId());
                viewModel.getUsuario(pedido.getIdUsuario());
                viewModel.buscarEnderecoSalvo(pedido.getIdUsuario());
                adapter.attackPedido(pedido);
            }
        });
        viewModel.itensPedido.observe(this, new Observer<List<ItensPedido>>() {
            @Override
            public void onChanged(List<ItensPedido> itensPedidos) {
                System.out.println("itensPedidos: "+itensPedidos.size());
                adapter.attackItensPedidos(itensPedidos);
            }
        });
        viewModel.statusPedido.observe(this, new Observer<List<Status_pedido>>() {
            @Override
            public void onChanged(List<Status_pedido> status_pedidos) {
                System.out.println("status_pedidos: "+status_pedidos.size());
                adapter.attackstatusPedido(status_pedidos);
            }
        });
        viewModel.usuario.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
               if(usuario != null){
                   usuarioAtual = usuario;
                   System.out.println("usuario id: "+usuario.toString());
                   adapter.attackUsuario(usuario);
               }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatAdmin:
                String nome = "";
                if(usuarioAtual != null){
                    if(usuarioAtual.getId() != null){

                        if(usuarioAtual.getId() != Constantes.ADMIN){
                            nome = usuarioAtual.getNome();
                        }else{
                            nome = "Chat";
                        }
                        System.out.println(usuarioAtual.toString());
                        Bundle bundle = new Bundle();
                        bundle.putString(Constantes.NOME,nome);
                        bundle.putLong(Constantes.ID_USUARIO,usuarioAtual.getId());
                        bundle.putLong(Constantes.ID_CONVERSA,usuarioAtual.getIdConversa());
                        Intent intent = new Intent(ExibirPedidoActivity.this,ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                }
             //   deslogar();
                break;
            case R.id.login:
               /// startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();
        primeiroCarregamento = true;



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
    public void onStop() {
        super.onStop();
        ticker = false;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ticker = false;


    }
}