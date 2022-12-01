package com.bergburg.bergburgdelivery.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.PedidoAdapter;
import com.bergburg.bergburgdelivery.databinding.FragmentPedidosBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.view.activity.ExibirPedidoActivity;
import com.bergburg.bergburgdelivery.viewmodel.PedidosViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;


public class PedidosFragment extends Fragment {
    private FragmentPedidosBinding binding;
    private PedidoAdapter pedidoAdapter = new PedidoAdapter();
    private PedidosViewModel viewModel;
    private DadosPreferences preferences;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private Dialog dialog;
    private ProgressBar progressBarStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new DadosPreferences(getActivity());
        dialog = new Dialog(getActivity());
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
                Long idUSuario = preferences.recuperarID();
                if(idUSuario != null){
                    if(idUSuario.equals(Constantes.ADMIN)){
                            opcaos_de_pedido(obj);
                    }
                }

            }

            @Override
            public void onClickObservacao(Pedido obj) {

            }
        };

        pedidoAdapter.attackListener(onListenerAcao);



        observe();
        return  binding.getRoot();
    }

    private void opcaos_de_pedido(Pedido pedido){
        dialog.setContentView(R.layout.layout_opcoes_pedido);

        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        progressBarStatus = dialog.findViewById(R.id.progressBarStatus);
        Button btnSalvar = dialog.findViewById(R.id.buttonSalvar);
        String[] status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,status);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarStatus.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle("Alteração")
                        .setMessage("Alterar o status para ("+spinnerStatus.getSelectedItem().toString()+")" )
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.salvarStatusPedido(pedido.getId(),spinnerStatus.getSelectedItem().toString());

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBarStatus.setVisibility(View.GONE);
                            }
                        }).show();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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

        viewModel.estabelicimento.observe(getViewLifecycleOwner(), new Observer<Estabelicimento>() {
            @Override
            public void onChanged(Estabelicimento estabelicimento) {
                if(estabelicimento != null){
                    preferences.salvarInfLoja(estabelicimento.getNome(), estabelicimento.getEndereco(), estabelicimento.getTelefone());
                }
            }
        });

        viewModel.resposta.observe(getViewLifecycleOwner(), new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                    if(resposta.getStatus()){
                        dialog.dismiss();
                    }
                    progressBarStatus.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), resposta.getMensagem(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ticker = true;
        startClock();
        viewModel.getEstabelicimento();


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