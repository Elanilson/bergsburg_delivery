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

public class CategoriaRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public CategoriaRepositorio(Context context) {
        this.context = context;
    }

    public void getCategorias(APIListener<Dados> listener){
        try{

            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getCategorias();
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        listener.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                       // listener.onFailures(Constantes.INSTABILIDADE);
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
