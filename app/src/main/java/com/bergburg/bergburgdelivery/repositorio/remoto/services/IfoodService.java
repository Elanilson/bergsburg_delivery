package com.bergburg.bergburgdelivery.repositorio.remoto.services;

import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.PedidoResposta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IfoodService {
    @POST("authentication/v1.0/oauth/token")
    @FormUrlEncoded
    Call<Autenticacao> autenticar(
            @Field("grantType") String grantType,
            @Field("clientId") String clientId,
            @Field("clientSecret") String clientSecret
    );

    @GET("order/v1.0/events:polling")
    Call<List<PedidoResposta>> verificarEventos();
}
