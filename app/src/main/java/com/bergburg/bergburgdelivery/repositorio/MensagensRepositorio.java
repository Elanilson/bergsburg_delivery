package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MensagensRepositorio {
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);
    private Context context;

    public MensagensRepositorio(Context context) {
        this.context = context;
    }

    public void getMensagens(APIListener<Dados> listener, Long idUsuario){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getMensagens(idUsuario);
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
                    //    listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void enviarMensagem(APIListener<Dados> listener, Long idUsuario,String texto , Long idConversa){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.enviarMensagem(idUsuario, texto, idConversa);
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
                listener.onFailures("Verifique sua conexão");
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void visualizarMensagem(APIListener<Dados> listener, Long idMensagem){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.visualizarMensagem(idMensagem);
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
                     //   listener.onFailures(Constantes.INSTABILIDADE);
                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }


}
