package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Produto;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.repositorio.ProdutoRepositorio;

import java.util.List;

public class EditarProdutoViewModel extends AndroidViewModel {

    private ProdutoRepositorio produtoRepositorio ;

    private MutableLiveData<Produto> _Produto = new MutableLiveData<>();
    public LiveData<Produto> produto = _Produto;

    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    public EditarProdutoViewModel(@NonNull Application application) {
        super(application);
        produtoRepositorio = new ProdutoRepositorio(application.getBaseContext());

    }

    public  void buscarProduto(Long idProduto){

        APIListener<Dados> listener = new APIListener<Dados>() {
            @Override
            public void onSuccess(Dados result) {
                if(result.getProduto() != null){
                    _Produto.setValue(result.getProduto());
                }else{
                    _Resposta.setValue(new Resposta(result.getError()));
                }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        produtoRepositorio.buscarProduto(listener,idProduto);

    }

    public  void atualizarProduto(Long idProduto,String nome,String descricao,String preco,String ativo){

        if(nome != null && !nome.isEmpty() && !nome.equalsIgnoreCase(" ")){
            if(descricao != null && !descricao.isEmpty() && !descricao.equalsIgnoreCase(" ")){
            if(ativo != null && !ativo.isEmpty() && !ativo.equalsIgnoreCase(" ")){
              //  if(urlImagem != null && !urlImagem.isEmpty() && !urlImagem.equalsIgnoreCase(" ")){
                    if(preco != null && !preco.isEmpty() && !preco.equalsIgnoreCase(" ")){
                        if(idProduto != null){
                        Produto produto = new Produto();
                        produto.setTitulo(nome);
                        produto.setDescricao(descricao);
                        produto.setAtivo(ativo);
                       // produto.setUrlImagem(urlImagem);
                        preco = preco.replaceAll(",",".");
                        Float valor = Float.parseFloat(preco);
                        produto.setPreco(valor);
                        produto.setId(idProduto);
                        System.out.println(produto.toString());
                        APIListener<Dados> listener = new APIListener<Dados>() {
                            @Override
                            public void onSuccess(Dados result) {
                                if(result.getStatus()){
                                    _Resposta.setValue(new Resposta("Atualizado"));
                                }else{
                                    _Resposta.setValue(new Resposta(result.getError()));
                                }
                            }

                            @Override
                            public void onFailures(String mensagem) {
                                _Resposta.setValue(new Resposta(mensagem));

                            }
                        };

                        produtoRepositorio.atualizarProduto(listener,produto);

                        }else{
                            _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));

                        }

                    }else{
                      _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));

                    }
            /*    }else{
                    _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));

                }*/
            }else{
               _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));
            }
        }else{
            _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));
        }
        }else{
            _Resposta.setValue(new Resposta("Preencha os campos corretamente!"));
        }



    }
}
