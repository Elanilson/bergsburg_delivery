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
import com.bergburg.bergburgdelivery.repositorio.ProdutoRepositorio;

import java.util.List;

public class GerencialProdutosViewModel extends AndroidViewModel {

    private ProdutoRepositorio produtoRepositorio ;

    private MutableLiveData<List<Produto>> _Produtos = new MutableLiveData<>();
    public LiveData<List<Produto>> produtos = _Produtos;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public GerencialProdutosViewModel(@NonNull Application application) {
        super(application);
        produtoRepositorio = new ProdutoRepositorio(application.getBaseContext());

    }

    public void getProdutos(){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Produtos.setValue(result.getProdutos());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };
        produtoRepositorio.getProdutos(listener);

    }

    public void pesquisarProduto(String texto){

        if(texto != null && !texto.isEmpty() && !texto.equalsIgnoreCase(" ")){
            APIListener<Dados> listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                    _Produtos.setValue(result.getProdutos());
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));
                }
            };
            produtoRepositorio.pesquisarProduto(listener,texto);
        }else{
            getProdutos();
            //_Resposta.setValue(new Resposta("Preencha o campo corretamente!"));
        }


    }
}
