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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bergburg.bergburgdelivery.Constantes.Constantes;
import com.bergburg.bergburgdelivery.R;
import com.bergburg.bergburgdelivery.view.activity.ChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private  Bundle bundle = new Bundle();
    private String idUsuario;
    private String idConversa;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {
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
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx:  Notificação recebida! -"+notificacao.getData());
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx:  Notificação recebida! -"+notificacao.getData());
    }

    @Override
    public boolean handleIntentOnMainThread(Intent intent) {
        String titulo = intent.getStringExtra("gcm.notification.title");
        String mensagem = intent.getStringExtra("gcm.notification.body");

        idUsuario = intent.getStringExtra("idUsuario");
        idConversa = intent.getStringExtra("idConversa");

        if(idUsuario != null && idConversa != null){
            Long idConv = Long.parseLong(idConversa);
            Long idUser= Long.parseLong(idUsuario);

            bundle.putString(Constantes.NOME,intent.getStringExtra("gcm.notification.title"));
            bundle.putLong(Constantes.ID_CONVERSA,idConv);
            bundle.putLong(Constantes.ID_USUARIO,idUser);

            enviarNotificacao(titulo,mensagem);
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
            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        }

        String GROUP_KEY = "com.bergburg.bergburgdelivery.chat"+idUsuario;
        System.out.println("Grupo xxxxxxxxxxxx "+idUsuario);
        //criar notificação
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this,canal)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSmallIcon(R.drawable.logoburgs)
                .setSound(uriSom)
                .setGroup(GROUP_KEY)
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
        notificationManager.notify(0,notificacao.build());
    }
}
