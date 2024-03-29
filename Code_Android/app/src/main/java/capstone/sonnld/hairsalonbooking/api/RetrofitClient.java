package capstone.sonnld.hairsalonbooking.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit ourInstance ;

    private static final String BASE_URL = "http://192.168.1.7:8080/api/";

    public static Retrofit getInstance() {

        if(ourInstance == null){
            ourInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ourInstance;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    private RetrofitClient() {

    }
}
