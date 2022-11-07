package com.bergburg.bergburgdelivery.repositorio.remoto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientNotificacao {
    private RetrofitClientNotificacao(){}

    private static Retrofit INSTACE;
    private static Retrofit getINSTACE(){
        if(INSTACE == null){
            INSTACE = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTACE;
    }

    public static <T> T classService(Class<T> classService){
        return getINSTACE().create(classService);
    }

}
