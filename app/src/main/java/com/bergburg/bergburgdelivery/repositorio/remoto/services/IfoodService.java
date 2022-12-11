package com.bergburg.bergburgdelivery.repositorio.remoto.services;

import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.LayoutEnvioPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IfoodService {
    @POST("authentication/v1.0/oauth/token")
    @FormUrlEncoded
    Call<Autenticacao> autenticar(
            @Field("grantType") String grantType,
            @Field("clientId") String clientId,
            @Field("clientSecret") String clientSecret
    );

    @POST("order/v1.0/events/acknowledgment")
    Call<Void> reconhecerLimparEnventos(
            @Body List<EventoPedido> eventoPedidos
    );

    @GET("shipping/v1.0/merchants/{idLoja}/deliveryAvailabilities")
    Call<RespostaDisponibilidadeDeEntrega> verificarFreteIfood(
            @Path(value = "idLoja" , encoded = true) String idLoja,
            @Query(value = "latitude", encoded = true) float latitude,
            @Query(value ="longitude", encoded = true) float longitude
    );

    @POST("shipping/v1.0/merchants/{idLoja}/orders")
    Call<RespostaPedido> criarPedidoIfood(
            @Path(value = "idLoja" , encoded = true) String idLoja,
            @Body LayoutEnvioPedido layoutEnvioPedido
    );

    @GET("order/v1.0/events:polling")
    Call<List<EventoPedido>> verificarEventos();


}
