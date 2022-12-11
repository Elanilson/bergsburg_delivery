package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.ifood.Cliente;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.Coordenadas;
import com.bergburg.bergburgdelivery.ifood.model.EnderecoDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.Entrega;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.Item;
import com.bergburg.bergburgdelivery.ifood.model.LayoutEnvioPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.ifood.model.Telefone;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClientIFood;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.IfoodService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IfoodRepositorio {
    private Context context;
    private IfoodService service = RetrofitClientIFood.classService(IfoodService.class);
    private  String  grantType ;
    private  String  clientId ;
    private  String  clientSecret ;
    private Cliente cliente = new Cliente();
    private EnderecoDeEntrega enderecoDeEntrega = new EnderecoDeEntrega();
    private Entrega entrega = new Entrega();
    private Coordenadas coordenadas = new Coordenadas();
    private Telefone telefone = new Telefone();
    private List<Item> itens=  new ArrayList<>();

    public IfoodRepositorio(Context context) {
        this.context = context;

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
                        listener.onFailures(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace(); //melhorar
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
                        listener.onFailures(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace(); //melhorar
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

    public void reconhecerLimparEnventos(APIListener<Boolean> listener, List<EventoPedido> eventoPedidos){
        Call<Void> call = service.reconhecerLimparEnventos(eventoPedidos);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    if(response.code() == 202){
                        listener.onSuccess(true);
                    }else{
                        listener.onSuccess(false);
                    }
                    System.out.println("Code: "+response.code());
                    System.out.println("Body: "+response.body());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailures(t.getMessage());
            }
        });

    }

    public void verificarFreteIfood(APIListener<RespostaDisponibilidadeDeEntrega> listener){
        String idLoja = context.getString(R.string.idLoja);
        float latitude = -9.826368351303818f;
        float longitude = -67.95070159938493f;
        Call<RespostaDisponibilidadeDeEntrega> call = service.verificarFreteIfood(idLoja,latitude,longitude);
        call.enqueue(new Callback<RespostaDisponibilidadeDeEntrega>() {
            @Override
            public void onResponse(Call<RespostaDisponibilidadeDeEntrega> call, Response<RespostaDisponibilidadeDeEntrega> response) {
              if(response.isSuccessful()){
                  listener.onSuccess(response.body());
              }
                System.out.println("frete "+response.body());
                System.out.println("frete "+response.errorBody());

            }

            @Override
            public void onFailure(Call<RespostaDisponibilidadeDeEntrega> call, Throwable t) {
                listener.onFailures(t.getMessage());

                System.out.println("frete er");

            }
        });


    }
    public void criarPedidoIfood(APIListener<RespostaPedido> listener){
        carregarDadosPedido();
        String idLoja = context.getString(R.string.idLoja);
        LayoutEnvioPedido layoutPedido = new LayoutEnvioPedido();
        layoutPedido.setCliente(cliente);
        layoutPedido.setEntrega(entrega);
        layoutPedido.setItens(itens);
        Call<RespostaPedido> call = service.criarPedidoIfood(idLoja,layoutPedido);

        call.enqueue(new Callback<RespostaPedido>() {
            @Override
            public void onResponse(Call<RespostaPedido> call, Response<RespostaPedido> response) {
                if(response.isSuccessful()){
                    System.out.println("resposta: "+response.code());
                    listener.onSuccess(response.body());

                }else{
                    System.out.println("apkdoandroid: "+response.errorBody());
                    try {
                        listener.onFailures(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace(); //melhorar
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaPedido> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });
    }

    private void carregarDadosPedido(){
        telefone.setCodigoDoPais("55");
        telefone.setCodigoDeArea("91");
        telefone.setNumero("996017676");

        coordenadas.setLatitude(-9.822159f);
        coordenadas.setLongitude(-67.948475f);

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
