package com.bergburg.bergburgdelivery.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.MainActivity;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.MensagensAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityChatBinding;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.notificacaoLocal.NotificationHelper;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Token;
import com.bergburg.bergburgdelivery.viewmodel.ChatViewModel;
import com.bergburg.bergburgdelivery.viewmodel.MainViewModel;
import com.bergburg.bergburgdelivery.viewmodel.PedidosViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatViewModel viewModel;
    private MainViewModel mainViewModel;
    private MensagensAdapter adapter;
    private EditText editTextMensagem;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private List<Mensagem> mensagensLocal = new ArrayList<>();
    private UsuarioPreferences preferences;
    private  Long idUsuario,idConversa;
    private Token tokenAtual = new Token();
    private String mensagemEnviada = "";
    private String tituloMensagem = "Nova mensagem";
    private  String titulo = "";
    private Boolean verSacola = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarPersonalizada.toolbar);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        preferences = new UsuarioPreferences(ChatActivity.this);
        editTextMensagem = binding.editTextMensagem;
        binding.toolbarPersonalizada.textViewTituloToolbar.setText("Chat");
        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // viewModel.getMensagens();

        adapter = new MensagensAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(binding.getRoot().getContext());
       // manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        manager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewConversas.setLayoutManager(manager);
        binding.recyclerviewConversas.setAdapter(adapter);
        binding.recyclerviewConversas.setNestedScrollingEnabled(false);
        binding.recyclerviewConversas.smoothScrollToPosition(1);
      //  binding.recyclerviewConversas.

        binding.imageButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagem = editTextMensagem.getText().toString();
                binding.textViewInfo.setText("Enviando...");
                    if(mensagem != null && !mensagem.isEmpty()){
                        if(!mensagem.trim().isEmpty()){
                            System.out.println("passou");
                          //  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //    imm.hideSoftInputFromWindow(editTextMensagem.getWindowToken(), 0);
                            String nome = preferences.recuperarNome();
                            Long idUsuario = preferences.recuperarID();
                            //titulo da notificação recebe o nome do usario que está enviado a mensagem
                            if(nome != null){
                                tituloMensagem = nome;
                            }
                            if(idUsuario != null){
                                mensagemEnviada = mensagem;
                                viewModel.enviarMensagem(idConversa,mensagem,idUsuario);
                                binding.textViewInfo.setVisibility(View.VISIBLE);
                            }
                            editTextMensagem.setText("");
                        }
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

                idUsuario = preferences.recuperarID();
                idConversa = preferences.recuperarIDConversa();

                //recupero o id enviado
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    idConversa = bundle.getLong(Constantes.ID_CONVERSA);
                    idUsuario = bundle.getLong(Constantes.ID_USUARIO);
                   System.out.println("xxxxxxxxxxxxx cliente "+idUsuario);

                    if(preferences.recuperarID() != idUsuario){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                mainViewModel.getToken(idUsuario);
                            }
                        }, 30000);

                        titulo = bundle.getString(Constantes.NOME);
                        String nomeUsuario = preferences.recuperarNome();
                        if(titulo != null && nomeUsuario != null && !titulo.equalsIgnoreCase(nomeUsuario)){
                            binding.toolbarPersonalizada.textViewTituloToolbar.setText(titulo);
                        }
                    }else{
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                mainViewModel.getToken(Constantes.ADMIN);
                            }
                        }, 30000);

                    }

                }else{
                    //enviar notificação de mensagem para admin
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mainViewModel.getToken(Constantes.ADMIN);
                        }
                    }, 30000);

                        System.out.println("xxxxxxxxxxxxx admin "+idUsuario);
                }

                if(idUsuario != null){
                    viewModel.getMensagens(idUsuario);
                    System.out.println("Buscando novas mensagens");
                }else{
                    System.out.println("nullo não buscou mensagens");
                }

                Long now = SystemClock.uptimeMillis();
                Long next = now + (1000 - (now % 1000));
                handler.postAtTime(runnable,next);



            }
        };
        this.runnable.run();
    }
    private void observe() {

        mainViewModel.token.observe(this, new Observer<Token>() {
            @Override
            public void onChanged(Token token) {

                tokenAtual = token;

            }
        });

        mainViewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    System.out.println("Notificação enviada "+resposta.getMensagem());
                }else{
                    System.out.println("Button "+resposta.getStatus());
                    System.out.println("Notificação não enviada "+resposta.getMensagem());

                }

            }
        });

        viewModel.mensagens.observe(this, new Observer<List<Mensagem>>() {
            @Override
            public void onChanged(List<Mensagem> mensagems) {
                if(mensagems != null){
                    if(mensagensLocal.size() != mensagems.size()){
                        binding.textViewInfo.setVisibility(View.GONE);
                        mensagensLocal = mensagems;
                        binding.recyclerviewConversas.smoothScrollToPosition(mensagems.size());
                        adapter.attackMensagens(mensagems);
                    }
                    //salvo o id da ultima mensasgem lida
                    Long idUsuario = preferences.recuperarID();
                    if(mensagems != null){
                        if(mensagems.size() > 0) {
                            binding.progressBarMensagens.setVisibility(View.GONE);
                            if (idUsuario != null) {
                                if (mensagems.get(mensagems.size() - 1).getIdUsuario() != idUsuario) {
                                    viewModel.visualizarMensagem(mensagems.get(mensagems.size() - 1).getId());
                                    preferences.salvarIdUltimaMensagemLida(mensagems.get(mensagems.size() - 1).getId());
                                    System.out.println("SAlvando o id da ultima mensagem "+mensagems.get(mensagems.size() - 1).getId());
                                }
                            }
                        }
                    }
                }


            }
        });

        viewModel.mensagen.observe(this, new Observer<Mensagem>() {
            @Override
            public void onChanged(Mensagem mensagem) {
                if(!mensagem.getVisualizado().equalsIgnoreCase(Constantes.SIM)){
                    mainViewModel.enviarNotificacao(tokenAtual.getToken(), tituloMensagem,mensagemEnviada,idUsuario,idConversa);
                }
                System.out.println("visualizado ?? "+mensagem.toString());
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    if(resposta.getMensagem().equalsIgnoreCase(Constantes.ENVIADO)){
                      binding.textViewInfo.setVisibility(View.GONE);
                     // mainViewModel.enviarNotificacao(tokenAtual.getToken(), tituloMensagem,mensagemEnviada,idUsuario,idConversa);
                    }else{
                        binding.textViewInfo.setText("Aguardando conexão..");
                    }
                }else{
                    Toast.makeText(ChatActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }
                    binding.textViewInfo.setVisibility(View.GONE);
                    binding.textViewInfo.setText("");


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_conversa,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.meusPedidos:
                verSacola = true;
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_USUARIO,idUsuario);
                Intent intent = new Intent(ChatActivity.this,MeusPedidosActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();
        verSacola = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ticker = false;
       if(!verSacola){
           finish();
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticker = false;
    }
}