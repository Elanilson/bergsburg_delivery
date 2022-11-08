package com.bergburg.bergburgdelivery.repositorio.remoto.services;


import com.bergburg.bergburgdelivery.model.Dados;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BergburgService {
    @GET("getCategorias.php")
    Call<Dados> getCategorias();

    @GET("getEstabelicimento.php")
    Call<Dados> getEstabelicimento();

    @GET("produtosPopulares.php/")
    Call<Dados> produtosPopulares();

    @GET("getProdutos.php")
    Call<Dados> getProdutos();

    @POST("pesquisarProduto.php/")
    @FormUrlEncoded
    Call<Dados> pesquisarProduto(
            @Field("texto") String texto);



    @POST("login.php/")
    @FormUrlEncoded
    Call<Dados> login(
            @Field("login") String login,
            @Field("senha") String senha);

    @POST("salvarToken.php/")
    @FormUrlEncoded
    Call<Dados> salvarToken(
            @Field("idUsuario")Long idUsuario,
            @Field("token") String token);

    @POST("getToken.php/")
    @FormUrlEncoded
    Call<Dados> getToken(
            @Field("idUsuario") Long idUsuario);

    @POST("getMensagens.php/")
    @FormUrlEncoded
    Call<Dados> getMensagens(
            @Field("idUsuario") Long idUsuario);

    @GET("getConversas.php/")
    Call<Dados> getConversas();

    @POST("salvarMensagem.php/")
    @FormUrlEncoded
    Call<Dados> enviarMensagem(

            @Field("idUsuario") Long idUsuario,
            @Field("texto") String texto,
            @Field("idConversa") Long idConversa);

    @POST("verificarUsuarioLogado.php/")
    @FormUrlEncoded
    Call<Dados> verificarUsuarioLogado(
            @Field("id") Long id);

    @POST("enviarEmailDeRecuperacao.php/")
    @FormUrlEncoded
    Call<Dados> enviarEmailDeRecuperacao(
            @Field("email") String email);

    @PUT("deslogar.php/")
    @FormUrlEncoded
    Call<Dados> deslogar(
            @Field("id") Long id);
    @PUT("visualizarMensagem.php/")
    @FormUrlEncoded
    Call<Dados> visualizarMensagem(
            @Field("idMensagem") Long idMensagem);
    //ok
    @PUT("alterarSenha.php/")
    @FormUrlEncoded
    Call<Dados> alterarSenha(
            @Field("email") String email,
            @Field("senha") String senha);

    //ok
    @PUT("alterarDadosUsuario.php/")
    @FormUrlEncoded
    Call<Dados> alterarDadosUsuario(
            @Field("email") String email,
            @Field("nome") String nome,
            @Field("telefone") String telefone
    );

    @POST("getProdutosPorcategoria.php/")
    @FormUrlEncoded
    Call<Dados> produtosPorCategoria(
            @Field("idCategoria") Long idCategoria);

    @POST("buscarProduto.php/")
    @FormUrlEncoded
    Call<Dados> buscarProduto(
            @Field("idProduto") Long idProduto);


    @POST("enviarEmail.php/")
    @FormUrlEncoded
    Call<Dados> enviarEmailDeConfirmacao(
            @Field("email") String email);

    @POST("salvarUsuario.php/")
    @FormUrlEncoded
    Call<Dados> salvarUsuario(
            @Field("nome") String nome,
            @Field("senha") String senha,
            @Field("email") String email,
            @Field("telefone") String telefone,
            @Field("rua") String rua,
            @Field("bairro") String bairro,
            @Field("cep") String cep,
            @Field("cidade") String cidade,
            @Field("estado") String estado,
            @Field("numeroCasa") String numeroCasa,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("complemento") String complemento

    );

    @POST("buscarEnderecoSalvo.php/")
    @FormUrlEncoded
    Call<Dados> buscarEnderecoSalvo(
            @Field("idUsuario") Long idUsuario
    );

    @POST("salvarEndereco.php/")
    @FormUrlEncoded
    Call<Dados> salvarEndereco(
            @Field("idUsuario") Long idUsuario,
            @Field("rua") String rua,
            @Field("bairro") String bairro,
            @Field("cep") String cep,
            @Field("cidade") String cidade,
            @Field("estado") String estado,
            @Field("numeroCasa") String numeroCasa,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("complemento") String complemento
    );


    @PUT("atualizarProduto.php/")
    @FormUrlEncoded
    Call<Dados> atualizarProduto(
            @Field("id") Long idProduto,
            @Field("titulo") String titulo,
            @Field("descricao") String descricao,
            @Field("preco") Float preco,
            @Field("ativo") String ativo
          //  @Field("urlImagem") String urlImagem
    );
    //ok
    @PUT("atualizarEnderecov2.php/")
    @FormUrlEncoded
    Call<Dados> atualizarEnderecov2(
            @Field("idUsuario") Long idUsuario,
            @Field("rua") String rua,
            @Field("bairro") String bairro,
            @Field("cep") String cep,
            @Field("numeroCasa") String numeroCasa,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("complemento") String complemento
    );

    @POST("criarPedido2.php/")
    @FormUrlEncoded
    Call<Dados> criarPedido(
            @Field("idUsuario") Long idUsuario,
            @Field("idSacola") Long idSacola,
            @Field("opcaoEntrega") String opcaoEntrega,
            @Field("total") Float total,
            @Field("taxa_entrega") Float taxa_entrega,
            @Field("subtotal") Float subTotal
    );

    @POST("getPedidos.php/")
    @FormUrlEncoded
    Call<Dados> listarPedidos(
            @Field("idUsuario") Long idUsuario
    );

    @POST("getPedido.php/")
    @FormUrlEncoded
    Call<Dados> listarPedido(
            @Field("idPedido") Long idPedido
    );

    @POST("getUsuario.php/")
    @FormUrlEncoded
    Call<Dados> getUsuario(
            @Field("idUsuario") Long idUsuario
    );

    @POST("getItensPedido.php/")
    @FormUrlEncoded
    Call<Dados> getItensPedido(
            @Field("idPedido") Long idPedido
    );

    @POST("getStatusPedido.php/")
    @FormUrlEncoded
    Call<Dados> getStatusPedido(
            @Field("idPedido") Long idPedido
    );


    @POST("getItensSacola.php/")
    @FormUrlEncoded
    Call<Dados> getItensSacola(
            @Field("idSacola") Long idSacola
    );

    @POST("deletarItemDaSacola.php/")
    @FormUrlEncoded
    Call<Dados> deletarItemDaSacola(
            @Field("idItem") Long idItem
    );
    //ok
    @POST("adicionarItemSacola.php/")
    @FormUrlEncoded
    Call<Dados> adicionarItemSacola(
            @Field("idSacola") Long idSacola,
            @Field("idItem") Long idItem,
            @Field("quantidade") int quantidade,
            @Field("preco") Float preco,
            @Field("observacao") String observacao
    );
    //ok
    @POST("adicionarItemPedido2.php/")
    @FormUrlEncoded
    Call<Dados> adicionarItemPedido(
            @Field("idPedido") Long idPedido,
            @Field("idSacola") Long idSacola
    );

    @PUT("atualizarQuantidadeItemSacola.php/")
    @FormUrlEncoded
    Call<Dados> atualizarQuantidadeItemSacola(
            @Field("idSacola") Long idSacola,
            @Field("idItem") Long idItem,
            @Field("quantidade") int quantidade
    );

    //ok
    @PUT("atualizarObservacaoItemSacola.php/")
    @FormUrlEncoded
    Call<Dados> atualizarObservacaoItemSacola(
            @Field("idSacola") Long idSacola,
            @Field("idItem") Long idItem,
            @Field("observacao") String observacao
    );
}