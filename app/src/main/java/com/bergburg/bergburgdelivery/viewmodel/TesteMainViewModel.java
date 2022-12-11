package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.IfoodRepositorio;

import java.util.ArrayList;
import java.util.List;

public class TesteMainViewModel extends AndroidViewModel {
    private IfoodRepositorio repositorio ;

    private MutableLiveData<Autenticacao> _Autenticacao = new MutableLiveData<>();
    public LiveData<Autenticacao> autenticacao = _Autenticacao;
    private MutableLiveData<RespostaDisponibilidadeDeEntrega> _FreteIfood = new MutableLiveData<>();
    public LiveData<RespostaDisponibilidadeDeEntrega> freteIfood = _FreteIfood;

    private MutableLiveData<List<EventoPedido>> _EventosPedido = new MutableLiveData<>();
    public LiveData<List<EventoPedido>> eventoPedido = _EventosPedido;



    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private MutableLiveData<RespostaPedido> _RespostaPedido = new MutableLiveData<>();
    public LiveData<RespostaPedido> respostaPedido = _RespostaPedido;

    private List<EventoPedido> listaTemporaria = new ArrayList<>();

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
        APIListener<List<EventoPedido>> listener = new APIListener<List<EventoPedido>>() {
            @Override
            public void onSuccess(List<EventoPedido> result) {

                    if(result != null){
                        listaTemporaria.addAll(result);
                    }else{
                        System.out.println("Sem eventos");
                    }

                _EventosPedido.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.verificarEventos(listener);
    }

    public void reconhecerLimparEnventos(){
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Sucesso ao limpar eventos",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.reconhecerLimparEnventos(listener,listaTemporaria);
    }

    public void criarPedidoIfood(){
        APIListener<RespostaPedido> listener = new APIListener<RespostaPedido>() {
            @Override
            public void onSuccess(RespostaPedido result) {

                _RespostaPedido.setValue(result);

               // _Resposta.setValue(new Resposta("Sucesso ao limpar eventos",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.criarPedidoIfood(listener);
    }

    public void verificarFreteIfood(){
        APIListener<RespostaDisponibilidadeDeEntrega> listener = new APIListener<RespostaDisponibilidadeDeEntrega>() {
            @Override
            public void onSuccess(RespostaDisponibilidadeDeEntrega result) {

                _FreteIfood.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.verificarFreteIfood(listener);
    }

}
