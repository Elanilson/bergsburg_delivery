package com.bergburg.bergburgdelivery.repositorio.remoto;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private RetrofitClient(){}

    private static Retrofit INSTACE;
    private static OkHttpClient client = new OkHttpClient();
    private static Retrofit getINSTACE(){
        if(INSTACE == null){
            INSTACE = new Retrofit.Builder()
                    .baseUrl("https://apkdoandroidonline.com/bergs_burg_delivery/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return INSTACE;
    }
    public static void CancelarRequisicoes(){
        System.out.println("Cancelar todas as requisições");
       // System.out.println("Cancelando todas as requisicoes 1-total: "+ client.dispatcher().getMaxRequestsPerHost());
        client.dispatcher().cancelAll();
       // client.dispatcher().getMaxRequests();
      //  System.out.println("Cancelando todas as requisicoes 2-total: "+ client.dispatcher());
    }

    public static <T> T classService(Class<T> classService){
        return getINSTACE().create(classService);
    }

}
