package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Conversas;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.ConversasRepositorio;
import com.bergburg.bergburgdelivery.repositorio.MensagensRepositorio;

import java.util.List;

public class ConversasViewModel extends AndroidViewModel {
    private ConversasRepositorio repositorio;
    private MensagensRepositorio mensagensRepositorio;



    private MutableLiveData<List<Conversas>> _Conversas = new MutableLiveData<>();
    public LiveData<List<Conversas>> conversas = _Conversas;

    private MutableLiveData<List<Mensagem>> _Mensagens = new MutableLiveData<>();
    public LiveData<List<Mensagem>> mensagens = _Mensagens;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private UsuarioPreferences preferences;

    public ConversasViewModel(@NonNull Application application) {
        super(application);
        preferences = new UsuarioPreferences(application.getBaseContext());
        repositorio = new ConversasRepositorio(application.getBaseContext());
        mensagensRepositorio = new MensagensRepositorio(application.getBaseContext());
    }

    public void getConversas( ){

            APIListener<Dados>  listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                   if(result.getConversas() != null){
                       _Conversas.setValue(result.getConversas());
                   }else{// não tem mensagens
                     _Resposta.setValue(new Resposta("Limpo "));

                   }
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));

                }
            };
            repositorio.getConversas(listener);

    }

    public void getMensagens(Long idUsuario ){
        if(idUsuario != null){
            APIListener<Dados>  listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                    if(result.getMensagens() != null){
                        _Mensagens.setValue(result.getMensagens());
                    }else{// não tem mensagens
                        // _Resposta.setValue(new Resposta("Limpo "+idConversa));

                    }
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));

                }
            };
            mensagensRepositorio.getMensagens(listener,idUsuario);
        }else{
            _Resposta.setValue(new Resposta("Verifique sua conexão"));

        }
    }

    public void enviarMensagem(String texto ){
        Long idUsuario = preferences.recuperarID();
        Long idConversa = preferences.recuperarIDConversa();
        if(idUsuario != null && idConversa != null){
            if(texto != null && !texto.isEmpty() && !texto.equalsIgnoreCase(" ")){
                APIListener<Dados>  listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {
                        if(result.getStatus()){
                            _Resposta.setValue(new Resposta(Constantes.ENVIADO,true));
                        }else{
                            _Resposta.setValue(new Resposta(idUsuario+"- Não foi possível enviar! "+idConversa));

                        }
                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));

                    }
                };
                //repositorio.enviarMensagem(listener,idUsuario,texto,idConversa);
            }
        }else{
            _Resposta.setValue(new Resposta("Algo deu errado!"));

        }
    }




}
