package com.bergburg.bergburgdelivery.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.PedidoAdapter;
import com.bergburg.bergburgdelivery.databinding.FragmentPedidosBinding;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.view.activity.ExibirPedidoActivity;
import com.bergburg.bergburgdelivery.viewmodel.PedidosViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PedidosFragment extends Fragment {
    private FragmentPedidosBinding binding;
    private PedidoAdapter pedidoAdapter = new PedidoAdapter();
    private PedidosViewModel viewModel;
    private UsuarioPreferences preferences;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new UsuarioPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPedidosBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(PedidosViewModel.class);

        binding.recyclerviewPedido.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerviewPedido.setAdapter(pedidoAdapter);

        OnListenerAcao<Pedido> onListenerAcao = new OnListenerAcao<Pedido>() {
            @Override
            public void onClick(Pedido obj) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_PEDIDO, obj.getId());
                Intent intent = new Intent(getActivity(), ExibirPedidoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Pedido obj) {

            }

            @Override
            public void onClickObservacao(Pedido obj) {

            }
        };

        pedidoAdapter.attackListener(onListenerAcao);



        observe();
        return  binding.getRoot();
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
                        System.out.println("Pedidos -Milisegundos: "+System.currentTimeMillis());

                        //ficar observando se tem alguma alteração
                        String statusLogado = preferences.recuperarStatus();
                        if (statusLogado != null) {
                            if(!statusLogado.equalsIgnoreCase(Constantes.DESLOGADO) && !statusLogado.isEmpty()){
                                Long idUsuario = preferences.recuperarID();
                                if (idUsuario != null) {
                                    viewModel.listarPedido(idUsuario);
                                }
                            }
                        }

                        Long now = SystemClock.uptimeMillis();
                        Long next = now + (1000 - (now % 1000));
                        handler.postAtTime(runnable,next);

            }
        };
        this.runnable.run();
    }

    private  void snackbar(String mensagem){
        Snackbar.make(binding.constraintlayoutPedidos, mensagem, Snackbar.LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    private void observe() {
        viewModel.pedidos.observe(getViewLifecycleOwner(), new Observer<List<Pedido>>() {
            @Override
            public void onChanged(List<Pedido> pedidos) {
                try {
                    if(pedidos != null){
                        if(pedidos.size() > 0){
                            binding.progressBarPedido.setVisibility(View.GONE);
                            binding.imagemPedidos.setVisibility(View.GONE);
                            binding.textPedido.setVisibility(View.GONE);
                            pedidoAdapter.attackPedidos(pedidos);
                        }else{
                            binding.progressBarPedido.setVisibility(View.GONE);
                            binding.imagemPedidos.setVisibility(View.VISIBLE);
                            binding.textPedido.setVisibility(View.VISIBLE);
                        }
                    }else{
                            binding.progressBarPedido.setVisibility(View.GONE);
                            binding.imagemPedidos.setVisibility(View.VISIBLE);
                            binding.textPedido.setVisibility(View.VISIBLE);

                    }
                }catch (Exception e){
                    System.out.println("Error peidosFragment "+e.getMessage());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ticker = true;
        startClock();


        String status = preferences.recuperarStatus();
        if(status != null){
            if(status.isEmpty() || status.equalsIgnoreCase(Constantes.DESLOGADO)){
                binding.imagemPedidos.setVisibility(View.VISIBLE);
                binding.textPedido.setVisibility(View.VISIBLE);
                binding.progressBarPedido.setVisibility(View.GONE);

            }
        }


    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onPause() {
        super.onPause();

        ticker = false;

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