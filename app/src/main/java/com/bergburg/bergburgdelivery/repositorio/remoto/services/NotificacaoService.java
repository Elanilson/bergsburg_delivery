package com.bergburg.bergburgdelivery.repositorio.remoto.services;


import com.bergburg.bergburgdelivery.model.NotificacaoDados;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificacaoService {
    @Headers({"Authorization: key=AAAAER39Big:APA91bH_EAk44J0-GUrm9sGbBjMYh8mst2-raYxzO37CjiyKl0G9HkPmunj7T_3kixezQ_bdTIxds3bHBGv150lOp5AgqGc_RRy2xFbII501hsJHxOBca27GMISkEgM5a53ITlNuSrSe ",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<NotificacaoDados>enviarNotificacao(@Body NotificacaoDados notificacaoDados);
}
