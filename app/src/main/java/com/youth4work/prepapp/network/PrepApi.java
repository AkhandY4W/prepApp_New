package com.youth4work.prepapp.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrepApi {

    private static final String API_BASE_URL = "https://prepapi.youth4work.com/Android/";


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                httpClient.readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        } else {

            httpClient.readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }


        return retrofit.create(serviceClass);
    }

  /*  public static PrepService provideRetrofit() {

        String credentials = "YOUTH4WORKAPP" + ":" + "YOUTH4WORK@14FEB";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(
                        (chain -> {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", basic)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Host", "apis4prep.youth4work.com")
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })).addInterceptor(logging).build();



            return new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient)
                    .build().create(PrepService.class);
        }*/
}
