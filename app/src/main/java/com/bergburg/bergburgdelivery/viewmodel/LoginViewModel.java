package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.UsuarioRepositorio;

public class LoginViewModel extends AndroidViewModel {

    private UsuarioRepositorio repositorio ;

    private MutableLiveData<Usuario> _Usuario = new MutableLiveData<>();
    public LiveData<Usuario> usuario = _Usuario;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private DadosPreferences preferences;
    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repositorio = new UsuarioRepositorio(application.getBaseContext());
        preferences = new DadosPreferences(application.getBaseContext());
        context = application.getBaseContext();
    }


    public void login(String login, String senha){

        if(login.equalsIgnoreCase("") || senha.equalsIgnoreCase("" ) ||
                        login.equalsIgnoreCase(" ") ||
                        senha.equalsIgnoreCase(" ") ||
                        login == null || senha == null){
            _Resposta.setValue(new Resposta("Todos os campos são obrigatorios"));

        }else{
            APIListener<Dados> listener = new APIListener<Dados>() {
                @Override
                public void onSuccess(Dados result) {
                    if(result.getError() != "" ){ // sem mensagem de error
                        _Resposta.setValue(new Resposta(result.getError()));
                    }else {
                       // System.out.println(result.getUsuario().toString());
                        //salvar id do usuario para verificar se ele ta logado ou nao
                        Long id = result.getUsuario().getId();
                        Long idConversa = result.getUsuario().getIdConversa();
                      //  System.out.println("IDConversa "+idConversa);
                        Long idSacola = result.getUsuario().getId();
                        String nome = result.getUsuario().getNome();
                        String status = result.getUsuario().getStatus();
                         preferences.salvar(id,idSacola,nome,status);
                        if(idConversa != null){
                            preferences.salvarIdConversa(idConversa);
                        }
                        _Usuario.setValue(result.getUsuario());

                        //verificar se o status do usuário está logado
                        if(result.getUsuario().getStatus().equalsIgnoreCase(context.getString(R.string.logado))){
                             _Resposta.setValue(new Resposta(Constantes.LOGIN_SUCESSO,true));
                        }
                    }
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));

                }
            };

            repositorio.login(listener,login,senha);
        }

    }

    public void enviarEmailDeConfirmacao(String email){
        APIListener<Dados>  listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
              if(result.getStatus()){
                  _Resposta.setValue(new Resposta(Constantes.EMAIL_ENVIADO,true));
              }else{
                  _Resposta.setValue(new Resposta("Não foi possível enviar E-mail."));
              }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.enviarEmailDeConfirmacao(listener,email);
    }

    public void buscarUsuario(Long idUsuario){
        APIListener<Dados>  listener = new APIListener<Dados>() {
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

    public void enviarEmailDeRecuperacao(String email){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
               if(result.getStatus()){
                   _Resposta.setValue(new Resposta(Constantes.EMAIL_ENVIADO,true));
               }else{
                   _Resposta.setValue(new Resposta(Constantes.EMAIL_NAO_CADASTRADO));
               }


            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.enviarEmailDeRecuperacao(listener,email);

    }

    public void verificarUsuarioLogado(Long id){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                System.out.println(result.getUsuario());
                if(result.getStatus()){ // sem mensagem de error
                    if(result.getUsuario() != null){
                        if(result.getUsuario().getStatus() != null){
                            if(result.getUsuario().getStatus().equalsIgnoreCase("Logado")){
                                _Resposta.setValue(new Resposta("Sucesso",true));
                            }
                        }else{

                        }

                    }
                      _Usuario.setValue(result.getUsuario());
                }

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.verificarUsuarioLogado(listener,id);

    }

    public void deslogar(Long id){
        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getStatus()){
                    String status = result.getUsuario().getStatus();
                    preferences.salvarStatus(status.trim());
                    _Usuario.setValue(result.getUsuario());
                    _Resposta.setValue(new Resposta(Constantes.DESLOGADO,true));
                }else{
                    _Resposta.setValue(new Resposta(Constantes.USUARIO_NAO_ENCONTRADO));
                }

            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.deslogar(listener,id);


    }



}
