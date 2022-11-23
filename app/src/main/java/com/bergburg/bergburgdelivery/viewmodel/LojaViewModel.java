package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.EstabelicimentoRepositorio;

public class LojaViewModel extends AndroidViewModel {

    private EstabelicimentoRepositorio estabelicimentoRepositorio;

    private MutableLiveData<Estabelicimento> _Estabelicimento = new MutableLiveData<>();
    public LiveData<Estabelicimento> estabelicimento = _Estabelicimento;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public LojaViewModel(@NonNull Application application) {
        super(application);

        estabelicimentoRepositorio = new EstabelicimentoRepositorio(application.getBaseContext());
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

    public void atualizarLoja( Long id, String nome,String status,String ramo,String endereco,String tempoEntrega,String valorMinimo,String telefone){


        if(id != null){
            if(!nome.isEmpty()){
                    if(!status.isEmpty()){
                        if(!ramo.isEmpty()){
                            if(!endereco.isEmpty()){
                                if(!tempoEntrega.isEmpty()){
                                    if(!valorMinimo.isEmpty()){
                                      if(!telefone.isEmpty()){

                                        APIListener<Dados> listener = new APIListener<Dados>() {
                                            @Override
                                            public void onSuccess(Dados result) {
                                                if(result.getStatus()){
                                                    _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO,true));
                                                }else{
                                                    _Resposta.setValue(new Resposta("Não foi possível alterar, tente novamente"));
                                                }
                                            }

                                            @Override
                                            public void onFailures(String mensagem) {
                                                _Resposta.setValue(new Resposta(mensagem));

                                            }
                                        };
                                        estabelicimentoRepositorio.atualizarLoja(listener,id,nome,status,ramo,endereco,tempoEntrega,valorMinimo,telefone);

                                    }else{
                                        _Resposta.setValue(new Resposta("Informe o telefone"));
                                    }
                                    }else{
                                        _Resposta.setValue(new Resposta("Informe o valor minimo"));
                                    }
                                }else{
                                    _Resposta.setValue(new Resposta("Informe o tempo de entrega"));
                                }
                            }else{
                                _Resposta.setValue(new Resposta("Informe o endereço"));
                            }
                        }else{
                            _Resposta.setValue(new Resposta("Digite o ramo da loja"));
                        }
                    }else{
                        _Resposta.setValue(new Resposta("A loja está aberta ou fechada ?"));
                    }
            }else{
                _Resposta.setValue(new Resposta("Informe o nome da loja"));
            }
        }else{
            _Resposta.setValue(new Resposta("ID da loja não encontrado"));
        }


    }



  /*  public void atualizarLoja( Long id, String status){


        if(id != null){
                if(!status.isEmpty()){
                                    APIListener<Dados> listener = new APIListener<Dados>() {
                                        @Override
                                        public void onSuccess(Dados result) {
                                            if(result.getStatus()){
                                                _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO,true));
                                            }else{
                                                _Resposta.setValue(new Resposta("Não foi possível alterar, tente novamente"));
                                            }
                                        }

                                        @Override
                                        public void onFailures(String mensagem) {
                                            _Resposta.setValue(new Resposta(mensagem));

                                        }
                                    };
                                    estabelicimentoRepositorio.atualizarStatusLoja(listener,id,status);

                }else{
                    _Resposta.setValue(new Resposta("A loja está aberta ou fechada ?"));
                }
        }else{
            _Resposta.setValue(new Resposta("ID da loja não encontrado"));
        }

    }*/
}
