package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Categoria;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.CategoriaRepositorio;
import com.bergburg.bergburgdelivery.repositorio.EstabelicimentoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.ProdutoRepositorio;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private CategoriaRepositorio categoriaRepositorio  ;
    private ProdutoRepositorio produtoRepositorio ;
    private EstabelicimentoRepositorio estabelicimentoRepositorio;

    private MutableLiveData<List<Categoria>> _Categorias = new MutableLiveData<>();
    public LiveData<List<Categoria>> categorias = _Categorias;

    private MutableLiveData<List<Produto>> _Entradas = new MutableLiveData<>();
    public LiveData<List<Produto>> entradas = _Entradas;

    private MutableLiveData<List<Produto>> _Produtos = new MutableLiveData<>();
    public LiveData<List<Produto>> produtos = _Produtos;

    private MutableLiveData<Estabelicimento> _Estabelicimento = new MutableLiveData<>();
    public LiveData<Estabelicimento> estabelicimento = _Estabelicimento;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        produtoRepositorio = new ProdutoRepositorio(application.getBaseContext());
        categoriaRepositorio = new CategoriaRepositorio(application.getBaseContext());
        estabelicimentoRepositorio = new EstabelicimentoRepositorio(application.getBaseContext());
    }

    public void getCategorias(){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Categorias.setValue(result.getCategorias());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        categoriaRepositorio.getCategorias(listener);
    }

    public void getEstabelicimento(){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Estabelicimento.setValue(result.getEstabelicimento());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        estabelicimentoRepositorio.getEstabelicimento(listener);
    }

    public void produtosPopulares(){
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
        produtoRepositorio.produtosPopulares(listener);

    }
}
