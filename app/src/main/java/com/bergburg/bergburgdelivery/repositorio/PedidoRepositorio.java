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

public class PedidoRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public PedidoRepositorio(Context context) {
        this.context = context;
    }



    public void criarPedido(APIListener<Dados> listener,String formaDePagamento, Long idUsuario, Long idSacola, String opcaoEntrega, Float total, Float taxa_entrega, Float subTotal){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.criarPedido(formaDePagamento,idUsuario, idSacola, opcaoEntrega, total, taxa_entrega, subTotal);
                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus()) {
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
              // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void listarItensSacola(APIListener<Dados> listener,Long idSacola){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getItensSacola(idSacola);
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
              // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }

    public void deletarItemDaSacola(APIListener<Dados> listener,Long idItem){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.deletarItemDaSacola(idItem);
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

    public void listarPedidos(APIListener<Dados> listener,Long idUsuario){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.listarPedidos(idUsuario);
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
    public void listarPedido(APIListener<Dados> listener,Long idPedido){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.listarPedido(idPedido);

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

    public void listarItensPedidos(APIListener<Dados> listener,Long idPedido){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getItensPedido(idPedido);

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

    public void visualizarPedido(APIListener<Dados> listener,Long idPedido){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.visualizarPedido(idPedido);

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

    public void listarStatusPedidos(APIListener<Dados> listener,Long idPedido){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getStatusPedido(idPedido);

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

    public void salvarStatusPedido(APIListener<Dados> listener,Long idPedido,String status){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {

                        Call<Dados> call = remoto.salvarStatusPedido(idPedido,status);

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

    public void adicionarItemSacola(APIListener<Dados> listener,Long idSacola,Long idProduto,Float preco,int quantidade,String obsevacao){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.adicionarItemSacola(idSacola, idProduto, quantidade, preco, obsevacao);

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

    public void atualizarQuantidadeItemSacola(APIListener<Dados> listener,Long idSacola,Long idProduto,int quantidade){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.atualizarQuantidadeItemSacola(idSacola, idProduto, quantidade);

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
              // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
    public void atualizarObservacaoItemSacola(APIListener<Dados> listener,Long idSacola,Long idProduto,String observacao){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.atualizarObservacaoItemSacola(idSacola, idProduto, observacao);

                call.enqueue(new Callback<Dados>() {
                    @Override
                    public void onResponse(Call<Dados> call, Response<Dados> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus()) {
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
              // listener.onFailures(Constantes.SEM_INTERNET);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }


}
