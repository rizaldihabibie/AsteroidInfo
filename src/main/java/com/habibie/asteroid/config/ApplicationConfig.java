package com.habibie.asteroid.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class ApplicationConfig {

    @Bean
    public Retrofit retrofit() {
        // Create the logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);
        return new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
