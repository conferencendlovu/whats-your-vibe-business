package za.co.whatsyourvibe.business.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://wyv-api.azurewebsites.net/";
    //private static final String BASE_URL = "http://192.168.8.101:3000/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                            .connectTimeout(15, TimeUnit.SECONDS)
                                            .readTimeout(20, TimeUnit.SECONDS)
                                            .writeTimeout(20, TimeUnit.SECONDS)
                                            .build();
        Gson gson = new GsonBuilder().setLenient().create();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                               .baseUrl(BASE_URL)
                               .addConverterFactory(GsonConverterFactory.create(gson))
                               .client(okHttpClient)
                               .build();
        }

        return  retrofit;
    }
}
