package com.bergburg.bergburgdelivery.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.SacolaAdapter;
import com.bergburg.bergburgdelivery.databinding.FragmentSacolaBinding;
import com.bergburg.bergburgdelivery.helpers.Local;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.viewmodel.ExibirPedidoViewModel;
import com.bergburg.bergburgdelivery.viewmodel.SacolaViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class SacolaFragment extends Fragment {
    private FragmentSacolaBinding    binding;
    private SacolaViewModel viewModel;
    private ExibirPedidoViewModel exibirPedidoViewModel;
    private SacolaAdapter sacolaAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout frameLayoutSheetTotal;
    private LinearLayout layoutTotal;
    private CardView cardViewTotal;
    private Float total = 0f;
    private Float subTotal =0f;
    private Float taxa_de_entrega = 0f; // ele já fica com o valor da entrega disponivel
    private Float valor_do_frete = 0f; // ele recebe o valor da entrega ser fo aptante
    private Long idSacola;
    private Long idUsuario;
    String opcaoEntrega = "Retirar no local";
    private DadosPreferences preferences;
    private Usuario dadosUsuario = new Usuario();
    private  Boolean confirmarEntregaADomicilio = false;
    private  Boolean enderecoConfirmado = false;
    private ProgressBar progressBarEnvioPedido;
    private Dialog dialog;
    private  DecimalFormat formatCasaDecimal ;
    private Endereco enderecoUsuario = new Endereco();
    private ItensSacola itensSacolaLocal = new ItensSacola();
    private Estabelicimento estabelicimentoAtual = new Estabelicimento();
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private  Dialog dialogInternet;
    private String formarDePagamento = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new DadosPreferences(getActivity());
        formatCasaDecimal = new DecimalFormat("0.00");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSacolaBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(SacolaViewModel.class);
        exibirPedidoViewModel = new ViewModelProvider(this).get(ExibirPedidoViewModel.class);

        sacolaAdapter = new SacolaAdapter(getActivity());
        binding.recyclerviewSacola.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerviewSacola.setAdapter(sacolaAdapter);
        binding.buttonFazerPedido.setTextColor(Color.BLACK);

        frameLayoutSheetTotal = binding.frameSheetSacola;
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayoutSheetTotal);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.carviewTotal.setVisibility(View.VISIBLE);
                 //   Toast.makeText(binding.getRoot().getContext(), "STATE_COLLAPSED", Toast.LENGTH_SHORT).show();;
                }else if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    //Toast.makeText(binding.getRoot().getContext(), "STATE_EXPANDED", Toast.LENGTH_SHORT).show();;


                }else if(newState == BottomSheetBehavior.STATE_DRAGGING){
                   // Toast.makeText(binding.getRoot().getContext(), "STATE_DRAGGING", Toast.LENGTH_SHORT).show();;

                }else if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    binding.carviewTotal.setVisibility(View.VISIBLE);
                    //Toast.makeText(binding.getRoot().getContext(), "STATE_HIDDEN", Toast.LENGTH_SHORT).show();;

                }else if(newState == BottomSheetBehavior.STATE_HALF_EXPANDED){
                   // Toast.makeText(binding.getRoot().getContext(), "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT).show();;

                }
                else if(newState == BottomSheetBehavior.STATE_SETTLING){
                    //Toast.makeText(binding.getRoot().getContext(), "STATE_SETTLING", Toast.LENGTH_SHORT).show();;

                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

                    binding.buttonFazerPedido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("click: "+estabelicimentoAtual.toString());
                            if(estabelicimentoAtual.getStatus() != null){
                                if(estabelicimentoAtual.getStatus().equalsIgnoreCase("Aberto")){
                                    alertaConfirmarEndereco();
                                }else{
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Estabelecimento Fechado!")
                                            .setMessage("Lamentamos informar, mas o estabelecimento encontra-se fechado neste momento.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setIcon(R.drawable.ic_baseline_warning_24)
                                            .show();
                                }
                            }

                        }
                    });



        layoutTotal = binding.linearLayoutTotal;
        layoutTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.carviewTotal.setVisibility(View.GONE);
                bottomSheetTotal();
            }
        });

        OnListenerAcao<ItensSacola> onListenerAcao = new OnListenerAcao<ItensSacola>() {
            @Override
            public void onClick(ItensSacola itensSacola) {
                itensSacolaLocal = itensSacola;
             //   Toast.makeText(getActivity(), "getQuantidade "+obj.getQuantidade(), Toast.LENGTH_SHORT).show();
                if(itensSacola.getQuantidade() == 0){

                    viewModel.deletarItemDaSacola(itensSacola.getId());

                }else{
                    viewModel.atualizarQuantidadeItemSacola(idSacola,itensSacola.getId(),itensSacola.getQuantidade());
                }
               // sacolaAdapter.limpar();
                viewModel.getItensSacola(idSacola);
                //sacolaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLongClick(ItensSacola obj) {

            }

            @Override
            public void onClickObservacao(ItensSacola obj) {
                alertaExbirObservacao(obj);

            }
        };



        sacolaAdapter.attackListener(onListenerAcao);
        observer();

        return binding.getRoot();
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
                        System.out.println("Sacola-Milisegundos: "+System.currentTimeMillis());

                        //ficar observando se tem alguma alteração de status do estabelicimento
                         viewModel.getEstabelicimento();
                        idSacola = preferences.recuperarIDSacola();
                        if (idSacola != null) {
                            viewModel.getItensSacola(idSacola);
                        }

                        Long now = SystemClock.uptimeMillis();
                        Long next = now + (1000 - (now % 1000));
                        handler.postAtTime(runnable,next);

            }
        };
        this.runnable.run();
    }

    private void alertaExbirObservacao(ItensSacola itensSacola){
        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout_observacao);
        dialog.setCancelable(false);
        EditText editCampoObservacao = dialog.findViewById(R.id.editTextObservacao);
        Button btnConfirmar = dialog.findViewById(R.id.buttonConfirmarObservacao);
        Button btnCancelar = dialog.findViewById(R.id.buttonCAncelarObservacao);
        editCampoObservacao.setText(itensSacola.getObservacao());
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacao = editCampoObservacao.getText().toString();
                if(observacao != null && !observacao.isEmpty()){
                     viewModel.atualizarObservacaoItemSacola(idSacola,itensSacola.getId(),observacao);
                }else{
                     viewModel.atualizarObservacaoItemSacola(idSacola,itensSacola.getId()," ");
                }
                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener( v -> dialog.dismiss());

        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    private void alertaConfirmarEndereco(){



         dialog = new Dialog(getActivity(),android.R.style.Theme_Material_Light_Dialog_Presentation);
       // Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_retirada_ou_entegra);
        Button btnVoltar = dialog.findViewById(R.id.buttonVoltar);
        FrameLayout framFinalizar = dialog.findViewById(R.id.layoutFinalizarPedido);
        LinearLayout layoutEndereco = dialog.findViewById(R.id.layoutEndereco);
        LinearLayout layoutFrete = dialog.findViewById(R.id.layout_frete);

        RadioGroup opcaoDeEntrega = dialog.findViewById(R.id.radioGroupOpcao);
        RadioGroup opcaoDePagamento = dialog.findViewById(R.id.groupPagamento);
        Switch confirmarEndereco = dialog.findViewById(R.id.switchConfirmarEndereco);
        TextView nome = dialog.findViewById(R.id.textViewNomeView);
        TextView telefone = dialog.findViewById(R.id.textViewTelefoneView);
        TextView cep = dialog.findViewById(R.id.textViewCepView);
        TextView numeroCasa = dialog.findViewById(R.id.textViewNumeroCasaView);
        TextView endereco = dialog.findViewById(R.id.textViewEnderecoView);
        TextView subTotal_pedido = dialog.findViewById(R.id.textViexSubTotal);
        TextView frete_pedido = dialog.findViewById(R.id.textViecxwTaxaEntrega);
        TextView total_pedido = dialog.findViewById(R.id.textViewSheetTotal);
        progressBarEnvioPedido = dialog.findViewById(R.id.progressBarPedidoEnviado);

        cep.setText(enderecoUsuario.getCep());
        numeroCasa.setText(enderecoUsuario.getNumeroCasa());
        endereco.setText(
                enderecoUsuario.getRua()+","+
                        enderecoUsuario.getNumeroCasa()+", "+
                        enderecoUsuario.getBairro()+", "+
                        enderecoUsuario.getCidade()+",\n  Complemento: "+
                        enderecoUsuario.getComplemento()
        );
        frete_pedido.setText("R$ "+String.format("%.2f", taxa_de_entrega));

        opcaoDePagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (opcaoDePagamento.getCheckedRadioButtonId()){

                    case R.id.radioButtonDinheiro:
                        formarDePagamento = Constantes.DINHEIRO;
                        break;
                    case R.id.radioButtonPix:
                        formarDePagamento = Constantes.PIX;
                        break;
                    case R.id.radioButtonCartaoCredito:
                        formarDePagamento = Constantes.CARTAO_DE_CREDIDO;
                        break;
                    case R.id.radioButtonDebito:
                        formarDePagamento = Constantes.CARTAO_DE_DEBITO;
                        break;

                }

            }
        });


        opcaoDeEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (opcaoDeEntrega.getCheckedRadioButtonId()){
                    case R.id.radioButtonRetiraNoLocal:
                        valor_do_frete = 0f;
                        layoutEndereco.setVisibility(View.GONE);
                        layoutFrete.setVisibility(View.GONE);
                        confirmarEntregaADomicilio = false;
                        opcaoEntrega = getString(R.string.retirar_no_local);
                        total = subTotal; // sem taxa de entrega
                        total_pedido.setText("R$ "+String.format("%.2f",total));
                        System.out.println("Total: "+total);
                        break;
                    case R.id.radioButtonEntregaEmCasa:
                        valor_do_frete = Float.parseFloat(formatCasaDecimal.format(taxa_de_entrega).replace(",",".")); //formatando
                        opcaoEntrega = getString(R.string.entrega_a_domicilio);
                        confirmarEntregaADomicilio = true;
                        total = calcularTotal(taxa_de_entrega); // com taxa de entrega
                        total_pedido.setText("R$ "+String.format("%.2f",total));
                        layoutEndereco.setVisibility(View.VISIBLE);
                        layoutFrete.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        confirmarEndereco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enderecoConfirmado = isChecked;
            }
        });

        if(dadosUsuario != null ){
            nome.setText(dadosUsuario.getNome());
            telefone.setText(dadosUsuario.getTelefone());

        }



        subTotal_pedido.setText("R$ "+String.format("%.2f", subTotal));
        total = subTotal; // sem taxa de entrega
        total_pedido.setText("R$ "+String.format("%.2f",total));


        framFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entrega a domiciilio e endereço confirmado ou
                //retirada no local
                if (!formarDePagamento.isEmpty()){
                    if (opcaoEntrega.equalsIgnoreCase(getString(R.string.entrega_a_domicilio)) &&
                            enderecoConfirmado ||
                            opcaoEntrega.equalsIgnoreCase(getString(R.string.retirar_no_local))

                    ) {
                        total = Float.parseFloat(formatCasaDecimal.format(total).replace(",", ".")); //formatando
                        System.out.println("SubTotal: " + subTotal);
                        viewModel.criarPedido(formarDePagamento,idUsuario, idSacola, opcaoEntrega, total, valor_do_frete, subTotal);
                        progressBarEnvioPedido.setVisibility(View.VISIBLE);
                    } else {
                        //snackbar(getString(R.string.confirme_endereco_de_entrega));
                            Toast.makeText(binding.getRoot().getContext(), getString(R.string.confirme_endereco_de_entrega), Toast.LENGTH_SHORT).show();
                    }
              }else{
                    Toast.makeText(binding.getRoot().getContext(), getString(R.string.confirme_forma_pagamento), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total = subTotal;
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void alertaPedidoEnviado(){
        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout__pedido_enviado_com_sucesso);
        Button btnFechar = dialog.findViewById(R.id.buttonSucessoFechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new PedidosFragment()).commit();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void observer() {
        viewModel.estabelicimento.observe(getViewLifecycleOwner(), new Observer<Estabelicimento>() {
            @Override
            public void onChanged(Estabelicimento estabelicimento) {
                if(estabelicimento != null){
                    estabelicimentoAtual.setEndereco(estabelicimento.getEndereco());
                    estabelicimentoAtual.setId(estabelicimento.getId());
                    estabelicimentoAtual.setNome(estabelicimento.getNome());
                    estabelicimentoAtual.setRamo(estabelicimento.getRamo());
                    estabelicimentoAtual.setTempoEntrega(estabelicimento.getTempoEntrega());
                    estabelicimentoAtual.setValorMinimo(estabelicimento.getValorMinimo());
                    estabelicimentoAtual.setStatus(estabelicimento.getStatus());
                    if(estabelicimento.getStatus().equalsIgnoreCase(getString(R.string.fechado))){
                        binding.buttonFazerPedido.setText(getString(R.string.estabelicimento_fechado));
                        binding.buttonFazerPedido.setTextColor(Color.WHITE);
                        binding.buttonFazerPedido.setBackgroundColor(Color.RED);
                    }else{
                        binding.buttonFazerPedido.setText(getString(R.string.continuar));
                        binding.buttonFazerPedido.setBackgroundColor(Color.YELLOW);
                    }
                }
                System.out.println("Estabelicimento:2 "+estabelicimentoAtual.toString());
            }
        });
        viewModel.endereco.observe(getViewLifecycleOwner(), new Observer<Endereco>() {
            @Override
            public void onChanged(Endereco endereco) {
                taxa_de_entrega =  0f;
                if(endereco != null){
                    enderecoUsuario.setRua(endereco.getRua());
                    enderecoUsuario.setBairro(endereco.getBairro());
                    enderecoUsuario.setCidade(endereco.getCidade());
                    enderecoUsuario.setEstado(endereco.getEstado());
                    enderecoUsuario.setCep(endereco.getCep());
                    enderecoUsuario.setNumeroCasa(endereco.getNumeroCasa());
                    enderecoUsuario.setComplemento(endereco.getComplemento());
                    enderecoUsuario.setLatitude(endereco.getLatitude());
                    enderecoUsuario.setLongitude(endereco.getLongitude());
                    //recebe as cordenadas do endereço para gerar o valor da taxa de entrega
                    taxa_de_entrega = calcularTaxaDeEntrega();
                }
            }
        });
        viewModel.itensSacola.observe(getViewLifecycleOwner(), new Observer<List<ItensSacola>>() {
            @Override
            public void onChanged(List<ItensSacola> itensSacola) {

                if(itensSacola != null){
                    if(itensSacola.size() > 0){
                        binding.progressBarSacola.setVisibility(View.GONE);
                        binding.imageView3Lanches.setVisibility(View.GONE);
                        binding.textView3InfoLances.setVisibility(View.GONE);
                        binding.carviewTotal.setVisibility(View.VISIBLE);
                        total = 0f;
                        subTotal = 0f;
                        for (ItensSacola item : itensSacola){
                            //id igual e  quantidade igual, tudo certo
                            if(itensSacolaLocal.getId() == item.getId() && itensSacolaLocal.getQuantidade() == item.getQuantidade()){
                                subTotal += (item.getPreco() * item.getQuantidade());
                                //itens diferentes, tudo certo
                            }else if(itensSacolaLocal.getId() != item.getId() ) {
                                subTotal += (item.getPreco() * item.getQuantidade());
                            }else{ // id iqual e  quantidade diferente ele deve entrar aqui para sempre manter os valores atualizados
                                viewModel.getItensSacola(idSacola);
                            }
                        }

                        sacolaAdapter.attackProdutos(itensSacola);
                        // }
                        total = subTotal;
                        binding.textViewPreviewTotal.setText("R$ "+String.format("%.2f",total));
                        atualizarInfo();
                    }else{
                        binding.imageView3Lanches.setVisibility(View.VISIBLE);
                        binding.textView3InfoLances.setVisibility(View.VISIBLE);
                    }


                }else{
                    binding.progressBarSacola.setVisibility(View.GONE);
                    sacolaAdapter.limpar();
                    binding.imageView3Lanches.setVisibility(View.VISIBLE);
                    binding.textView3InfoLances.setVisibility(View.VISIBLE);
                    binding.carviewTotal.setVisibility(View.GONE);
                }



            }
        });
        viewModel.resposta.observe(getViewLifecycleOwner(), new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    viewModel.getItensSacola(idSacola);
                }else{
                   // snackbar(resposta.getMensagem());
                    Toast.makeText(getActivity(), resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewModel.respostaPedidoEnviado.observe(getViewLifecycleOwner(), new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
                if(resposta.getStatus()){
                    alertaPedidoEnviado();
                    dialog.dismiss();
                    sacolaAdapter.limpar();
                }else{
                   // snackbar(resposta.getMensagem());
                    Toast.makeText(getActivity(), resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                }
                progressBarEnvioPedido.setVisibility(View.GONE);
                viewModel.getItensSacola(idSacola);
            }
        });
        exibirPedidoViewModel.usuario.observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                try {
                    dadosUsuario.setTelefone(usuario.getTelefone());
                    dadosUsuario.setIdSacola(usuario.getIdSacola());
                    dadosUsuario.setNome(usuario.getNome());
                    dadosUsuario.setId(usuario.getId());
                    System.out.println("Dados usuario recebido: "+usuario.toString());
                }catch (Exception e){
                    System.out.println("Error SacolaFragment "+e.getMessage());
                }



            }
        });

    }

    private Float calcularTotal(Float taxa_de_entrega){
        if(taxa_de_entrega != null && taxa_de_entrega != 0.0){
          return subTotal + taxa_de_entrega;
        }
        return  subTotal;
    }
    private  void atualizarInfo(){

        TextView textViewSubTotal = frameLayoutSheetTotal.findViewById(R.id.textViexSubTotal);
        TextView textViewFrete = frameLayoutSheetTotal.findViewById(R.id.textViecxwFrete);
        TextView textViewTotal = frameLayoutSheetTotal.findViewById(R.id.textViewSheetTotal);
        textViewSubTotal.setText("R$ "+String.format("%.2f", subTotal));
        //textViewFrete.setText("R$ "+String.format("%.2f", taxa_de_entrega));
        textViewTotal.setText("R$ "+String.format("%.2f",total));
    }



    public void bottomSheetTotal(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        atualizarInfo();
    }
    private float calcularTaxaDeEntrega(){
        //latitudes do bergsburg
        LatLng localizacaoUsuario = new LatLng(enderecoUsuario.getLatitude(),enderecoUsuario.getLongitude() );
        LatLng bergsBurg = new LatLng(Constantes.LATITUDE_BERGS_BURG, Constantes.LONGITUDE_BERGS_BURG);
        float distancia = Local.calcularDistancia(localizacaoUsuario,bergsBurg);
        float valor = distancia * 3;// 3 reais a cada 1km
        DecimalFormat decimal = new DecimalFormat("0.00");
        String resultado = decimal.format(valor);
        System.out.println("Taxa de entrega : latitude "+localizacaoUsuario.latitude+" - "+localizacaoUsuario.longitude);
        System.out.println("Taxa de entrega : "+resultado);
        return  valor;
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

    @Override
    public void onResume() {
        super.onResume();
        ticker = true;
        startClock();

        String statusLogado = preferences.recuperarStatus();
        if (statusLogado != null && !statusLogado.equalsIgnoreCase(getString(R.string.deslogado))) {
            idUsuario = preferences.recuperarID();
            if (idUsuario != null) {
                exibirPedidoViewModel.getUsuario(idUsuario);
                viewModel.buscarEnderecoSalvo(idUsuario);
            }

        }else{
            binding.imageView3Lanches.setVisibility(View.VISIBLE);
            binding.textView3InfoLances.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ticker = false;

    }

    @Override
    public void onStart() {
        super.onStart();

        ticker = false;

    }
}