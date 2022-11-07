package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Status_pedido;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.EnderecoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.PedidoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.UsuarioRepositorio;

import java.util.List;

public class ExibirPedidoViewModel extends AndroidViewModel {
    private PedidoRepositorio repositorio ;
    private UsuarioRepositorio usuarioRepositorio ;
    private EnderecoRepositorio enderecoRepositorio;

    private MutableLiveData<Endereco> _Endereco = new MutableLiveData<>();
    public LiveData<Endereco> endereco = _Endereco;

    private MutableLiveData<Usuario> _Usuario = new MutableLiveData<>();
    public LiveData<Usuario> usuario = _Usuario;

    private MutableLiveData<List<Pedido>> _Pedidos = new MutableLiveData<>();
    public LiveData<List<Pedido>> pedidos = _Pedidos;

    private MutableLiveData<Pedido> _Pedido = new MutableLiveData<>();
    public LiveData<Pedido> pedido = _Pedido;

    private MutableLiveData<List<ItensPedido>> _ItensPedido = new MutableLiveData<>();
    public LiveData<List<ItensPedido>> itensPedido = _ItensPedido;

    private MutableLiveData<List<Status_pedido>> _StatusPedido = new MutableLiveData<>();
    public LiveData<List<Status_pedido>> statusPedido = _StatusPedido;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public ExibirPedidoViewModel(@NonNull Application application) {
        super(application);
        usuarioRepositorio = new UsuarioRepositorio(application.getBaseContext());
        repositorio = new PedidoRepositorio(application.getBaseContext());
        enderecoRepositorio = new EnderecoRepositorio(application.getBaseContext());
    }

    public  void listarPedido(Long idPedido){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Pedido.setValue(result.getPedido());
            }
            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };

        repositorio.listarPedido(listener,idPedido);

    }

    public  void getItensPedido(Long idPedido){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _ItensPedido.setValue(result.getItensPedido());
            }
            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };
        repositorio.listarItensPedidos(listener,idPedido);
    }

    public  void getStatusPedido(Long idPedido){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _StatusPedido.setValue(result.getStatus_pedido());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };
        repositorio.listarStatusPedidos(listener,idPedido);

    }

    public  void getUsuario(Long idUsuario){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Usuario.setValue(result.getUsuario());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };
        usuarioRepositorio.getUsuario(listener,idUsuario);

    }

    public void buscarEnderecoSalvo(Long idUsuario){
        APIListener<Dados>  listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Endereco.setValue(result.getEndereco());
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        enderecoRepositorio.buscarEnderecoSalvo(listener,idUsuario);
    }
}
