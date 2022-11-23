package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
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

    private DadosPreferences preferences;

    public ConversasViewModel(@NonNull Application application) {
        super(application);
        preferences = new DadosPreferences(application.getBaseContext());
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





}
