package com.bergburg.bergburgdelivery.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class DadosIFoodPreferences {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences preferencesEmail_Senha ;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editorEmail_Senha;



    private final String NOME_ARQUIVO = "rerere.preferencias";
    private  String CHAVE_ID_PEIDO = "PEDIDO";
    private  String CHAVE_LINK = "LINK";
    private  String CHAVE_TOKEN_IFOOD = "token";



    public DadosIFoodPreferences(Context context, Long idPedidoApp) {
        this.context = context;
        CHAVE_ID_PEIDO = "Pedido_"+idPedidoApp;
        CHAVE_LINK = "link_"+idPedidoApp;

        preferences = context.getSharedPreferences(NOME_ARQUIVO,0);
        editor = preferences.edit();

    }

    public void salvar(String idPedido,String link){
        editor.putString(CHAVE_ID_PEIDO, idPedido );
        editor.putString(CHAVE_LINK, link );
        editor.commit();
    }

    public void salvarTokenIFood(String token){
        editor.putString(CHAVE_TOKEN_IFOOD, token );
        editor.commit();
    }


    public String recuperarLinkAcompanhamento(){
        return preferences.getString(CHAVE_LINK, "");
    }
    public String recuperaIdPedidoIfood(){
        return preferences.getString(CHAVE_ID_PEIDO, "");
    }
    public String recuperarToken(){
        return preferences.getString(CHAVE_TOKEN_IFOOD, "");
    }


}
