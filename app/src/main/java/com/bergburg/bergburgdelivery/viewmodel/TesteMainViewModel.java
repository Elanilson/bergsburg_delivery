package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.PedidoResposta;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.IfoodRepositorio;

import java.util.List;

public class TesteMainViewModel extends AndroidViewModel {
    private IfoodRepositorio repositorio ;

    private MutableLiveData<Autenticacao> _Autenticacao = new MutableLiveData<>();
    public LiveData<Autenticacao> autenticacao = _Autenticacao;

    private MutableLiveData<List<PedidoResposta>> _PedidoResposta = new MutableLiveData<>();
    public LiveData<List<PedidoResposta>> pedidoResposta = _PedidoResposta;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public TesteMainViewModel(@NonNull Application application) {
        super(application);
        repositorio = new IfoodRepositorio(application.getApplicationContext());
    }

    public void autenticar(){
        APIListener<Autenticacao> listener = new APIListener<Autenticacao>() {
            @Override
            public void onSuccess(Autenticacao result) {
                _Autenticacao.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.autenticar(listener);

    }

    public void verificarEvento(){
        APIListener<List<PedidoResposta>> listener = new APIListener<List<PedidoResposta>>() {
            @Override
            public void onSuccess(List<PedidoResposta> result) {
                _PedidoResposta.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.verificarEventos(listener);

    }
}
