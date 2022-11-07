package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.PedidoRepositorio;

import java.util.List;

public class PedidosViewModel extends AndroidViewModel {
    private PedidoRepositorio repositorio  ;

    private MutableLiveData<List<Pedido>> _Pedidos = new MutableLiveData<>();
    public LiveData<List<Pedido>> pedidos = _Pedidos;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public PedidosViewModel(@NonNull Application application) {
        super(application);
        repositorio =new PedidoRepositorio(application.getBaseContext());
    }

    public  void listarPedido(Long idUsuario){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Pedidos.setValue(result.getPedidos());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));


            }
        };

        repositorio.listarPedidos(listener,idUsuario);

    }
}
