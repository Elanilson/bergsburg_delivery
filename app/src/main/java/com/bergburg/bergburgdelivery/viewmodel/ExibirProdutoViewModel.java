package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.PedidoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.ProdutoRepositorio;

import java.util.List;

public class ExibirProdutoViewModel extends AndroidViewModel {
    protected PedidoRepositorio pedidoRepositorio ;
    private ProdutoRepositorio produtoRepositorio ;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private MutableLiveData<Produto> _Produto = new MutableLiveData<>();
    public LiveData<Produto> produto = _Produto;

    public ExibirProdutoViewModel(@NonNull Application application) {
        super(application);
        produtoRepositorio = new ProdutoRepositorio(application.getBaseContext());
        pedidoRepositorio = new PedidoRepositorio(application.getBaseContext());
    }


    public  void buscarProduto(Long idProduto){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getProduto() != null){
                    _Produto.setValue(result.getProduto());
                }else{
                    _Resposta.setValue(new Resposta("nullo "+result.getError()));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        produtoRepositorio.buscarProduto(listener,idProduto);

    }
    public  void adicionarItemSacola(Long idSacola,Long idProduto,Float preco,int quantidade,String obsevacao){
       // System.out.println("");

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                    _Resposta.setValue(new Resposta("Adicionado",true));
                }else{
                    _Resposta.setValue(new Resposta(result.getError()));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        pedidoRepositorio.adicionarItemSacola(listener,idSacola,idProduto,preco,quantidade,obsevacao);

    }

    public  void atualizarQuantidadeItemSacola(Long idSacola,Long idProduto,int quantidade){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                   // _Resposta.setValue(new Resposta("Adicionado",true));
                }else{
                    _Resposta.setValue(new Resposta(result.getError()));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        pedidoRepositorio.atualizarQuantidadeItemSacola(listener,idSacola,idProduto,quantidade);

    }
}
