package com.bergburg.bergburgdelivery.repositorio.remoto;

import android.content.Context;

import com.bergburg.bergburgdelivery.helpers.DadosIFoodPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientIFood {
    private RetrofitClientIFood(){}
    private static String token = "";



    private static Retrofit INSTACE;
    private static Retrofit getINSTACE(  ){
        System.out.println("Token x recebido "+token);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization","Bearer "+token)
                        .build();
                return chain.proceed(request);
            }
        });


        if(INSTACE == null){
            INSTACE = new Retrofit.Builder()
                    .baseUrl("https://merchant-api.ifood.com.br/")
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTACE;
    }


    public static  <T> T classService(Class<T> classService ){
        return getINSTACE().create(classService);
    }
    public static void novoToken(String novoToken){
        token = novoToken;
    }
}
