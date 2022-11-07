package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.EnderecoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.UsuarioRepositorio;

public class PerfilViewModel extends AndroidViewModel {
    private UsuarioRepositorio repositorio;
    private EnderecoRepositorio enderecoRepositorio;

    private MutableLiveData<Usuario> _Usuario = new MutableLiveData<>();
    public LiveData<Usuario> usuario = _Usuario;

    private MutableLiveData<Endereco> _Endereco = new MutableLiveData<>();
    public LiveData<Endereco> endereco = _Endereco;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public PerfilViewModel(@NonNull Application application) {
        super(application);

        repositorio = new UsuarioRepositorio(application.getBaseContext());
        enderecoRepositorio = new EnderecoRepositorio(application.getBaseContext());
    }


    public void buscarUsuario(Long idUsuario){
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
        repositorio.getUsuario(listener,idUsuario);
    }

    public void alterarSenha(String email,String senha,String senha2){
        if(email != null && senha != null && senha2 != null){
            if(!senha.isEmpty() && !senha.equalsIgnoreCase(" ")){
                if(senha.equalsIgnoreCase(senha2)){
                        APIListener<Dados> listener = new APIListener<Dados>() {
                            @Override
                            public void onSuccess(Dados result) {
                                if(result.getStatus()){
                                    _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO,true));
                                }else{
                                    _Resposta.setValue(new Resposta("Não foi possível alterar senha no momento"));
                                }
                            }

                            @Override
                            public void onFailures(String mensagem) {
                                _Resposta.setValue(new Resposta(mensagem));

                            }
                        };
                        repositorio.alterarSenha(listener,email,senha);

                }else{
                    _Resposta.setValue(new Resposta("As senhas devem ser iguais"));
                }

            }else{
                _Resposta.setValue(new Resposta("Preencha corretamente"));
            }

        }else{
            _Resposta.setValue(new Resposta("Preencha corretamente"));
        }
    }

    public void alterarDadosUsuario(String email,String nome,String telefone){
        if(email != null && nome != null && telefone != null){
            if(!nome.isEmpty() && !nome.equalsIgnoreCase(" ")){
                if(!telefone.isEmpty() && !telefone.equalsIgnoreCase(" ")){
                    APIListener<Dados> listener = new APIListener<Dados>() {
                        @Override
                        public void onSuccess(Dados result) {
                            if(result.getStatus()){
                                _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO,true));
                            }else{
                                _Resposta.setValue(new Resposta(result.getError()));
                            }
                        }

                        @Override
                        public void onFailures(String mensagem) {
                            _Resposta.setValue(new Resposta(mensagem));

                        }
                    };
                    repositorio.alterarDadosUsuario(listener,email,nome,telefone);

                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }

            }else{
                _Resposta.setValue(new Resposta("Preencha corretamente"));
            }

        }else{
            _Resposta.setValue(new Resposta("Preencha corretamente"));
        }
    }

    public void alterarEndereco(Usuario usuario,Endereco endereco ){
        if(endereco != null && usuario != null){
            if(!endereco.getRua().isEmpty() && !endereco.getRua().equalsIgnoreCase(" ")){
            if(!endereco.getBairro().isEmpty() && !endereco.getBairro().equalsIgnoreCase(" ")){
            if(!endereco.getCep().isEmpty() && !endereco.getCep().equalsIgnoreCase(" ")){
            if(!endereco.getNumeroCasa().isEmpty() && !endereco.getNumeroCasa().equalsIgnoreCase(" ")){
            if(endereco.getLatitude() != null && endereco.getLatitude() != null){
            if(endereco.getLongitude() != null && endereco.getLongitude() != null){
            if(usuario.getId() != null && usuario.getId() != null){

                APIListener<Dados> listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {
                        if(result.getStatus()){
                            _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO,true));
                        }else{
                            _Resposta.setValue(new Resposta("Não foi possível alterar dados no momento"));
                        }
                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));

                    }
                };
                enderecoRepositorio.atualizarEnderecov2(listener,usuario,endereco);

                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }
                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }

                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }

                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }
                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }
                }else{
                    _Resposta.setValue(new Resposta("Preencha corretamente"));
                }

            }else{
                _Resposta.setValue(new Resposta("Preencha corretamente"));
            }

        }else{
            _Resposta.setValue(new Resposta("Preencha corretamente"));
        }
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
