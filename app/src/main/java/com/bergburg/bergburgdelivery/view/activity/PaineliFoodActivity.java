package com.bergburg.bergburgdelivery.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bergburg.bergburgdelivery.MainActivity;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.adapter.IFoodAdapter;
import com.bergburg.bergburgdelivery.databinding.ActivityPaineliFoodBinding;
import com.bergburg.bergburgdelivery.helpers.DadosIFoodPreferences;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.DetalhesPedido;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.LayoutPedidoIfood;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClientIFood;
import com.bergburg.bergburgdelivery.viewmodel.PedidosViewModel;
import com.bergburg.bergburgdelivery.viewmodel.IFoodMainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PaineliFoodActivity extends AppCompatActivity {
    private ActivityPaineliFoodBinding binding;
    private LayoutPedidoIfood layoutPedidoIfood = new LayoutPedidoIfood();
    private PedidosViewModel pedidosViewModel;
    private IFoodMainViewModel viewModel;
    private IFoodAdapter adapter = new IFoodAdapter();
    private Dialog dialog;
    private Runnable runnable;
    private Handler handler = new Handler();
    private Boolean ticker = false;
    private Timer timer;
    private Long idPedido = 0L;
    private DadosIFoodPreferences preferencesiFood;
    private String idPedidoIfood, linkAcompanhamentoPedido;
    private List<EventoPedido> listaTemporariaDeEventos = new ArrayList<>();
    private LinearLayout layoutAcompanhamento;
    private AlertDialog alertToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaineliFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new Dialog(this,android.R.style.Theme_Material_Light_Dialog_Presentation);
        viewModel = new ViewModelProvider(this).get(IFoodMainViewModel.class);
        pedidosViewModel = new ViewModelProvider(this).get(PedidosViewModel.class);

        binding.recyclerviewEventosIfood.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewEventosIfood.setAdapter(adapter);
        layoutAcompanhamento = binding.layoutAcompanhamento;

        setSupportActionBar(binding.toolbarPersonalizadaPaine.toolbar);

        binding.toolbarPersonalizadaPaine.imageViewButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recuperarObjeto();
        viewModel.verificarEvento();



        binding.buttonAcompanharPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linkAcompanhamentoPedido != null){
                    String url = linkAcompanhamentoPedido;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
        binding.buttonCopiarLinkPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linkAcompanhamentoPedido != null){

                    // Inicializando o ClipboardManager e os dados do Clip
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData;

                    // os dados do clipe são inicializados com a variável de texto declarada acima
                    clipData = ClipData.newPlainText("text", linkAcompanhamentoPedido);

                    // A área de transferência salva este objeto de clipe
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(PaineliFoodActivity.this, "Copiado.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        binding.buttonCriarPedidoIfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Um entregador parceiro será alocado automaticamente após o registro com sucesso de um pedido. \n \n Obs: Caso não seja possível realizar a entrega, o pedido não é registrado e é retornado um erro.")
                        .setPositiveButton("Criar pedido", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                binding.progressBarIfood.setVisibility(View.VISIBLE);
                                viewModel.criarPedidoIfood(layoutPedidoIfood);
                            }
                        })
                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

                System.out.println("Ifood: "+layoutPedidoIfood.toString());;

            }
        });
        binding.buttonConfirmarPedidoIfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Existe um tempo máximo de 8 minutos para confirmação dos pedidos. Caso ultrapasse esse período, o pedido é cancelado automaticamente.")

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.confirmarPedido(idPedidoIfood);
                            }
                        })

                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });

        binding.buttonDespachar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Você está preste a despachar o pedido.")

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.despachar(idPedidoIfood);
                            }
                        })

                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });
        binding.buttonCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcaos_de_cancelamento();

            }
        });
        binding.buttonAceitarCancelamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Aceitar uma solicitação de cancelamento ?")

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.aceitarPedidoDeCanelamento();
                            }
                        })

                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });
        binding.buttonNegaCAncelamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Negar uma solicitação de cancelamento ?")

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.negarPedidoDeCanelamento();
                            }
                        })

                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });
        binding.buttonVerificarFrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBarIfood.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Disponibilidade de entrega")
                        .setMessage("Antes de registrar um pedido de fora do iFood, é importante consultar a disponibilidade de entrega. É possível que o endereço não esteja dentro da área de cobertura e mesmo que o endereço esteja dentro da área , é possível que exista alguma indisponibilidade temporária na região, como raio de entrega reduzido temporariamente. \n \n Ao realizar a consulta antes de registrar o pedido evita que erros aconteçam e permitem que a loja se planeje na preparação do pedido.")

                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.verificarFreteIfood(layoutPedidoIfood.getEndereco());
                            }
                        })

                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });
        binding.buttonBuscarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.verificarEvento();

            }
        });

        observe();

    }

    private void opcaos_de_cancelamento(){
        dialog.setContentView(R.layout.layout_cancelar_pedido_ifood);

        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        Button btnFechar = dialog.findViewById(R.id.buttonFecharDialog);
        ProgressBar progressBarStatus = dialog.findViewById(R.id.progressBarStatus);
        Button btnSalvar = dialog.findViewById(R.id.buttonSalvar);
        EditText comenatarioCancelamento = dialog.findViewById(R.id.editTextComentario);
        String[] status = getResources().getStringArray(R.array.motivo_de_cancelamentos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,status);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarStatus.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setCancelable(false)
                        .setTitle("Confirmar")
                        .setMessage("Motivo do cancelamento: "+spinnerStatus.getSelectedItem().toString().toLowerCase() )
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dlog, int which) {
                                String comentario = "";
                                comentario = comenatarioCancelamento.getText().toString();
                               // viewModel.salvarStatusPedido(pedido.getId(),spinnerStatus.getSelectedItem().toString());
                                if(idPedidoIfood != null && !idPedidoIfood.isEmpty()){
                                    viewModel.cancelarPepdido(spinnerStatus.getSelectedItem().toString(),idPedidoIfood,comentario);
                                    progressBarStatus.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    dlog.dismiss();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBarStatus.setVisibility(View.GONE);
                            }
                        })
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void recuperarObjeto(){
     //  LayoutPedidoIfood bundle = (LayoutPedidoIfood) getIntent().getSerializableExtra("layoutIfood");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            layoutPedidoIfood = (LayoutPedidoIfood) bundle.getSerializable("layoutIfood");
            idPedido =  bundle.getLong("idPedido");

            preferencesiFood = new DadosIFoodPreferences(this,idPedido);
            idPedidoIfood = preferencesiFood.recuperaIdPedidoIfood();
            linkAcompanhamentoPedido = preferencesiFood.recuperarLinkAcompanhamento();


            if(idPedidoIfood != null && !idPedidoIfood.isEmpty()){
                layoutAcompanhamento.setVisibility(View.VISIBLE);
            }else{
                idPedidoIfood = preferencesiFood.recuperaIdPedidoIfood();
                linkAcompanhamentoPedido = preferencesiFood.recuperarLinkAcompanhamento();
                layoutAcompanhamento.setVisibility(View.GONE);

            }
        }
    }

    private void observe() {
        pedidosViewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
               // Toast.makeText(PaineliFoodActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.detalhesDoPedido.observe(this, new Observer<DetalhesPedido>() {
            @Override
            public void onChanged(DetalhesPedido detalhesPedido) {
                System.out.println("apkdoandroid: "+detalhesPedido.toString());
            }
        });
        viewModel.freteIfood.observe(this, new Observer<RespostaDisponibilidadeDeEntrega>() {
            @Override
            public void onChanged(RespostaDisponibilidadeDeEntrega respostaDisponibilidadeDeEntrega) {
                binding.progressBarIfood.setVisibility(View.GONE);
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Atenção!")
                        .setMessage("Há disponibilidade de entrega. O valor da taxa de entrega é de R$"+respostaDisponibilidadeDeEntrega.getCitar().getValorLiquido())
                        .setNegativeButton("Fechar", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();
                System.out.println("apkdoandroid: "+respostaDisponibilidadeDeEntrega.toString());
            }
        });
        viewModel.respostaPedido.observe(this, new Observer<RespostaPedido>() {
            @Override
            public void onChanged(RespostaPedido respostaPedido) {
              if(respostaPedido.getId() != null && !respostaPedido.getId().isEmpty()){
                  preferencesiFood.salvar(respostaPedido.getId(),respostaPedido.getLink());
                  SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy h:mm:s");
                  Date data = new Date();
                  String dataFormatada = formataData.format(data);
                  linkAcompanhamentoPedido = respostaPedido.getLink();
                  idPedidoIfood = respostaPedido.getId();
                //  adapter.limparDado();
                  viewModel.verificarEvento();
                  layoutAcompanhamento.setVisibility(View.VISIBLE);
                  viewModel.salvarEventos(idPedido,respostaPedido.getLink(),new EventoPedido(respostaPedido.getId(),dataFormatada));
                  Toast.makeText(PaineliFoodActivity.this, "Pedido criado", Toast.LENGTH_SHORT).show();
              }
                System.out.println("apkdoandroid: "+respostaPedido.toString());
            }
        });
        viewModel.autenticacao.observe(this, new Observer<Autenticacao>() {
            @Override
            public void onChanged(Autenticacao autenticacao) {
                if(autenticacao.getTokenDeAcesso() != null && !autenticacao.getTokenDeAcesso().isEmpty()){
                    preferencesiFood.salvarTokenIFood(autenticacao.getTokenDeAcesso(),autenticacao.getRefreshToken());
                     RetrofitClientIFood.novoToken(autenticacao.getTokenDeAcesso());
                    Toast.makeText(PaineliFoodActivity.this, "Autenticado!", Toast.LENGTH_SHORT).show();
                }
                    alertToken = null;
                System.out.println("apkdoandroid: "+autenticacao.toString());
            }
        });
        viewModel.eventoPedido.observe(this, new Observer<List<EventoPedido>>() {
            @Override
            public void onChanged(List<EventoPedido> eventoPedido) {
                if(eventoPedido != null){
                   adapter.limparDado();

                    analisarEventosE_Separando(eventoPedido);
                    analisarEventos(listaTemporariaDeEventos);

                    adapter.attackEventos(listaTemporariaDeEventos);
                    //System.out.println("xxxxxxxxxxxxxxxxxxxxxx total "+listaTemporariaDeEventos.size());
                   // listaTemporariaDeEventos.clear();
                    System.out.println("apkdoandroid: "+ eventoPedido.toString());
                }
            }
        });
        viewModel.resposta.observe(this, new Observer<Resposta>() {
            @Override
            public void onChanged(Resposta resposta) {
               // binding.progressBar.setVisibility(View.GONE);
                if(!resposta.getStatus()){
                    if(resposta.getMensagem().contains("Entrega Fácil indisponível")){
                        new AlertDialog.Builder(binding.getRoot().getContext())
                                .setTitle("Atenção!")
                                .setMessage(resposta.getMensagem())
                                .setNegativeButton("Fechar", null)
                                .setIcon(R.drawable.ic_baseline_warning_24)
                                .show();
                    }else if(
                            resposta.getMensagem().equalsIgnoreCase("token expired Tente novamente") ||
                                    resposta.getMensagem().equalsIgnoreCase("no jwt token Tente novamente") ||
                                    resposta.getMensagem().equalsIgnoreCase("null Tente novamente")
                    ){
                      //  Toast.makeText(PaineliFoodActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                        if(alertToken == null){
                            alertToken =  new AlertDialog.Builder(binding.getRoot().getContext())
                                    .setTitle("Token expirado!")
                                    .setMessage("Por favor, é nescessário a renovação do token. Após renovação tente novamente!")
                                    .setPositiveButton("Renovar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            viewModel.renovarToken(preferencesiFood.recuperarTokenRefresh(), PaineliFoodActivity.this);
                                            //viewModel.verificarEvento();
                                            alertToken = null;
                                        }
                                    })
                                    .setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertToken = null;
                                            viewModel.verificarEvento();
                                        }
                                    })
                                    .setIcon(R.drawable.ic_baseline_warning_24)
                                    .show();

                        }

                    }else{
                       // Toast.makeText(PaineliFoodActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    }

                }else{

                    if(resposta.getMensagem().contains("idEvento")){
                        String idEvento = resposta.getMensagem().replace("idEvento","");
                        List<EventoPedido> listaEventos = new ArrayList<>();
                        listaEventos.add(new EventoPedido(idEvento));

                       // viewModel.reconhecerLimparEnventos(listaEventos);
                        //limpar o evento x
                    }else if(resposta.getMensagem().equalsIgnoreCase("Sucesso ao limpar eventos")){
                       // adapter.limparDado();

                    }else if(resposta.getMensagem().equalsIgnoreCase("Pedido confirmado com sucesso")){
                        Toast.makeText(PaineliFoodActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    }else if(resposta.getMensagem().equalsIgnoreCase("Pedido despachado com sucesso")){
                        Toast.makeText(PaineliFoodActivity.this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
                    }
                  //  viewModel.verificarEvento();
                }
                System.out.println("apkdoandroid: status "+resposta.getStatus());
                System.out.println("apkdoandroid: "+resposta.getMensagem());

               // viewModel.verificarEvento();
                binding.progressBarIfood.setVisibility(View.GONE);
            }

        });
    }

    private void analisarEventosE_Separando(List<EventoPedido> eventoPedidos){

        for(EventoPedido evento : eventoPedidos){
            if(idPedidoIfood != null && !idPedidoIfood.isEmpty()){
                if(evento.getIdDoPedido().equalsIgnoreCase(idPedidoIfood)){
                    listaTemporariaDeEventos.add(evento);
                }
            }

        }

        if(listaTemporariaDeEventos.size() > 0){
        //- Enviar /acknowledgment para todos os eventos recebidos imediatamente após a request de polling;
      //  viewModel.reconhecerLimparEnventos(listaTemporariaDeEventos);

        }


    }

    private void analisarEventos(List<EventoPedido> eventoPedidos){
        for(EventoPedido evento : eventoPedidos){
             viewModel.salvarEventos(idPedido,"",evento);
            if(evento.getCodigoCompleto().equalsIgnoreCase("PLACED")){
               // pedidosViewModel.salvarStatusPedido(idPedido,"Novo pedido");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("PREPARATION_STARTED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Preparando");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("CONFIRMED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Confirmado");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("ASSIGN_DRIVER")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Entregador selecionado");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("GOING_TO_ORIGIN")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Entregador a caminho do restaurente");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("ARRIVED_AT_ORIGIN")){
                pedidosViewModel.salvarStatusPedido(idPedido,"O entregador chegou ao restaurante");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("COLLECTED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Coletado");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("DISPATCHED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Despachou");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("ARRIVED_AT_DESTINATION")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Chegou ao destino");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("CONCLUDED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Pedido entregue");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("CANCELLATION_REQUESTED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Cancelamento solicitado");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("CANCELLED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Cancelado");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("CANCELLATION_REQUEST_FAILED")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Falha no pedido de cancelamento");
            }else if (evento.getCodigoCompleto().equalsIgnoreCase("READY_TO_PICKUP")){
                pedidosViewModel.salvarStatusPedido(idPedido,"Pronto para recolher");
            }else{
                pedidosViewModel.salvarStatusPedido(idPedido,evento.getCodigoCompleto());

            }
        }
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

                    //- Fazer requests no endpoint de /polling regularmente a cada 30 segundos;

                    long TEMPO = (1000 * 30); // chama o método a cada 30 segundos

                    if (timer == null) {
                        timer = new Timer();
                        TimerTask tarefa = new TimerTask() {

                            public void run() {
                                try {
                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                    System.out.println("Painel_iFood -Milisegundos: "+System.currentTimeMillis());


                                    //ficar observando se tem alguma alteração de status do estabelicimento
                                    viewModel.verificarEvento();

                                  //  viewModel.getEventosIfood(idPedido);

                                    if(idPedidoIfood != null && !idPedidoIfood.isEmpty()){
                                        layoutAcompanhamento.setVisibility(View.VISIBLE);
                                    }else{
                                        idPedidoIfood = preferencesiFood.recuperaIdPedidoIfood();
                                        linkAcompanhamentoPedido = preferencesiFood.recuperarLinkAcompanhamento();
                                        layoutAcompanhamento.setVisibility(View.GONE);
                                    }

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









            }
        };
        this.runnable.run();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ticker = true;
        startClock();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ticker = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ticker = false;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ticker = false;
    }
}