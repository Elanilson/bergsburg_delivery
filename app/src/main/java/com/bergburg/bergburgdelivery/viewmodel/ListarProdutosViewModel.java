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

public class ListarProdutosViewModel extends AndroidViewModel {

    private ProdutoRepositorio repositorio;

    private MutableLiveData<List<Produto>> _Produtos = new MutableLiveData<>();
    public LiveData<List<Produto>> produtos = _Produtos;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public ListarProdutosViewModel(@NonNull Application application) {
        super(application);
        repositorio = new ProdutoRepositorio(application.getBaseContext());
    }

    public void listarProdutosPorCategoria(Long idCategoria){
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
        repositorio.produtosPorCategoria(listener,idCategoria);

    }


}
