package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnderecoRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public EnderecoRepositorio(Context context) {
        this.context = context;
    }

    public void buscarEnderecoSalvo(APIListener<Dados> listener,Long idUsuario){
        try{
            if(idUsuario != 0){
                if(VerificadorDeConexao.isConnectionAvailable(context)) {
                    Call<Dados> call = remoto.buscarEnderecoSalvo(idUsuario);
                    call.enqueue(new Callback<Dados>() {
                        @Override
                        public void onResponse(Call<Dados> call, Response<Dados> response) {
                            if (response.code() == 200) {
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
                    //listener.onFailures(Constantes.SEM_INTERNET);
                }
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }



    }

    public void atualizarEnderecov2 (APIListener<Dados> listener, Usuario usuario, Endereco endereco){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                System.out.println(usuario.toString());
                System.out.println(endereco.toString());
                Call<Dados> call = remoto.atualizarEnderecov2(usuario.getId(),
                        endereco.getRua(),
                        endereco.getBairro(),
                        endereco.getCep(),
                        endereco.getNumeroCasa(),
                        endereco.getLatitude(),
                        endereco.getLongitude(),
                        endereco.getComplemento()
                );
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getError().isEmpty()) {
                                listener.onSuccess(response.body());
                            } else {

                                listener.onFailures(response.body().getError());
                            }
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
                //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }


    }

    public void salvarEndereco (APIListener<Dados> listener, Usuario usuario, Endereco endereco){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                System.out.println("Usuario: " + usuario.toString());
                System.out.println("endereco: " + endereco.toString());
                Call<Dados> call = remoto.salvarEndereco(usuario.getId(),
                        endereco.getRua(),
                        endereco.getBairro(),
                        endereco.getCep(),
                        endereco.getCidade(),
                        endereco.getEstado(),
                        endereco.getNumeroCasa(),
                        endereco.getLatitude(),
                        endereco.getLongitude(),
                        endereco.getComplemento()
                );
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getError().isEmpty()) {
                                listener.onSuccess(response.body());
                            } else {

                                listener.onFailures(response.body().getError());
                            }
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
                //listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }


    }
}
