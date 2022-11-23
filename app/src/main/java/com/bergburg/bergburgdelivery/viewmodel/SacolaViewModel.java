package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.ItensSacola;
import com.bergburg.bergburgdelivery.model.Pedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.EnderecoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.EstabelicimentoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.PedidoRepositorio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SacolaViewModel extends AndroidViewModel {
    private PedidoRepositorio pedidoRepositorio  ;
    private EnderecoRepositorio enderecoRepositorio  ;
    private EstabelicimentoRepositorio estabelicimentoRepositorio;
    private MutableLiveData<Pedido> _Pedido = new MutableLiveData<>();
    public LiveData<Pedido> pedido = _Pedido;

    private MutableLiveData<Endereco> _Endereco = new MutableLiveData<>();
    public LiveData<Endereco> endereco = _Endereco;

    private MutableLiveData<List<ItensSacola>> _ItensSacola = new MutableLiveData<>();
    public LiveData<List<ItensSacola>> itensSacola = _ItensSacola;

    private MutableLiveData<Estabelicimento> _Estabelicimento = new MutableLiveData<>();
    public LiveData<Estabelicimento> estabelicimento = _Estabelicimento;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private MutableLiveData<Resposta> _RespostaPedidoEnviado = new MutableLiveData<>();
    public LiveData<Resposta> respostaPedidoEnviado = _RespostaPedidoEnviado;

    public SacolaViewModel(@NonNull Application application) {
        super(application);
        pedidoRepositorio = new PedidoRepositorio(application.getBaseContext());
        enderecoRepositorio = new EnderecoRepositorio(application.getBaseContext());
        estabelicimentoRepositorio = new EstabelicimentoRepositorio(application.getBaseContext());
    }

    public  void criarPedido(String formaDePagamento,Long idUsuario,Long idSacola,String opcaoEntrega,Float total,Float taxa_entrega,Float subTotal){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _RespostaPedidoEnviado.setValue(new Resposta("Pedido enviado",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _RespostaPedidoEnviado.setValue(new Resposta(mensagem));


            }
        };

        pedidoRepositorio.criarPedido(listener,formaDePagamento,idUsuario,idSacola,opcaoEntrega,total,taxa_entrega, subTotal);

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

    public  void deletarItemDaSacola(Long idItem){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                    _Resposta.setValue(new Resposta("Removido",true));
                }else{
                    _Resposta.setValue(new Resposta("Não foi possível remover o item, tente novamente mais tarde"));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        pedidoRepositorio.deletarItemDaSacola(listener,idItem);

    }
    public  void getItensSacola(Long idSacola){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
               // if(result.getItensSacola() != null){
                  //  _Resposta.setValue(new Resposta("Sucesso",true));
                    _ItensSacola.setValue(result.getItensSacola());
                //}
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        pedidoRepositorio.listarItensSacola(listener,idSacola);

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
    public  void atualizarObservacaoItemSacola(Long idSacola,Long idProduto,String observacao){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                     _Resposta.setValue(new Resposta("Atualizado",true));
                }else{
                    _Resposta.setValue(new Resposta(result.getError()));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        pedidoRepositorio.atualizarObservacaoItemSacola(listener,idSacola,idProduto,observacao);

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

}
