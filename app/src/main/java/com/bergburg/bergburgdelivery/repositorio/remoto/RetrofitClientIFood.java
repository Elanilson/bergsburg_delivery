package com.bergburg.bergburgdelivery.repositorio.remoto;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientIFood {
    private RetrofitClientIFood(){}

    private static Retrofit INSTACE;
    private static Retrofit getINSTACE(){

     String tokenAcesse = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI1OGJmNmY4Yy02YmUxLTRiNTYtYjUxYi03MzYyMzUzMDhjOTkiLCJhdWQiOlsic2hpcHBpbmciLCJjYXRhbG9nIiwicmV2aWV3IiwiZmluYW5jaWFsIiwibWVyY2hhbnQiLCJvcmRlciIsIm9hdXRoLXNlcnZlciJdLCJhcHBfbmFtZSI6IjU4YmY2ZjhjLTZiZTEtNGI1Ni1iNTFiLTczNjIzNTMwOGM5OSIsIm93bmVyX25hbWUiOiIiLCJzY29wZSI6WyJzaGlwcGluZyIsImNhdGFsb2ciLCJyZXZpZXciLCJtZXJjaGFudCIsIm9yZGVyIiwiY29uY2lsaWF0b3IiXSwiaXNzIjoiaUZvb2QiLCJtZXJjaGFudF9zY29wZSI6WyJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6bWVyY2hhbnQiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6Y2F0YWxvZyIsImRmNmIwNDhkLTM1NzAtNDI4Ny05MjhhLTk0NmM1ZDBkMjc0MjpyZXZpZXciLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6b3JkZXIiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6Y29uY2lsaWF0b3IiLCJkZjZiMDQ4ZC0zNTcwLTQyODctOTI4YS05NDZjNWQwZDI3NDI6c2hpcHBpbmciXSwiZXhwIjoxNjcwODA0NTc5LCJpYXQiOjE2NzA3ODI5NzksImp0aSI6IjU4YmY2ZjhjLTZiZTEtNGI1Ni1iNTFiLTczNjIzNTMwOGM5OSIsIm1lcmNoYW50X3Njb3BlZCI6dHJ1ZSwiY2xpZW50X2lkIjoiNThiZjZmOGMtNmJlMS00YjU2LWI1MWItNzM2MjM1MzA4Yzk5In0.CO-hPEcTqII3F1_Xy7i8cGcIdYIuTEDDaGK68Xva_3FfBMUxLlTGgQTOebMFSa8UjnwiCzCyu4r3B8u4NCPem1SeahCZtuofA5nVIhsDC8fawkF6a27tCD-CO24DZwdpxEGZZ3i8-a1q6NmUfuJ-Bslyq81OcB970FLw6AFVAXA";
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization","Bearer "+tokenAcesse)
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



    public static  <T> T classService(Class<T> classService){
        return getINSTACE().create(classService);
    }
}
