package com.bergburg.bergburgdelivery.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.PopularesAdapter;
import com.bergburg.bergburgdelivery.adapter.CategoriaAdapter;
import com.bergburg.bergburgdelivery.databinding.FragmentHomeBinding;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.view.activity.ExibirProdutoActivity;
import com.bergburg.bergburgdelivery.view.activity.ListarProdutosActivity;
import com.bergburg.bergburgdelivery.viewmodel.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private CategoriaAdapter categoriaAdapter ;
    private PopularesAdapter popularesAdapter = new PopularesAdapter();
    private MapView mapView;
    private GoogleMap mMap;
    private DadosPreferences preferences;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private TextView nomeEstabelicimento,ramoEstabelicimento,enderecoEstabelicimento,tempodeEntregaEstabelicimento,valorMinimoEstabelicimento,statusEstabelicimento;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private  Dialog dialogInternet;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new DadosPreferences(getActivity());
        if(!preferences.recuperarLatitude().isEmpty()){
            latitude = Double.parseDouble(preferences.recuperarLatitude());
            longitude = Double.parseDouble(preferences.recuperarLogitude());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mapView = binding.mapViewBergs;
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);




        nomeEstabelicimento = binding.textViewNomeEstabelicimento;
        ramoEstabelicimento = binding.textViewRamoEstabelicimento;
      //  enderecoEstabelicimento = binding.textViewEnderecoEstabelicimento;
        tempodeEntregaEstabelicimento = binding.textViewTempoEstabelicimento;
        valorMinimoEstabelicimento = binding.textViewValorMinimoEstabelicimento;
        statusEstabelicimento = binding.textViewStatusEstabelicimento;




        binding.viewClickMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRota();
            }
        });



        categoriaAdapter = new CategoriaAdapter(binding.getRoot().getContext());

        LinearLayoutManager manager = new LinearLayoutManager(binding.getRoot().getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);

        LinearLayoutManager managerProdutosPopulares = new LinearLayoutManager(binding.getRoot().getContext());
        managerProdutosPopulares.setOrientation(RecyclerView.VERTICAL);

        LinearLayoutManager manager4 = new LinearLayoutManager(binding.getRoot().getContext());
        manager4.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewCategoria.setLayoutManager(manager);
       binding.recyclerviewCategoria.setAdapter(categoriaAdapter);

        binding.recyclerviewPopulares.setLayoutManager(managerProdutosPopulares);
        binding.recyclerviewPopulares.setAdapter(popularesAdapter);
        binding.recyclerviewPopulares.setNestedScrollingEnabled(false);

        OnListenerAcao<Categoria> listener = new OnListenerAcao<Categoria>() {
            @Override
            public void onClick(Categoria obj) {

                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_CATEGORIA,obj.getId());
                Intent intent = new Intent(getContext(), ListarProdutosActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Categoria obj) {

            }

            @Override
            public void onClickObservacao(Categoria obj) {

            }
        };

        OnListenerAcao<Produto> produtoOnListenerAcao = new OnListenerAcao<Produto>() {
            @Override
            public void onClick(Produto obj) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constantes.ID_PRODUTO,obj.getId());
                Intent intent = new Intent(getActivity(), ExibirProdutoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Produto obj) {

            }

            @Override
            public void onClickObservacao(Produto obj) {

            }
        };
        //carregar dados do estabelicimento
        viewModel.getEstabelicimento();
        observe();

        popularesAdapter.attackListener(produtoOnListenerAcao);
        categoriaAdapter.attackListener(listener);


        return binding.getRoot();
    }


    private  void abrirRota(){
        String localizacao = +Constantes.LATITUDE_BERGS_BURG+","+Constantes.LONGITUDE_BERGS_BURG;
        Uri uri = Uri.parse("google.navigation:q="+localizacao+"&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void startClock(){

        final Calendar calendar = Calendar.getInstance();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                if(!ticker){
                    return;
                }

                try{


                    long TEMPO = (1000 * 5); // chama o método a cada 5 segundos

                    if (timer == null) {
                        timer = new Timer();
                        TimerTask tarefa = new TimerTask() {

                            public void run() {
                                try {
                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                    System.out.println("Home -Milisegundos: "+System.currentTimeMillis());

                                    //ficar observando se tem alguma alteração de status do estabelicimento
                                    viewModel.getEstabelicimento();

                                    Long now = SystemClock.uptimeMillis();
                                    Long next = now + (1000 - (now % 1000));
                                    handler.postAtTime(runnable,next);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
                    }



                }catch (Exception e){

                }

            /*    try {
                    int delay = 5000;   // delay de 5 seg.
                    int interval = 60000;  // intervalo de 1 seg.
                     if(timer == null){
                           timer = new Timer();
                     }


                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {

                            calendar.setTimeInMillis(System.currentTimeMillis());
                            System.out.println("Home -Milisegundos: "+System.currentTimeMillis());

                            //ficar observando se tem alguma alteração de status do estabelicimento
                            viewModel.getEstabelicimento();

                            Long now = SystemClock.uptimeMillis();
                            Long next = now + (1000 - (now % 1000));
                            handler.postAtTime(runnable,next);

                        }
                    }, delay, interval);
                }catch (Exception e){
                    System.out.println("Error "+e.getMessage());
                }*/

                System.out.println("Home - fora");







            }
        };
        this.runnable.run();

    }


    private void observe() {
        viewModel.categorias.observe(getActivity(), new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                if(categorias != null){
                    System.out.println("Home - categorias total "+categorias.size());
                    if(categorias.size() > 0){
                        binding.progressBarCategorias.setVisibility(View.GONE);
                        categoriaAdapter.attackCategorias(categorias);
                    }else {
                        binding.progressBarCategorias.setVisibility(View.VISIBLE);
                    }
                }else{
                    binding.progressBarCategorias.setVisibility(View.VISIBLE);
                }
            }
        });


        viewModel.produtos.observe(getActivity(), new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> produtos) {
               if(produtos != null){
                   System.out.println("Home - produtos total "+produtos.size());
                   if(produtos.size() > 0) {
                       binding.progressBarProdutosPopulares.setVisibility(View.GONE);
                       popularesAdapter.attackProdutos(produtos);
                   }else {
                       binding.progressBarProdutosPopulares.setVisibility(View.VISIBLE);
                   }
               }else{
                   binding.progressBarProdutosPopulares.setVisibility(View.VISIBLE);
               }
            }
        });
        viewModel.estabelicimento.observe(getViewLifecycleOwner(), new Observer<Estabelicimento>() {
            @Override
            public void onChanged(Estabelicimento estabelicimento) {
                if(estabelicimento != null){
                    preferences.salvarInfLoja(estabelicimento.getNome(), estabelicimento.getEndereco(), estabelicimento.getTelefone());
                    System.out.println("Home - "+estabelicimento.toString());
                    nomeEstabelicimento.setText(estabelicimento.getNome());
                    ramoEstabelicimento.setText(estabelicimento.getRamo());
                   // enderecoEstabelicimento.setText(estabelicimento.getEndereco());
                    valorMinimoEstabelicimento.setText("Valor mínimo: R$ "+String.format("%.2f",estabelicimento.getValorMinimo()));
                    if(estabelicimento.getStatus().equalsIgnoreCase("Fechado")){
                        statusEstabelicimento.setTextColor(Color.RED);
                        statusEstabelicimento.setText("Estabelecimento fechado!");
                        statusEstabelicimento.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_cancel_24,0,0,0);
                    }else{
                        statusEstabelicimento.setTextColor(Color.rgb(0,100,0));
                        statusEstabelicimento.setText("Estabelecimento aberto.");
                        statusEstabelicimento.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24,0,0,0);
                    }
                    tempodeEntregaEstabelicimento.setText(estabelicimento.getTempoEntrega());
                }
            }
        });

        viewModel.resposta.observe(getActivity(), new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(!resposta.getStatus()){
                    //snackbar(resposta.getMensagem());
                    Toast.makeText(binding.getRoot().getContext(), resposta.getMensagem(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ticker = true;
        startClock();


        viewModel.getCategorias();
        viewModel.produtosPopulares();


        mapView.onResume();

    }

    private  void snackbar(String mensagem){
        Snackbar.make(binding.scrolHome, mensagem, Snackbar.LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.grey11))
                .setBackgroundTint(getResources().getColor(R.color.amarelo))
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
      //  LatLng BergsBurg = new LatLng(-1.3774531415796394, -48.43608019913573);
        LatLng BergsBurg = new LatLng(-1.377264747242989, -48.43608930925187);
       // mMap.addMarker(new MarkerOptions().position(BergsBurg).title("Berg's Burg"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BergsBurg, 18));

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        viewModel.getCategorias();
        viewModel.produtosPopulares();


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();


        ticker = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        ticker = false;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        ticker = false;


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        ticker = false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ticker = false;
        mapView.onLowMemory();


    }


}