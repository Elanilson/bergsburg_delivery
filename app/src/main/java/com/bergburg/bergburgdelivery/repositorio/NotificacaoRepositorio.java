package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.DataNotificacao;
import com.bergburg.bergburgdelivery.model.Notificacao;
import com.bergburg.bergburgdelivery.model.NotificacaoDados;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClientNotificacao;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.NotificacaoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacaoRepositorio {
    NotificacaoService services = RetrofitClientNotificacao.classService(NotificacaoService.class);
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);
    private Context context;

    public NotificacaoRepositorio(Context context) {
        this.context = context;
    }
    public void getToken(APIListener<Dados> listener, Long idUsuario){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getToken(idUsuario);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures(Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                     //     listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
                 //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void enviarToken(APIListener<Dados> listener, Long idUsuario, String token){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.salvarToken(idUsuario, token);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures(Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        //  listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
                // //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void enviarTokenRefrshIFood(APIListener<Dados> listener,String token){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.salvarTokenRefreshIfood(token);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures(Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        //  listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
                // //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
    public void getTokenRefrshIFood(APIListener<Dados> listener){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getTokenRefreshiFood();
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures(Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        //     listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
                //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void enviarNotificacao(APIListener<NotificacaoDados> listener,String token,String titulo, String mensagem,Long idUsuario,Long idConversa){

        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                if(token != null && titulo != null && mensagem != null){
                    if(!token.isEmpty() && !titulo.isEmpty() && !mensagem.isEmpty()){
                        Notificacao notificacao = new Notificacao(titulo,mensagem);
                        NotificacaoDados notificacaoDados = new NotificacaoDados(token,notificacao,new DataNotificacao(idUsuario,idConversa));
                        Call<NotificacaoDados> call = services.enviarNotificacao(notificacaoDados);
                        call.enqueue(new Callback<NotificacaoDados>() {
                            @Override
                            public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {
                                if (response.isSuccessful()) {
                                    listener.onSuccess(response.body());
                                    System.out.println("enviar a notificação");
                                } else {
                                    System.out.println("Não foi possível enviar a notificação");
                                    listener.onFailures("cod: " + response.code() +"\n "+ response.message());
                                }
                                System.out.println(response.code()+" - "+response.message());
                            }

                            @Override
                            public void onFailure(Call<NotificacaoDados> call, Throwable t) {
                                System.out.println("Error: "+t.getMessage());
                               //   listener.onFailures(Constantes.INSTABILIDADE);
                            }
                        });

                    }else{
                        listener.onFailures(Constantes.INSTABILIDADE+" -1");
                    }

                }else{
                    listener.onFailures("não recebir os dados "+mensagem+" - "+titulo+" - "+" - "+token);
                }

            }else{
                // //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
