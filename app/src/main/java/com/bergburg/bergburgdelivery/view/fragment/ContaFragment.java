package com.bergburg.bergburgdelivery.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.FragmentContaBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.view.activity.ChatActivity;
import com.bergburg.bergburgdelivery.view.activity.ConversasActivity;
import com.bergburg.bergburgdelivery.view.activity.GerencialProdutosActivity;
import com.bergburg.bergburgdelivery.view.activity.LoginActivity;
import com.bergburg.bergburgdelivery.view.activity.LojaActivity;
import com.bergburg.bergburgdelivery.view.activity.PerfilActivity;
import com.bergburg.bergburgdelivery.viewmodel.CadastroViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ContaFragment extends Fragment {
    private FragmentContaBinding binding;
    private FrameLayout frameLayoutSheetConta;
    private BottomSheetBehavior bottomSheetBehavior;
    private CadastroViewModel viewModel;
    private DadosPreferences preferences;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Endereco enderecoUsuario = new Endereco();
    private Usuario usuarioAtual =new Usuario();
    private Long idUsuario;
    private  Dialog dialogInternet;
    private Switch aSwitchAbrir_FecharLoja;
    private Long idLoja;
    private String statusLojaWeb = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new DadosPreferences(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);
         idUsuario = preferences.recuperarID();
         //versão do aplicativo
         binding.textVisdewVersaoApp.setText(getString(R.string.versaoApp));

        String statusLogado = preferences.recuperarStatus();
        if (statusLogado != null && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {
            if (idUsuario != null) {
                System.out.println("idUsuario " + idUsuario);
                viewModel.buscarUsuario(idUsuario);
                //
            }
        }

        if(idUsuario != null){
            if(idUsuario == Constantes.ADMIN){
                binding.switchAbrirFecharLoja2.setVisibility(View.VISIBLE);
                binding.layoutGencialProdutos.setVisibility(View.VISIBLE);
                binding.layoutLoja.setVisibility(View.VISIBLE);
                binding.view12.setVisibility(View.VISIBLE);
            }else{
                binding.switchAbrirFecharLoja2.setVisibility(View.GONE);
                binding.layoutLoja.setVisibility(View.GONE);
                binding.layoutGencialProdutos.setVisibility(View.GONE);
                binding.view12.setVisibility(View.GONE);
            }
        }

        binding.layoutLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {
                    if(idUsuario == Constantes.ADMIN){
                        startActivity(new Intent(getActivity(), LojaActivity.class));
                    }
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });

        binding.layoutGencialProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {
                    if(idUsuario == Constantes.ADMIN){
                         startActivity(new Intent(getActivity(), GerencialProdutosActivity.class));
                    }
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        binding.layoutPrivacidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://apkdoandroidonline.com/bergs_burg_delivery/documents/politica_de_privacidade_bergs_burg.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        binding.layoutTermoDeUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://apkdoandroidonline.com/bergs_burg_delivery/documents/termos_de_uso_bergs_burg.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        binding.layoutWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://contate.me/bergs_burg";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        binding.layoutInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/bergs_burg/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

            binding.layoutPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {

                            startActivity(new Intent(getActivity(), PerfilActivity.class));
                    }else{
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
            });

        binding.layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!statusLogado.isEmpty() && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {
                    if(idUsuario != null){
                        if(idUsuario == Constantes.ADMIN){
                             startActivity(new Intent(getActivity(), ConversasActivity.class));
                        }else{
                         startActivity(new Intent(getActivity(), ChatActivity.class));
                        }
                    }
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        aSwitchAbrir_FecharLoja = binding.switchAbrirFecharLoja2;

        aSwitchAbrir_FecharLoja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_ABERTA);
                    aSwitchAbrir_FecharLoja.setTextColor(getResources().getColor(R.color.verde));
                }else{
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_FECHADO);
                    aSwitchAbrir_FecharLoja.setTextColor(getResources().getColor(R.color.vermelho));
                }
                atualizar();
            }
        });


        observe();


       return binding.getRoot();
    }

    private void observe() {

        viewModel.estabelicimento.observe(getViewLifecycleOwner(), new Observer<Estabelicimento>() {
            @Override
            public void onChanged(Estabelicimento estabelicimento) {
                idLoja = estabelicimento.getId();

                statusLojaWeb = estabelicimento.getStatus();

                if(estabelicimento.getStatus().equalsIgnoreCase(Constantes.ABERTA)){
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_ABERTA);
                    aSwitchAbrir_FecharLoja.setTextColor(getResources().getColor(R.color.verde));
                    aSwitchAbrir_FecharLoja.setChecked(true);
                }else{
                    aSwitchAbrir_FecharLoja.setText(Constantes.LOJA_FECHADO);
                    aSwitchAbrir_FecharLoja.setTextColor(getResources().getColor(R.color.vermelho));
                    aSwitchAbrir_FecharLoja.setChecked(false);
                }
            }
        });

        viewModel.resposta.observe(getViewLifecycleOwner(), new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                Toast.makeText(getActivity(), resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                binding.progressBarStatusLoja.setVisibility(View.GONE);


            }
        });
    }

    private void atualizar(){
        binding.progressBarStatusLoja.setVisibility(View.VISIBLE);
       String status = binding.switchAbrirFecharLoja2.isChecked() == true ? "Aberto" : "Fechado";

       if(!statusLojaWeb.equalsIgnoreCase(status)){
           viewModel.atualizarStatusLoja(idLoja,status);
       }else{
           binding.progressBarStatusLoja.setVisibility(View.GONE);
       }

    }






    @Override
    public void onResume() {
        super.onResume();
        viewModel.getEstabelicimento();

    }

    @Override
    public void onStop() {
        super.onStop();



    }

    @Override
    public void onPause() {
        super.onPause();


    }
}
