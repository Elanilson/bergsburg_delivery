package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.helpers.DadosPreferences;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.Estabelicimento;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.EnderecoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.EstabelicimentoRepositorio;
import com.bergburg.bergburgdelivery.repositorio.UsuarioRepositorio;

public class CadastroViewModel extends AndroidViewModel {
    private EnderecoRepositorio enderecoRepositorio ;
    private UsuarioRepositorio usuarioRepositorio ;
    private EstabelicimentoRepositorio estabelicimentoRepositorio;

    private MutableLiveData<Estabelicimento> _Estabelicimento = new MutableLiveData<>();
    public LiveData<Estabelicimento> estabelicimento = _Estabelicimento;

    private MutableLiveData<Endereco> _Endereco = new MutableLiveData<>();
    public LiveData<Endereco> endereco = _Endereco;

    private MutableLiveData<Usuario> _Usuario = new MutableLiveData<>();
    public LiveData<Usuario> usuario = _Usuario;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private DadosPreferences preferences;

    public CadastroViewModel(@NonNull Application application) {
        super(application);
        preferences = new DadosPreferences(application.getBaseContext());
        usuarioRepositorio = new UsuarioRepositorio(application.getBaseContext());
        enderecoRepositorio = new EnderecoRepositorio(application.getBaseContext());
        estabelicimentoRepositorio = new EstabelicimentoRepositorio(application.getBaseContext());
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

    public void salvarEndereco(Usuario usuario , Endereco endereco){
        System.out.println("endereço: "+endereco.toString());
            if(endereco.getRua() != null && !endereco.getRua().isEmpty()){
                if(!endereco.getBairro().isEmpty()){
                    if (!endereco.getCidade().isEmpty()){
                        if (!endereco.getCep().isEmpty()){
                            if (endereco.getLatitude() != 0.0 && endereco.getLongitude() != 0.0){
                                if (!endereco.getEstado().isEmpty()){
                                    if(endereco.getNumeroCasa() != null ){
                                        APIListener<Dados>  listener = new APIListener<Dados>() {
                                            @Override
                                            public void onSuccess(Dados result) {
                                                if(result.getStatus()){
                                                    _Resposta.setValue(new Resposta("Sucesso",true));
                                                }else {
                                                    _Resposta.setValue(new Resposta(result.getError()));
                                                }
                                            }

                                            @Override
                                            public void onFailures(String mensagem) {
                                                _Resposta.setValue(new Resposta(mensagem));

                                            }
                                        };
                                        enderecoRepositorio.salvarEndereco(listener,usuario,endereco);
                                    }else{

                                    _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                    }

                                }else {
                                    _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                }
                            }else {
                                _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                            }
                        }else {
                            _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                        }
                    }else {
                        _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                    }
                }else{
                    _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                }
            }else{
                _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
            }


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
        usuarioRepositorio.getUsuario(listener,idUsuario);
    }

    public void enviarEmailDeConfirmacao(String email){
        System.out.println("fui chamado enviarEmailDeConfirmacao");
        APIListener<Dados>  listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                _Resposta.setValue(new Resposta(Constantes.EMAIL_ENVIADO,true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        usuarioRepositorio.enviarEmailDeConfirmacao(listener,email);
    }

    public void salvarUsuario(String nome,String email,String senha1,String senha2, String telefone,Endereco endereco){
        Usuario usuario = new Usuario();
        System.out.println("endereco: "+endereco.toString());

        if(nome != null && !nome.isEmpty()){
            if(senha1 != null && !senha1.isEmpty() && senha2 != null && !senha2.isEmpty()){
                if(senha1.equalsIgnoreCase(senha2)){
                    if(telefone != null && !telefone.isEmpty()){
                        if(endereco.getRua() != null && !endereco.getRua().isEmpty()){
                            if(!endereco.getBairro().isEmpty()){
                                if (!endereco.getCidade().isEmpty()){
                                    if (!endereco.getCep().isEmpty()){
                                        if (endereco.getLatitude() != null && endereco.getLongitude() !=  null && endereco.getLatitude() != 0.0 && endereco.getLongitude() != 0.0){
                                            if (!endereco.getEstado().isEmpty()){
                                                if(endereco.getNumeroCasa() != null ){
                                                    if(email != null && !email.isEmpty() && email.contains("@")){

                                                        usuario.setNome(nome.trim());
                                                        usuario.setSenha(senha1.trim());
                                                        usuario.setTelefone(telefone.trim());
                                                        usuario.setEmail(email.trim());

                                                        APIListener<Dados>  listener = new APIListener<Dados>() {
                                                            @Override
                                                            public void onSuccess(Dados result) {
                                                                Long id = result.getUsuario().getId();
                                                                Long idSacola = result.getUsuario().getId();
                                                                String nome = result.getUsuario().getNome();
                                                                String status = result.getUsuario().getStatus();
                                                                preferences.salvar(id,idSacola,nome,status);
                                                                _Usuario.setValue(result.getUsuario());
                                                                _Resposta.setValue(new Resposta(Constantes.CADASTRO_SUCESSO, true));
                                                            }
                                                            @Override
                                                            public void onFailures(String mensagem) {
                                                                System.out.println("Não foi possivel cadastrar: "+mensagem);
                                                                _Resposta.setValue(new Resposta(mensagem));

                                                            }
                                                        };
                                                        usuarioRepositorio.salvarUsuario(listener,usuario,endereco);
                                                    }else{
                                                        _Resposta.setValue(new Resposta("E-mail invalido"));
                                                    }

                                                }else{
                                                    _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                                }
                                            }else {
                                                _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                            }
                                        }else {
                                            _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                        }
                                    }else {
                                        _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                    }
                                }else {
                                    _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                                }
                            }else{
                                _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                            }
                        }else{
                            _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                        }

                    }else{
                        _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
                    }
                }else{
                    _Resposta.setValue(new Resposta("As senhas não conferem"));
                }

            }else{
                _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
            }

        }else{
            _Resposta.setValue(new Resposta("Todos os campos são obrigatórios"));
        }


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

    public void atualizarStatusLoja( Long id, String status){


        if(id != null){
            if(!status.isEmpty()){
                APIListener<Dados> listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {
                        if(result.getStatus()) {
                            _Resposta.setValue(new Resposta(Constantes.ALTERADO_SUCESSO, true));
                        }else if(result.getEnviado()){

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


    }


}
