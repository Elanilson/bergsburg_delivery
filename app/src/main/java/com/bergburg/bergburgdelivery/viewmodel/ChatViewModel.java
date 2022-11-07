package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.model.NotificacaoDados;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.EnderecoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.MensagensRepositorio;
import com.bergburg.bergburgdelivery.repositorio.NotificacaoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.UsuarioRepositorio;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private MensagensRepositorio repositorio;
    private NotificacaoRepositorio notificacaoRepositorio;


    private MutableLiveData<List<Mensagem>> _Mensagens = new MutableLiveData<>();
    public LiveData<List<Mensagem>> mensagens = _Mensagens;

    private MutableLiveData<Mensagem> _Mensagen = new MutableLiveData<>();
    public LiveData<Mensagem> mensagen = _Mensagen;


    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private UsuarioPreferences preferences;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        preferences = new UsuarioPreferences(application.getBaseContext());
        repositorio = new MensagensRepositorio(application.getBaseContext());
        notificacaoRepositorio = new NotificacaoRepositorio(application.getBaseContext());
    }

    public void getMensagens(Long idUsuario ){
        if(idUsuario != null){
            APIListener<Dados>  listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                   if(result.getMensagens() != null){
                       _Mensagens.setValue(result.getMensagens());
                   }else{// não tem mensagens
                       System.out.println("Mensagens não encontradas id-"+idUsuario);
                     _Resposta.setValue(new Resposta(Constantes.SEM_MENSAGEM));

                   }
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));

                }
            };
            repositorio.getMensagens(listener,idUsuario);
        }else{
            _Resposta.setValue(new Resposta("Verifique sua conexão"));

        }
    }

    public void visualizarMensagem(Long idMensagem ){
        if(idMensagem != null){
            APIListener<Dados>  listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                    if(result.getStatus()){
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
            repositorio.visualizarMensagem(listener,idMensagem);
        }else{
            _Resposta.setValue(new Resposta("Verifique sua conexão"));

        }
    }

    public void enviarMensagem(Long idConversa,String texto,Long idUsuario ){
        if(idUsuario != null && idConversa != null){
            if(texto != null && !texto.isEmpty() && !texto.equalsIgnoreCase(" ")){
                APIListener<Dados>  listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {
                        if(result.getStatus()){
                            _Resposta.setValue(new Resposta(Constantes.ENVIADO,true));
                            //System.out.println("");
                        }else{
                            _Resposta.setValue(new Resposta(" Não foi possível enviar!"));
                        }

                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));

                    }
                };
                repositorio.enviarMensagem(listener,idUsuario,texto,idConversa);
            }
        }else{
            _Resposta.setValue(new Resposta("Não foi possível enviar"));

        }
    }



}
