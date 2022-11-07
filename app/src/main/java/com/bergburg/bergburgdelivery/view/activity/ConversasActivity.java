package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.ConversasAdapter;
import com.bergburg.bergburgdelivery.adapter.MensagensAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityConversasBinding;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Conversas;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.viewmodel.ConversasViewModel;

import java.util.List;

public class ConversasActivity extends AppCompatActivity {
    private ActivityConversasBinding binding;
    private ConversasViewModel viewModel;
    private ConversasAdapter adapter;
    public static Boolean statusActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarPersonalizada.textViewTituloToolbar.setText("Chat");
        binding.toolbarPersonalizada.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(ConversasViewModel.class);

        adapter = new ConversasAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(binding.getRoot().getContext());
        manager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerViewConversas.setLayoutManager(manager);
        binding.recyclerViewConversas.setAdapter(adapter);

        OnListenerAcao<Conversas> listenerAcao = new OnListenerAcao<Conversas>() {
            @Override
            public void onClick(Conversas obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.NOME,obj.getNome());
                bundle.putLong(Constantes.ID_CONVERSA,obj.getId());
                bundle.putLong(Constantes.ID_USUARIO,obj.getIdUsuario());
                Intent intent = new Intent(ConversasActivity.this,ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void onLongClick(Conversas obj) {

            }

            @Override
            public void onClickObservacao(Conversas obj) {

            }
        };
        adapter.attackListener(listenerAcao);
        observe();
    }

    private void observe() {

        viewModel.conversas.observe(this, new Observer<List<Conversas>>() {
            @Override
            public void onChanged(List<Conversas> conversas) {
                if(conversas != null){
                    adapter.attackConversas(conversas);

                }
            }
        });

        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    Toast.makeText(ConversasActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ConversasActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getConversas();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        statusActivity = true;

    }
}