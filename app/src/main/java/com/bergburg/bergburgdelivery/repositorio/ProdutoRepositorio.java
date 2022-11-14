package com.bergburg.bergburgdelivery.repositorio;

import android.content.Context;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.VerificadorDeConexao;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.repositorio.remoto.services.BergburgService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoRepositorio {
    private Context context;
    private BergburgService remoto = RetrofitClient.classService(BergburgService.class);

    public ProdutoRepositorio(Context context) {
        this.context = context;
    }
    public void produtosPorCategoria(APIListener<Dados> listener,Long idCategoria){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.produtosPorCategoria(idCategoria);
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

    public void produtosPopulares(APIListener<Dados> listener){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.produtosPopulares();
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

    public void getProdutos(APIListener<Dados> listener){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.getProdutos();
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

    public void pesquisarProduto(APIListener<Dados> listener,String texto){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.pesquisarProduto(texto);
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

    public void atualizarProduto(APIListener<Dados> listener, Produto produto){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.atualizarProduto(produto.getId(),produto.getTitulo(),produto.getDescricao(),produto.getPreco(),produto.getAtivo());
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

    public void buscarProduto(APIListener<Dados> listener,Long idProduto){
        try{
            if(VerificadorDeConexao.isConnectionAvailable(context)) {
                Call<Dados> call = remoto.buscarProduto(idProduto);
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
}
