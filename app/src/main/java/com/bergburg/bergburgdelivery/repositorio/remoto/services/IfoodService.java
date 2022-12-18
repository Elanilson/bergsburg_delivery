package com.bergburg.bergburgdelivery.repositorio.remoto.services;

import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.DetalhesPedido;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.LayoutEnvioPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.viewmodel.CancelamentoDePedido;

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

    @POST("order/v1.0/orders/{idPedido}/confirm")
    Call<Void> confirmarPedido(
            @Path(value = "idPedido" , encoded = true) String idPedido
    );

    @POST("order/v1.0/orders/{idPedido}/requestCancellation")
    Call<Void> cancelarPepdido(
            @Path(value = "idPedido" , encoded = true) String idPedido,
            @Body CancelamentoDePedido cancelamentoDePedido
    );

    @POST("order/v1.0/orders/{idPedido}/acceptCancellation")
    Call<Void> aceitarPedidoDeCanelamento(
            @Path(value = "idPedido" , encoded = true) String idPedido
    );

    @POST("order/v1.0/orders/{idPedido}/denyCancellation")
    Call<Void> negarPedidoDeCanelamento(
            @Path(value = "idPedido" , encoded = true) String idPedido
    );

    @GET("shipping/v1.0/merchants/{idLoja}/deliveryAvailabilities")
    Call<RespostaDisponibilidadeDeEntrega> verificarFreteIfood(
            @Path(value = "idLoja" , encoded = true) String idLoja,
            @Query(value = "latitude", encoded = true) Double latitude,
            @Query(value ="longitude", encoded = true) Double longitude
    );

    @GET("order/v1.0/orders/{idPedido}")
    Call<DetalhesPedido> buscarDetalhesDoPedido(
            @Path(value = "idPedido" , encoded = true) String idPedido
    );


    @POST("shipping/v1.0/merchants/{idLoja}/orders")
    Call<RespostaPedido> criarPedidoIfood(
            @Path(value = "idLoja" , encoded = true) String idLoja,
            @Body LayoutEnvioPedido layoutEnvioPedido
    );

    @GET("order/v1.0/events:polling")
    Call<List<EventoPedido>> verificarEventos();


}
