package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.DadosIFoodPreferences;
import com.bergburg.bergburgdelivery.ifood.Cliente;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.Coordenadas;
import com.bergburg.bergburgdelivery.ifood.model.DetalhesPedido;
import com.bergburg.bergburgdelivery.ifood.model.EnderecoDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.Entrega;
import com.bergburg.bergburgdelivery.ifood.model.Error;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.Item;
import com.bergburg.bergburgdelivery.ifood.model.LayoutEnvioPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.ifood.model.Telefone;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClientIFood;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.IfoodService;
import com.bergburg.bergburgdelivery.viewmodel.CancelamentoDePedido;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IfoodRepositorio {
    private Context context;
    private IfoodService service ;
    private BergburgService apiService = RetrofitClient.classService(BergburgService.class);
    private  String  grantType ;
    private  String  clientId ;
    private  String  clientSecret ;
    private Cliente cliente = new Cliente();
    private EnderecoDeEntrega enderecoDeEntrega = new EnderecoDeEntrega();
    private Entrega entrega = new Entrega();
    private Coordenadas coordenadas = new Coordenadas();
    private Telefone telefone = new Telefone();
    private List<Item> itens=  new ArrayList<>();
    private String token = "";

    public IfoodRepositorio(Context context ) {
        this.context = context;
        DadosIFoodPreferences  preferencesiFood = new DadosIFoodPreferences(context,0L);
        preferencesiFood.recuperarLinkAcompanhamento();

        if(preferencesiFood.recuperarToken() != null){
            token = preferencesiFood.recuperarToken();
        }

        service =  RetrofitClientIFood.classService(IfoodService.class);
        RetrofitClientIFood.novoToken(token);

         grantType = context.getString(R.string.grantType);
         clientId = context.getString(R.string.clientId);
         clientSecret = context.getString(R.string.clientSecret);
    }


    public void autenticar(APIListener<Autenticacao> listener){
        Call<Autenticacao> call = service.autenticar(grantType,clientId,clientSecret);
        call.enqueue(new Callback<Autenticacao>() {
            @Override
            public void onResponse(Call<Autenticacao> call, Response<Autenticacao> response) {
                if(response.isSuccessful()){
                    listener.onSuccess(response.body());

                }else{
                    System.out.println("apkdoandroid: "+response.errorBody());
                    try {
                        String json = response.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        Error obj = gson.fromJson(json, Error.class);
                        listener.onFailures(obj.getMensagem() + " Tente novamente");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Autenticacao> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });

    }

    public void verificarEventos(APIListener<List<EventoPedido>> listener){
        Call<List<EventoPedido>> call = service.verificarEventos();
        call.enqueue(new Callback<List<EventoPedido>>() {
            @Override
            public void onResponse(Call<List<EventoPedido>> call, Response<List<EventoPedido>> response) {
                if(response.isSuccessful()){
                    System.out.println("resposta: "+response.code());
                    listener.onSuccess(response.body());

                }else{
                    System.out.println("apkdoandroid: "+response.errorBody());
                    try {
                        String json = response.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        Error obj = gson.fromJson(json, Error.class);
                        listener.onFailures(obj.getMensagem() + " Tente novamente");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventoPedido>> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });
    }

  /*  public void reconhecerLimparEnventos(APIListener<List<EventoPedido>> listener, List<EventoPedido> eventoPedidos){
        Call<List<EventoPedido>> call = service.reconhecerLimparEnventos(eventoPedidos);
        call.enqueue(new Callback<List<EventoPedido>>() {
            @Override
            public void onResponse(Call<List<EventoPedido>> call, Response<List<EventoPedido>> response) {
                if(response.isSuccessful()){
                    listener.onSuccess(response.body());
                    System.out.println("Codigo: "+response.code());

                }else{
                    System.out.println("apkdoandroid: "+response.errorBody());
                    try {
                       // listener.onFailures(response.errorBody().string());
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                       // e.printStackTrace(); //melhorar
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventoPedido>> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });
    }*/

    public void confirmarPedido(APIListener<Boolean> listener,String idPedido){
       // String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        Call<Void> call = service.confirmarPedido(idPedido);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{
                        try {
                            String json = response.errorBody().string();
                            Gson gson = new GsonBuilder().create();
                            Error obj = gson.fromJson(json, Error.class);
                            listener.onFailures(obj.getMensagem() + " Tente novamente");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onSuccess(false);
                   }


                    System.out.println("Code: "+response.code());
                    System.out.println("Body: "+response.body());


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }
    public void cancelarPepdido(APIListener<Boolean> listener,CancelamentoDePedido cancelamentoDePedido, String idPedido){
      //  String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        Call<Void> call = service.cancelarPepdido(idPedido,cancelamentoDePedido);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{
                        try {
                            String json = response.errorBody().string();
                            Gson gson = new GsonBuilder().create();
                            Error obj = gson.fromJson(json, Error.class);
                            listener.onFailures(obj.getMensagem() + " Tente novamente");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onSuccess(false);

                    }


                System.out.println("Body: "+response.body());
                System.out.println("Code: "+response.code());

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }
    public void aceitarPedidoDeCanelamento(APIListener<Boolean> listener){
        String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        Call<Void> call = service.aceitarPedidoDeCanelamento(idPedido);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{

                        try {
                            String json = response.errorBody().string();
                            Gson gson = new GsonBuilder().create();
                            Error obj = gson.fromJson(json, Error.class);
                            listener.onFailures(obj.getMensagem() + " Tente novamente");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onSuccess(false);
                    }
                    System.out.println("Code: "+response.code());
                    System.out.println("Body: "+response.body());


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }
    public void negarPedidoDeCanelamento(APIListener<Boolean> listener){
        String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        Call<Void> call = service.negarPedidoDeCanelamento(idPedido);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{

                        try {
                            String json = response.errorBody().string();
                            Gson gson = new GsonBuilder().create();
                            Error obj = gson.fromJson(json, Error.class);
                            listener.onFailures(obj.getMensagem() + " Tente novamente");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        listener.onSuccess(false);
                    }
                    System.out.println("Code: "+response.code());
                    System.out.println("Body: "+response.body());


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }
    public void buscarDetalhesDoPedido(APIListener<DetalhesPedido> listener){
        String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        Call<DetalhesPedido> call = service.buscarDetalhesDoPedido(idPedido);
        call.enqueue(new Callback<DetalhesPedido>() {
            @Override
            public void onResponse(Call<DetalhesPedido> call, Response<DetalhesPedido> response) {
                if(response.code() == 200){
                    listener.onSuccess(response.body());
                }else{
                    try {
                        String json = response.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        Error obj = gson.fromJson(json, Error.class);
                        listener.onFailures(obj.getMensagem() + "Tente novamente");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
               // System.out.println("Detalhes Pedido "+response.body());
             //   System.out.println("Detalhes Pedido "+response.errorBody());

            }

            @Override
            public void onFailure(Call<DetalhesPedido> call, Throwable t) {
                listener.onFailures(t.getMessage());


            }
        });
    }
    public void reconhecerLimparEnventos(APIListener<Boolean> listener, List<EventoPedido> eventoPedidos){
        Call<Void> call = service.reconhecerLimparEnventos(eventoPedidos);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{
                        listener.onSuccess(false);
                    }
                    System.out.println("Code: "+response.code());
                    System.out.println("Body: "+response.body());


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }
    public void verificarFreteIfood(APIListener<RespostaDisponibilidadeDeEntrega> listener, Endereco endereco){
        String idLoja = context.getString(R.string.idLoja);
      /*  Double latitude = -9.826368351303818;
        Double longitude = -67.95070159938493;
        endereco.setLatitude(latitude);
        endereco.setLongitude(longitude);*/
        Call<RespostaDisponibilidadeDeEntrega> call = service.verificarFreteIfood(idLoja,endereco.getLatitude(),endereco.getLongitude());
        call.enqueue(new Callback<RespostaDisponibilidadeDeEntrega>() {
            @Override
            public void onResponse(Call<RespostaDisponibilidadeDeEntrega> call, Response<RespostaDisponibilidadeDeEntrega> response) {
              if(response.code() == 200) {
                  listener.onSuccess(response.body());


            //  }else if(response.code() == 401){
              //    listener.onFailures("token expirado");
              }else{ //parei no error que ta dando para saber o frete
                  System.out.println("frete "+response.code());
                //  listener.onFailures("Entrega Fácil indisponível: o endereço da entrega está a mais de 10 km da  loja.");
                //  System.out.println("Entrega Fácil indisponível: o endereço da entrega está a mais de 10 km da  loja.");
                  try {
                     String json = response.errorBody().string();
                      Gson gson = new GsonBuilder().create();
                      Error obj = gson.fromJson(json, Error.class);
                      listener.onFailures(obj.getMensagem() + " Tente novamente");
                  } catch (IOException | IllegalStateException e) {
                      e.printStackTrace();
                  }

              }

            }

            @Override
            public void onFailure(Call<RespostaDisponibilidadeDeEntrega> call, Throwable t) {
                listener.onFailures(t.getMessage());


            }
        });


    }
    public void criarPedidoIfood(APIListener<RespostaPedido> listener,LayoutEnvioPedido layoutPedido){
       // carregarDadosPedido();
     /*   LayoutEnvioPedido layoutPedido = new LayoutEnvioPedido();
        layoutPedido.setCliente(cliente);
        layoutPedido.setEntrega(entrega);
        layoutPedido.setItens(itens);*/

        String idLoja = context.getString(R.string.idLoja);
        Call<RespostaPedido> call = service.criarPedidoIfood(idLoja,layoutPedido);
        call.enqueue(new Callback<RespostaPedido>() {
            @Override
            public void onResponse(Call<RespostaPedido> call, Response<RespostaPedido> response) {
                if(response.isSuccessful()){
                    System.out.println("resposta: "+response.code());
                    listener.onSuccess(response.body());

                }else{

                    System.out.println("apkdoandroid: mensagem de error "+response.errorBody());
                    System.out.println("apkdoandroid: code "+response.code());

                    try {
                        String json = response.errorBody().string();
                        Gson gson = new GsonBuilder().create();
                        Error obj = gson.fromJson(json, Error.class);
                        listener.onFailures(obj.getMensagem() + "Tente novamente");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<RespostaPedido> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });
    }


    public void salvarEventos(APIListener<Dados> listener, Long idPedido, String linkAcompanhamento, String idEvento, String idPedidoIfood, String idEntregador,
                              String status,
                              String data_create,
                              String nomeEntregador,
                              String telefoneEntregador,
                              String veiculoEntregador
                              ){

        Call<Dados> call = apiService.salvarEventosIfood(
                idEvento,
                idPedidoIfood,
                idPedido,
                idEntregador,
                status,
                data_create,
                nomeEntregador,
                telefoneEntregador,
                veiculoEntregador,
                linkAcompanhamento
        );

        call.enqueue(new Callback<Dados>() {
            @Override
            public void onResponse(Call<Dados> call, Response<Dados> response) {

                listener.onSuccess(response.body());

            }

            @Override
            public void onFailure(Call<Dados> call, Throwable t) {
                listener.onFailures(t.getMessage().toString());

            }
        });

    }


    public void getEventosIfood(APIListener<Dados> listener, Long idPedido){
        Call<Dados> call = apiService.getEventosIfood(idPedido);
        call.enqueue(new Callback<Dados>() {
            @Override
            public void onResponse(Call<Dados> call, Response<Dados> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Dados> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });


    }


    private void carregarDadosPedido(){
        telefone.setCodigoDoPais("55");
        telefone.setCodigoDeArea("91");
        telefone.setNumero("996017676");

        coordenadas.setLatitude(-9.822159);
        coordenadas.setLongitude(-67.948475);

        cliente.setNome("Elanilson");
        cliente.setTelefone(telefone);
        enderecoDeEntrega.setBairro("Sacramenta");
        enderecoDeEntrega.setCidade("Belém");
        enderecoDeEntrega.setComplemento("");
        enderecoDeEntrega.setEstado("PA");
        enderecoDeEntrega.setCodigo_postal("66120300");
        enderecoDeEntrega.setPais("BR");
        enderecoDeEntrega.setNome_da_rua("Santa maria");
        enderecoDeEntrega.setNumero_da_casa("526");
        enderecoDeEntrega.setReferencia("");
        enderecoDeEntrega.setCoordenadas(coordenadas);

        entrega.setTaxa_do_comerciante(10f);
        entrega.setEnderecoDeEntrega(enderecoDeEntrega);

        itens.add(new Item(UUID.randomUUID().toString(),"Batata",123L,2,1f,2f,2f));
        itens.add(new Item(UUID.randomUUID().toString(),"Arroz",124L,2,1f,2f,1f));
        itens.add(new Item(UUID.randomUUID().toString(),"Carne",125L,1,1f,1f,1f));
        itens.add(new Item(UUID.randomUUID().toString(),"Aveia",126L,1,1f,1f,1f));


    }



}
