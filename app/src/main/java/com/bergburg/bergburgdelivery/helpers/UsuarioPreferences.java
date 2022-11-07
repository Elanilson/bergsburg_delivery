package com.bergburg.bergburgdelivery.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class UsuarioPreferences {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final String NOME_ARQUIVO = "usuario.preferencias";
    private final String CHAVE_ID = "idUsuario";
    private final String CHAVE_IDMENSAGEM = "idMensagem";
    private final String CHAVE_IDConversa = "idUConversa";
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_STATUS = "status";
    private final String CHAVE_IDSACOLA = "idSacola";
    private final String CHAVE_LATITUDE = "latitude";
    private final String CHAVE_LONGITUDE = "longitude";
    private final String CHAVE_EMAIL = "email";
    private final String CHAVE_SENHA = "senha";
    private final String CHAVE_ATIVADA = "ativada";

    public UsuarioPreferences(Context context) {
        this.context = context;

        preferences = context.getSharedPreferences(NOME_ARQUIVO,0);
        editor = preferences.edit();
    }

    public void salvar(Long id,Long idSacola,String nome,String status){
        editor.putLong(CHAVE_ID, id );
        editor.putLong(CHAVE_IDSACOLA, idSacola );
        editor.putString(CHAVE_NOME, nome );
        editor.putString(CHAVE_STATUS, status );
        editor.commit();
    }

    public void salvarIdUsuario(Long id){
        editor.putLong(CHAVE_ID, id );
        editor.commit();
    }

    public void salvarIdUltimaMensagemLida(Long id){
        editor.putLong(CHAVE_IDMENSAGEM, id );
        editor.commit();
    }

    public void salvarIdConversa(Long id){
        editor.putLong(CHAVE_IDConversa, id );
        editor.commit();
    }
    public void salvarContaAtivada(String ativada){
        editor.putString(CHAVE_ATIVADA, ativada );
        editor.commit();
    }
    public void salvarNome(String nome){
        editor.putString(CHAVE_NOME, nome );
        editor.commit();
    }

    public void salvarEmailSenha(String email,String senha){
        editor.putString(CHAVE_EMAIL, email );
        editor.putString(CHAVE_SENHA, senha );
        editor.commit();
    }
    public void salvarStatus(String status){
        editor.putString(CHAVE_STATUS, status );
        editor.commit();
    }

    public void salvarCordenadas(String latitude, String longitude){
        editor.putString(CHAVE_LATITUDE, latitude );
        editor.putString(CHAVE_LONGITUDE, longitude );
        editor.commit();
    }

    public String recuperarLatitude(){
        return preferences.getString(CHAVE_LATITUDE, "");
    }
    public String recuperarLogitude(){
        return preferences.getString(CHAVE_LONGITUDE, "");
    }
    public Long recuperarID(){
        return preferences.getLong(CHAVE_ID, 0L);
    }
    public Long recuperarIDUltimaMensagemLida(){
        return preferences.getLong(CHAVE_IDMENSAGEM, 0L);
    }
    public Long recuperarIDConversa(){
        return preferences.getLong(CHAVE_IDConversa, 0L);
    }
    public Long recuperarIDSacola(){
        return preferences.getLong(CHAVE_IDSACOLA, 0L);
    }

    public String recuperarContaAtivada(){
        return preferences.getString(CHAVE_ATIVADA, "");
    }

    public String recuperarNome(){
        return preferences.getString(CHAVE_NOME, "");
    }

    public String recuperarStatus(){
        return preferences.getString(CHAVE_STATUS, "");
    }
    public String recuperarEmail(){
        return preferences.getString(CHAVE_EMAIL, "");
    }
    public String recuperarSenha(){
        return preferences.getString(CHAVE_SENHA, "");
    }
    public void limpar(){
        context.deleteSharedPreferences(NOME_ARQUIVO);
        context.deleteSharedPreferences(CHAVE_ID);
        context.deleteSharedPreferences(CHAVE_IDMENSAGEM);
        context.deleteSharedPreferences(CHAVE_IDConversa);
        context.deleteSharedPreferences(CHAVE_NOME);
        context.deleteSharedPreferences(CHAVE_STATUS);
        context.deleteSharedPreferences(CHAVE_IDSACOLA);
        context.deleteSharedPreferences(CHAVE_LATITUDE);
        context.deleteSharedPreferences(CHAVE_LONGITUDE);
       // context.deleteSharedPreferences(CHAVE_EMAIL);
       // context.deleteSharedPreferences(CHAVE_SENHA);
        context.deleteSharedPreferences(CHAVE_ATIVADA);

    }
}
