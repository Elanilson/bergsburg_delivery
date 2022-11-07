package com.bergburg.bergburgdelivery.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.databinding.FragmentContaBinding;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.view.activity.CadastroActivity;
import com.bergburg.bergburgdelivery.view.activity.ChatActivity;
import com.bergburg.bergburgdelivery.view.activity.ConversasActivity;
import com.bergburg.bergburgdelivery.view.activity.GerencialProdutosActivity;
import com.bergburg.bergburgdelivery.view.activity.LoginActivity;
import com.bergburg.bergburgdelivery.view.activity.PerfilActivity;
import com.bergburg.bergburgdelivery.viewmodel.CadastroViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ContaFragment extends Fragment {
    private FragmentContaBinding binding;
    private FrameLayout frameLayoutSheetConta;
    private BottomSheetBehavior bottomSheetBehavior;
    private CadastroViewModel viewModel;
    private UsuarioPreferences preferences;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Endereco enderecoUsuario = new Endereco();
    private Usuario usuarioAtual =new Usuario();
    private Long idUsuario;
    private  Dialog dialogInternet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new UsuarioPreferences(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);
         idUsuario = preferences.recuperarID();

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
                binding.layoutGencialProdutos.setVisibility(View.VISIBLE);
                binding.view12.setVisibility(View.VISIBLE);
            }else{
                binding.layoutGencialProdutos.setVisibility(View.GONE);
                binding.view12.setVisibility(View.GONE);

            }
        }

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



       return binding.getRoot();
    }




    @Override
    public void onResume() {
        super.onResume();

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
