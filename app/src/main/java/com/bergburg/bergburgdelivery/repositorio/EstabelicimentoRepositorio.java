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

public class EstabelicimentoRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public EstabelicimentoRepositorio(Context context) {
        this.context = context;
    }

    public void getEstabelicimento(APIListener<Dados> listener){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getEstabelicimento();
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
              //  listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }


    public void atualizarLoja(APIListener<Dados> listener, Long id, String nome,String status,String ramo,String endereco,String tempoEntrega,String valorMinimo, String telefone){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.atualizarLoja(id,nome,status,ramo,endereco,tempoEntrega,valorMinimo,telefone);
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
                //  listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void atualizarStatusLoja(APIListener<Dados> listener, Long id,String status){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.atualizarStatusLoja(id,status);
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
                //  listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
