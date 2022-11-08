package com.bergburg.bergburgdelivery.repositorio.remoto.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.model.Dados;
import com.bergburg.bergburgdelivery.model.Mensagem;
import com.bergburg.bergburgdelivery.repositorio.remoto.RetrofitClient;
import com.bergburg.bergburgdelivery.view.activity.ChatActivity;
import com.bergburg.bergburgdelivery.viewmodel.ChatViewModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private  Bundle bundle = new Bundle();
    private String idUsuario;
    private String idConversa;
    private int id = 0;
    private BergburgService remoto;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) { // aqui você não consegue abrir notificação com app encerrado
        //chamado quando recebe uma notificação
        if(notificacao.getNotification() != null){
          /*  String titulo = notificacao.getNotification().getTitle();
            String mensagem = notificacao.getNotification().getBody();

             idUsuario = notificacao.getData().get("idUsuario");
             idConversa = notificacao.getData().get("idConversa");

            if(idUsuario != null && idConversa != null){
                Long idConv = Long.parseLong(idConversa);
                Long idUser= Long.parseLong(idUsuario);

                bundle.putString(Constantes.NOME,notificacao.getNotification().getTitle());
                bundle.putLong(Constantes.ID_CONVERSA,idConv);
                bundle.putLong(Constantes.ID_USUARIO,idUser);

            }



            enviarNotificacao(titulo,mensagem);*/


        }
        System.out.println(":  Notificação recebida! -"+notificacao.getData());
        System.out.println(":  Notificação recebida! -"+notificacao.getData());
    }

    @Override // aqui abre a notificação mesmo com app encerrado
    public boolean handleIntentOnMainThread(Intent intent) {
        remoto = RetrofitClient.classService(BergburgService.class);
        String titulo = intent.getStringExtra("gcm.notification.title");
        String mensagem = intent.getStringExtra("gcm.notification.body");

        idUsuario = intent.getStringExtra("idUsuario");
        idConversa = intent.getStringExtra("idConversa");
        System.out.println(" mensagem recebida");

        if(idUsuario != null && idConversa != null){
            Long idConv = Long.parseLong(idConversa);
            Long idUser= Long.parseLong(idUsuario);
            id = Integer.parseInt(idConversa);

            bundle.putString(Constantes.NOME,intent.getStringExtra("gcm.notification.title"));
            bundle.putLong(Constantes.ID_CONVERSA,idConv);
            bundle.putLong(Constantes.ID_USUARIO,idUser);
            //vai esperar 2 segundos para enviar a notificaçao
            //ele vai espera o tempo acaba para consultar se a mensagem foi lida ou não
            //para assim enviar a notificação
            try{
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<Dados> call = remoto.getMensagens(idUser);
                        call.enqueue(new Callback<Dados>() {
                            @Override
                            public void onResponse(Call<Dados> call, Response<Dados> response) {
                                if (response.isSuccessful()) {
                                    int totalMensagens = 0 ;
                                    totalMensagens =  response.body().getMensagens().size(); // total
                                    if(totalMensagens != 0){
                                        Mensagem mMensagem = new Mensagem();
                                        mMensagem.setIdUsuario(response.body().getMensagens().get(totalMensagens - 1).getIdUsuario());
                                        mMensagem.setVisualizado(response.body().getMensagens().get(totalMensagens - 1).getVisualizado());

                                        if(mMensagem.getVisualizado() != null){
                                            if(mMensagem.getVisualizado().equalsIgnoreCase(Constantes.NAO)){ // mensagem nao lida
                                                enviarNotificacao(titulo,mensagem); // enviar notificacao
                                                System.out.println("NOTIFICAÇÃO FOI ENVIADA! ");
                                            }else{
                                                System.out.println("MENSAGEM VISUALIZADA "+mMensagem.getVisualizado());
                                            }
                                        }else{
                                            System.out.println("CORPO MENSAGEM  "+response.body().getMensagens().get(totalMensagens - 1).toString());
                                        }

                                        // System.out.println(mMensagem.getIdUsuario()+" USUÁRIO DIFERENTE "+idUser);


                                    }else{
                                        System.out.println("NOTIFICACAO - SEM MENSAGENS = "+totalMensagens);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<Dados> call, Throwable t) {
                                System.out.println("Error ao receber notificação "+t.getMessage());
                                //    listener.onFailures(Constantes.INSTABILIDADE);

                            }
                        });

                    }
                }, 2000);
            }catch (Exception e){

            }








        }
        return  true;
    }




    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //cria um token  em uma unica vez quando o app e instalado
        System.out.println("DegubApk:  token: "+token);
    }

    private void enviarNotificacao(String titulo,String mensagem) {
        //configurações para notificaçao
        String canal =getString(R.string.default_notification_channel_id);
        Uri uriSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent;
        if(Build.VERSION.SDK_INT >= 31){
            pendingIntent = PendingIntent.getActivity(this,id,intent,PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this,id,intent,PendingIntent.FLAG_ONE_SHOT);
        }

        String GROUP_KEY = "com.bergburg.bergburgdelivery.chat"+idUsuario;
        //criar notificação
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this,canal)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSmallIcon(R.drawable.logoburgs)
                .setSound(uriSom)
                //.setGroup(GROUP_KEY)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        //recuperar notificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //verificar versao do android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            long[] swPattern = new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110,
                    170, 40, 450, 110, 200, 110, 170, 40, 500};
            NotificationChannel channel = new NotificationChannel(canal,"chat",NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(swPattern);
            notificationManager.createNotificationChannel(channel);

        }

        //Enviar notificação
        notificationManager.notify(id,notificacao.build());
    }
}
