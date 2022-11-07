package com.bergburg.bergburgdelivery.repositorio;

import android.app.Dialog;
import android.content.Context;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
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

public class UsuarioRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public UsuarioRepositorio(Context context) {
        this.context = context;
    }

    public void login (APIListener<Dados> listener, String login, String senha){
        try{

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        if(VerificadorDeConexao.isConnectionAvailable(context)) {

            Call<Dados> call = remoto.login(login, senha);
            call.enqueue(new Callback<Dados>() {
                @Override
                public void onResponse(Call<Dados> call, Response<Dados> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccess(response.body());
                    } else {
                        listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                    }
                }

                @Override
                public void onFailure(Call<Dados> call, Throwable t) {
                    System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                }
            });
        }else{
           // listener.onFailures(Constantes.SEM_INTERNET);
        }

    }
    public void enviarEmailDeRecuperacao (APIListener<Dados> listener, String email){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.enviarEmailDeRecuperacao(email);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);
                        System.out.println("Erro UsuarioRepositorio - "+t.getLocalizedMessage());


                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
    public void verificarUsuarioLogado (APIListener<Dados> listener, long id){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.verificarUsuarioLogado(id);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);
                        System.out.println("Erro UsuarioRepositorio - "+t.getLocalizedMessage());


                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
    public void deslogar (APIListener<Dados> listener, long id){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.deslogar(id);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void alterarSenha (APIListener<Dados> listener, String email,String senha){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.alterarSenha(email,senha);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void alterarDadosUsuario (APIListener<Dados> listener, String email,String nome,String telefone){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.alterarDadosUsuario(email,nome,telefone);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void getUsuario (APIListener<Dados> listener, long id){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getUsuario(id);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void enviarEmailDeConfirmacao (APIListener<Dados> listener, String email){
        System.out.println("Email "+email);
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.enviarEmailDeConfirmacao(email);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

                    }
                });
            }else{
               // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void salvarUsuario (APIListener<Dados> listener, Usuario usuario,Endereco endereco){
        System.out.println(usuario.toString());
        System.out.println(endereco.toString());
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.salvarUsuario(usuario.getNome(),
                        usuario.getSenha(),
                        usuario.getEmail(),
                        usuario.getTelefone(),

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
                            listener.onFailures("cod: " + response.code() +"\n "+Constantes.INSTABILIDADE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dados> call, Throwable t) {
                        System.out.println("Error: "+t.getMessage());
                        listener.onFailures(Constantes.INSTABILIDADE);

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
