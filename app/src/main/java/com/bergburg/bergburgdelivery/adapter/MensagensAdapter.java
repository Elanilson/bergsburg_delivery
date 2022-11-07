package com.bergburg.bergburgdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.UsuarioPreferences;
import com.bergburg.bergburgdelivery.listeners.OnListenerAcao;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.viewholder.MensagensViewHolder;
import com.bergburg.bergburgdelivery.viewholder.ViewItemPedioViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensViewHolder> {
    private Context context;
    private List<Mensagem> mensagens = new ArrayList<>();
    private OnListenerAcao<Mensagem> onListenerAcao;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;
    private UsuarioPreferences preferences;

    public MensagensAdapter(Context context) {
        this.context = context;
        preferences = new UsuarioPreferences(context);
    }

    @NonNull
    @Override
    public MensagensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = null;
        if(viewType  == TIPO_REMETENTE){
             layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente,parent,false);
        }else if(viewType  == TIPO_DESTINATARIO){
             layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario,parent,false);

        }
        return new MensagensViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MensagensViewHolder holder, int position) {
        holder.bind(mensagens.get(position));

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        Long idUsuario = preferences.recuperarID();
        if(idUsuario != null){
            if(mensagem.getIdUsuario().equals(idUsuario)){
                return  TIPO_REMETENTE;
            }
        }
            return TIPO_DESTINATARIO;
    }

    public void attackMensagens(List<Mensagem> mensagens){
            int tamanho = this.mensagens.size(); // tamanho
            if(tamanho != 0){
                Long id = this.mensagens.get(tamanho-1).getId();
                if(mensagens.size() != 0){ // je tiver dados atualiza apenas os novos
                    //limpar();
                    //  this.mensagens = mensagens;
                    for (Mensagem m : mensagens){
                        if(m.getId() > id){
                            this.mensagens.add(m);
                            notifyItemChanged(mensagens.indexOf(m));
                        }

                    }
                    // notifyDataSetChanged();
                }

            }else{ // carrega todos os dados na primeira vez
                this.mensagens = mensagens;
                notifyDataSetChanged();
            }


    }
    public void limpar(){
        this.mensagens.clear();
       // notifyDataSetChanged();
    }
    public void attackListener(OnListenerAcao<Mensagem> onListenerAcao){
        this.onListenerAcao = onListenerAcao;
    }
}
