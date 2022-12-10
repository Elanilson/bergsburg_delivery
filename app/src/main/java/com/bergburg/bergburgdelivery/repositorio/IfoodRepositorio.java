package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.PedidoResposta;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClientIFood;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.IfoodService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IfoodRepositorio {
    private Context context;
    private IfoodService service = RetrofitClientIFood.classService(IfoodService.class);
    private  String  grantType ;
    private  String  clientId ;
    private  String  clientSecret ;

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

    public void verificarEventos(APIListener<List<PedidoResposta>> listener){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI1OGJmNmY4Yy02YmUxLTRiNTYtYjUxYi03MzYyMzUzMDhjOTkiLCJhdWQiOlsic2hpcHBpbmciLCJjYXRhbG9nIiwicmV2aWV3IiwiZmluYW5jaWFsIiwibWVyY2hhbnQiLCJvcmRlciIsIm9hdXRoLXNlcnZlciJdLCJhcHBfbmFtZSI6IjU4YmY2ZjhjLTZiZTEtNGI1Ni1iNTFiLTczNjIzNTMwOGM5OSIsIm93bmVyX25hbWUiOiIiLCJzY29wZSI6WyJtZXJjaGFudCIsInNoaXBwaW5nIiwiY2F0YWxvZyIsInJldmlldyIsIm9yZGVyIiwiY29uY2lsaWF0b3IiXSwiaXNzIjoiaUZvb2QiLCJtZXJjaGFudF9zY29wZSI6WyJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6bWVyY2hhbnQiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6Y2F0YWxvZyIsImRmNmIwNDhkLTM1NzAtNDI4Ny05MjhhLTk0NmM1ZDBkMjc0MjpyZXZpZXciLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6b3JkZXIiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6Y29uY2lsaWF0b3IiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6c2hpcHBpbmciXSwiZXhwIjoxNjcwNjk5MjExLCJpYXQiOjE2NzA2Nzc2MTEsImp0aSI6IjU4YmY2ZjhjLTZiZTEtNGI1Ni1iNTFiLTczNjIzNTMwOGM5OSIsIm1lcmNoYW50X3Njb3BlZCI6dHJ1ZSwiY2xpZW50X2lkIjoiNThiZjZmOGMtNmJlMS00YjU2LWI1MWItNzM2MjM1MzA4Yzk5In0.i5SnM4awjHqe9XiWtmTFIXiQCmUhZuAUks44h_EFYsJ3mqUibjwb_h8jIrsPVElQGsAzJZZBCoyVsYoWxFuO79lZUSwO-gp78bc0ORU4a9KO4qA6qvu4K90NpteJvABB3mXe1axYQfspPp0UYWJOuvSaBs9mKmzDD3ZC7pOBgNg";
        Call<List<PedidoResposta>> call = service.verificarEventos();
        call.enqueue(new Callback<List<PedidoResposta>>() {
            @Override
            public void onResponse(Call<List<PedidoResposta>> call, Response<List<PedidoResposta>> response) {
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
            public void onFailure(Call<List<PedidoResposta>> call, Throwable t) {
                listener.onFailures(t.getMessage());

            }
        });
    }
}
