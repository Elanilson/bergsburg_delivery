package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.NotificacaoDados;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Token;
import com.bergburg.bergburgdelivery.repositorio.ConversasRepositorio;
import com.bergburg.bergburgdelivery.repositorio.NotificacaoRepositorio;

public class MainViewModel extends AndroidViewModel {
    private NotificacaoRepositorio repositorio ;
    private ConversasRepositorio conversasRepositorio;

    private MutableLiveData<Token> _Token = new MutableLiveData<>();
    public LiveData<Token> token = _Token;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repositorio = new NotificacaoRepositorio(application.getBaseContext());
        conversasRepositorio = new ConversasRepositorio(application.getBaseContext());
    }

    public void enviarToken(Long idUsuario, String token ){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                    _Resposta.setValue(new Resposta(Constantes.ENVIADO,true));
                }else{
                    System.out.println("Não foi possível enviar o token");
                }

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.enviarToken(listener,idUsuario,token);
    }

    public void getToken(Long idUsuario ){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {

                if(result.getToken() != null){
                    if(result.getToken().getId() != null){
                        _Token.setValue(result.getToken());
                        System.out.println("Token recebido: "+result.getToken().getToken());
                    }
                }else{
                    _Token.setValue(new Token());
                    System.out.println("Token não recebido");
                }

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.getToken(listener,idUsuario);
    }

    public void getTokenRefreshIFood(){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {

                if(result.getToken() != null){
                    if(result.getToken().getId() != null){
                        _Token.setValue(result.getToken());
                        System.out.println("TokenRefresh recebido: "+result.getToken().getToken());
                    }
                }else{
                    _Token.setValue(new Token());
                    System.out.println("Token não recebido");
                }

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.getTokenRefrshIFood(listener);
    }

    public void enviarTokenRefreshIfood( String token ){
        if(token != null){
            if(!token.isEmpty()){
                APIListener<Dados> listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {

                        if(result.getStatus()){
                            System.out.println("token refrest enviado");
                        }else{
                            System.out.println("Não foi possível enviar o token");
                        }
                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));

                    }
                };
                repositorio.enviarTokenRefrshIFood(listener,token);
            }
        }

    }

    public void enviarNotificacao(String token,String titulo, String mensagem,Long idUsuario,Long idConversa ){
        APIListener<NotificacaoDados> listener = new APIListener<NotificacaoDados>() {
            @Override
            public void onSuccess(NotificacaoDados result) {

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.enviarNotificacao(listener,token,titulo,mensagem,idUsuario,idConversa);
    }
}
