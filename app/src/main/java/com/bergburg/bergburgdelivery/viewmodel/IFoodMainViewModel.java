package com.bergburg.bergburgdelivery.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.helpers.DadosIFoodPreferences;
import com.bergburg.bergburgdelivery.ifood.Cliente;
import com.bergburg.bergburgdelivery.ifood.model.Autenticacao;
import com.bergburg.bergburgdelivery.ifood.model.Coordenadas;
import com.bergburg.bergburgdelivery.ifood.model.DetalhesPedido;
import com.bergburg.bergburgdelivery.ifood.model.EnderecoDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.Entrega;
import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.bergburg.bergburgdelivery.ifood.model.Item;
import com.bergburg.bergburgdelivery.ifood.model.LayoutEnvioPedido;
import com.bergburg.bergburgdelivery.ifood.model.LayoutPedidoIfood;
import com.bergburg.bergburgdelivery.ifood.model.RespostaDisponibilidadeDeEntrega;
import com.bergburg.bergburgdelivery.ifood.model.RespostaPedido;
import com.bergburg.bergburgdelivery.ifood.model.Telefone;
import com.bergburg.bergburgdelivery.listeners.APIListener;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Endereco;
import com.bergburg.bergburgdelivery.model.ItensPedido;
import com.bergburg.bergburgdelivery.model.Resposta;
import com.bergburg.bergburgdelivery.model.Usuario;
import com.bergburg.bergburgdelivery.repositorio.IfoodRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IFoodMainViewModel extends AndroidViewModel {
    private IfoodRepositorio repositorio ;

    private MutableLiveData<Autenticacao> _Autenticacao = new MutableLiveData<>();
    public LiveData<Autenticacao> autenticacao = _Autenticacao;

    private MutableLiveData<DetalhesPedido> _DetalhesPedido = new MutableLiveData<>();
    public LiveData<DetalhesPedido> detalhesDoPedido = _DetalhesPedido;

    private MutableLiveData<RespostaDisponibilidadeDeEntrega> _FreteIfood = new MutableLiveData<>();
    public LiveData<RespostaDisponibilidadeDeEntrega> freteIfood = _FreteIfood;

    private MutableLiveData<List<EventoPedido>> _EventosPedido = new MutableLiveData<>();
    public LiveData<List<EventoPedido>> eventoPedido = _EventosPedido;



    private MutableLiveData<Resposta> _Resposta = new MutableLiveData<>();
    public LiveData<Resposta> resposta = _Resposta;

    private MutableLiveData<RespostaPedido> _RespostaPedido = new MutableLiveData<>();
    public LiveData<RespostaPedido> respostaPedido = _RespostaPedido;

    private List<EventoPedido> listaTemporaria = new ArrayList<>();

    private LayoutEnvioPedido  layoutEnvioPedidoiFood = new LayoutEnvioPedido();

    private String refresh_token;

    public IFoodMainViewModel(@NonNull Application application) {
        super(application);
        repositorio = new IfoodRepositorio(application);
    }

    public void autenticar(){
        APIListener<Autenticacao> listener = new APIListener<Autenticacao>() {
            @Override
            public void onSuccess(Autenticacao result) {
                _Autenticacao.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.autenticar(listener);

    }

    public void renovarToken(String refresh_token, Context context){
        if(refresh_token != null){
            if(!refresh_token.isEmpty()){
                this.refresh_token = refresh_token;
            }else{
                refresh_token = context.getString(R.string.refresh_token);
            }
        }else{
            refresh_token = context.getString(R.string.refresh_token);
        }


        System.out.println("xxxxxxxxxxxxxxxxxxxxxx recebi "+refresh_token);

        APIListener<Autenticacao> listener = new APIListener<Autenticacao>() {
            @Override
            public void onSuccess(Autenticacao result) {
                _Autenticacao.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };
        repositorio.renovarToken(listener,refresh_token);

    }
    // Receber pedido
    public void verificarEvento(){
        System.out.println("Procurando eventos");
        APIListener<List<EventoPedido>> listener = new APIListener<List<EventoPedido>>() {
            @Override
            public void onSuccess(List<EventoPedido> result) {

                    if(result != null){

                        listaTemporaria.addAll(result);
                        //- Enviar /acknowledgment para todos os eventos recebidos imediatamente após a request de polling;
                     //   reconhecerLimparEnventos(result); // acknowledgment
                    }else{
                        System.out.println("Sem eventos");
                    }

                _EventosPedido.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));
            }
        };
        repositorio.verificarEventos(listener);
    }

    public void reconhecerLimparEnventos(List<EventoPedido> eventos){
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

               if(result){
                   _Resposta.setValue(new Resposta("Sucesso ao limpar eventos",true));
               }else{
                   _Resposta.setValue(new Resposta("Não foi possível limpar eventos"));
               }
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

            repositorio.reconhecerLimparEnventos(listener,eventos);
       /* if(eventos.size() > 0){
        }else{
            repositorio.reconhecerLimparEnventos(listener,listaTemporaria);
        }*/
    }
    //confirmar pedido
    public void confirmarPedido(String idPedido){
       // String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Pedido confirmado com sucesso",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.confirmarPedido(listener,idPedido);
    }
    //despachar pedido
    public void despachar(String idPedido){
        // String idPedido = "a0af51da-72ff-44dc-9950-a09912d00f12";
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Pedido despachado com sucesso",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.despachar(listener,idPedido);
    }

    //cancelar um pedido delivery
    public void cancelarPepdido(String motivo,String idPedidoIfood,String comentario){

        CancelamentoDePedido cancelamentoDePedido = new CancelamentoDePedido();
        if(comentario != null && !comentario.isEmpty()){
            cancelamentoDePedido.setMotivo(comentario);
        }else{
            cancelamentoDePedido.setMotivo(motivo);
        }

        if(motivo.equalsIgnoreCase("501-PROBLEMAS DE SISTEMA")){
            cancelamentoDePedido.setCodigoDeCancelamento(501);
        }else if(motivo.equalsIgnoreCase("502-PEDIDO EM DUPLICIDADE")){
            cancelamentoDePedido.setCodigoDeCancelamento(502);
        }else if(motivo.equalsIgnoreCase("503-ITEM INDISPONÍVEL")){
            cancelamentoDePedido.setCodigoDeCancelamento(503);
        }else if(motivo.equalsIgnoreCase("504-RESTAURANTE SEM MOTOBOY")){
            cancelamentoDePedido.setCodigoDeCancelamento(504);
        }else if(motivo.equalsIgnoreCase("505-CARDÁPIO DESATUALIZADO")){
            cancelamentoDePedido.setCodigoDeCancelamento(505);
        }else if(motivo.equalsIgnoreCase("506-PEDIDO FORA DA ÁREA DE ENTREGA")){
            cancelamentoDePedido.setCodigoDeCancelamento(506);
        }else if(motivo.equalsIgnoreCase("507-CLIENTE GOLPISTA / TROTE")){
            cancelamentoDePedido.setCodigoDeCancelamento(507);
        }else if(motivo.equalsIgnoreCase("508-FORA DO HORÁRIO DO DELIVERY")){
            cancelamentoDePedido.setCodigoDeCancelamento(508);
        }else if(motivo.equalsIgnoreCase("509-DIFICULDADES INTERNAS DO RESTAURANTE")){
            cancelamentoDePedido.setCodigoDeCancelamento(509);
        }else if(motivo.equalsIgnoreCase("5011-ÁREA DE RISCO")){
            cancelamentoDePedido.setCodigoDeCancelamento(5011);
        }else if(motivo.equalsIgnoreCase("5012-RESTAURANTE ABRIRÁ MAIS TARD")){
            cancelamentoDePedido.setCodigoDeCancelamento(5012);
        }

        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Iniciado processo de cancelamento",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.cancelarPepdido(listener,cancelamentoDePedido,idPedidoIfood);
    }

    public void aceitarPedidoDeCanelamento(){
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Solicitação de cancelamento aceito",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.aceitarPedidoDeCanelamento(listener);
    }

    public void negarPedidoDeCanelamento(){
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {

                _Resposta.setValue(new Resposta("Solicitação de cancelamento negado com sucesso",true));
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.negarPedidoDeCanelamento(listener);
    }

    public void buscarDetalhesDoPedido(){
        APIListener<DetalhesPedido> listener = new APIListener<DetalhesPedido>() {
            @Override
            public void onSuccess(DetalhesPedido result) {

                _DetalhesPedido.setValue(result);
            }

            @Override
            public void onFailures(String mensagem) {
                _Resposta.setValue(new Resposta(mensagem));

            }
        };

        repositorio.buscarDetalhesDoPedido(listener);
    }

    public void criarPedidoIfood(LayoutPedidoIfood layoutPedidoIfood){

        if(montandoPedido(layoutPedidoIfood)){
            APIListener<RespostaPedido> listener = new APIListener<RespostaPedido>() {
                @Override
                public void onSuccess(RespostaPedido result) {

                    _RespostaPedido.setValue(result);

                    // _Resposta.setValue(new Resposta("Sucesso ao limpar eventos",true));
                }

                @Override
                public void onFailures(String mensagem) {
                    _Resposta.setValue(new Resposta(mensagem));

                }
            };

            repositorio.criarPedidoIfood(listener,layoutEnvioPedidoiFood);
        }




    }
    private Boolean montandoPedido( LayoutPedidoIfood layoutPedidoIfood){


        Cliente cliente = new Cliente();
        Entrega entrega = new Entrega();
        Telefone telefone = new Telefone();
        Coordenadas coordenadas = new Coordenadas();
        EnderecoDeEntrega enderecoDeEntrega = new EnderecoDeEntrega();
        List<Item> itens = new ArrayList<>();


        if(layoutPedidoIfood != null){
            //telefone
            String telefoneComDD = String.valueOf(layoutPedidoIfood.getUsuario().getTelefone());
            String[] numeroTelefone = telefoneComDD.split("-");

            System.out.println("xxxxxx DD "+numeroTelefone[0] + " ========");
            System.out.println("xxxx TElefone "+numeroTelefone[1]);

            telefone.setCodigoDoPais("55");
            telefone.setCodigoDeArea(numeroTelefone[0]);
            telefone.setNumero(numeroTelefone[1]);

            //cliente
            cliente.setNome(layoutPedidoIfood.getUsuario().getNome());
            cliente.setTelefone(telefone);
            // coordenadas
            coordenadas.setLatitude(layoutPedidoIfood.getEndereco().getLatitude());
            coordenadas.setLongitude(layoutPedidoIfood.getEndereco().getLongitude());
            //endereco de entrega
           enderecoDeEntrega.setNumero_da_casa(layoutPedidoIfood.getEndereco().getNumeroCasa());
            enderecoDeEntrega.setNome_da_rua(layoutPedidoIfood.getEndereco().getRua());
            enderecoDeEntrega.setBairro(layoutPedidoIfood.getEndereco().getBairro());
            enderecoDeEntrega.setCidade(layoutPedidoIfood.getEndereco().getCidade());
            enderecoDeEntrega.setEstado("PA");
            enderecoDeEntrega.setPais("BR");
            enderecoDeEntrega.setReferencia("");
            enderecoDeEntrega.setComplemento(layoutPedidoIfood.getEndereco().getComplemento());
            enderecoDeEntrega.setCodigo_postal(layoutPedidoIfood.getEndereco().getCep());
            enderecoDeEntrega.setCoordenadas(coordenadas);

            entrega.setTaxa_do_comerciante(layoutPedidoIfood.getPedido().getTaxa_entrega());



            entrega.setEnderecoDeEntrega(enderecoDeEntrega);
            //montando pedido
            float total = 0f;
            for(ItensPedido item :  layoutPedidoIfood.getItensPedidos()){
                 total = (item.getPreco() * item.getQuantidade());
                itens.add(new Item(UUID.randomUUID().toString(),item.getTitulo(),item.getId(), item.getQuantidade(), item.getPreco(),total,total));
            }



            layoutEnvioPedidoiFood.setCliente(cliente);
            layoutEnvioPedidoiFood.setEntrega(entrega);
            layoutEnvioPedidoiFood.setItens(itens);

            return  true;
        }else{
            _Resposta.setValue(new Resposta("não a dados suficientes para criar pedido iFood"));
            return false;
        }

    }

    public void verificarFreteIfood(Endereco endereco){
        if(endereco.getLatitude() != null && endereco.getLatitude() != 0 ){
            if(endereco.getLongitude() != null && endereco.getLongitude() != 0){
                System.out.println("frete: "+endereco.getLatitude()+" - "+endereco.getLongitude());
                APIListener<RespostaDisponibilidadeDeEntrega> listener = new APIListener<RespostaDisponibilidadeDeEntrega>() {
                    @Override
                    public void onSuccess(RespostaDisponibilidadeDeEntrega result) {
                        _FreteIfood.setValue(result);
                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));

                    }
                };

                repositorio.verificarFreteIfood(listener,endereco);

            }else{
                _Resposta.setValue(new Resposta("Error no endereço, por favor atulize seu dados"));
            }
        }else{
            _Resposta.setValue(new Resposta("Error no endereço, por favor atulize seu dados"));
        }

    }

    public void salvarEventos(Long idPedido, String linkAcompanhamento, EventoPedido eventoPedido){
        String idEvento = "";
        String idEntregador = "";
        String status = "";
        String data_create = "";
        String telefoneEntregador = "";
        String veiculoEntregador = "";
        String idPedidoIfood = "";
        String nomeEntregador = "";
        data_create = eventoPedido.getDataDeCriacao();
        status = eventoPedido.getCodigoCompleto();
        idEvento =  eventoPedido.getId();
        idPedidoIfood = eventoPedido.getIdDoPedido();


                        if(eventoPedido.getMetadata() != null){

                            if(eventoPedido.getMetadata().getIdEntregador() != null){
                                idEntregador =  eventoPedido.getMetadata().getIdEntregador();

                                        if(eventoPedido.getMetadata().getNomeEntregador() != null){
                                            nomeEntregador = eventoPedido.getMetadata().getNomeEntregador();

                                            if( eventoPedido.getMetadata().getTelefoneEntregador() != null){
                                                telefoneEntregador =  eventoPedido.getMetadata().getTelefoneEntregador();

                                                if(eventoPedido.getMetadata().getVeiiculoEntregador() != null ){
                                                    veiculoEntregador = eventoPedido.getMetadata().getVeiiculoEntregador();

                                                }
                                            }
                                        }


                            }
                        }


                        APIListener<Dados> listener = new APIListener<Dados>() {
                            @Override
                            public void onSuccess(Dados result) {

                                if(!result.getError().isEmpty()){
                                    _Resposta.setValue(new Resposta(result.getError()));
                                }else{
                                    if(result.getStatus()){
                                        _Resposta.setValue(new Resposta("idEvento"+result.getIdEvento(),true));
                                    }else{
                                       // _Resposta.setValue(new Resposta("Não foi possível salvar"));
                                    }
                                }

                               // _Resposta.setValue(new Resposta("error "+result.getError()));

                            }

                            @Override
                            public void onFailures(String mensagem) {
                                _Resposta.setValue(new Resposta(mensagem));
                            }
                        };
                         repositorio.salvarEventos(listener,idPedido,
                                   linkAcompanhamento,
                                   idEvento,
                                   idPedidoIfood,
                                   idEntregador,
                                   status,
                                   data_create,
                                   nomeEntregador,
                                   telefoneEntregador,
                                   veiculoEntregador);


    }

    public void getEventosIfood(Long idPedido){
        if(idPedido != null && idPedido != 0 ){
                APIListener<Dados> listener = new APIListener<Dados>() {
                    @Override
                    public void onSuccess(Dados result) {

                        if(result.getError().isEmpty()){
                            _EventosPedido.setValue(result.getEventos());
                        }else{
                            _Resposta.setValue(new Resposta(result.getError()));
                        }

                    }

                    @Override
                    public void onFailures(String mensagem) {
                        _Resposta.setValue(new Resposta(mensagem));
                    }
                };
                repositorio.getEventosIfood(listener,idPedido);

        }else{
            _Resposta.setValue(new Resposta("ID do pedido não encontrado."));
        }

    }

}
