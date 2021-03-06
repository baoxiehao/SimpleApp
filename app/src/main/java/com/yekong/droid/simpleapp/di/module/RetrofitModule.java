package com.yekong.droid.simpleapp.di.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yekong.droid.simpleapp.di.Gank;
import com.yekong.droid.simpleapp.di.Github;
import com.yekong.droid.simpleapp.di.Rss;
import com.yekong.droid.simpleapp.di.ZhiHu;

import java.lang.reflect.Field;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module
public class RetrofitModule {
    @Provides
    @Singleton
    @Gank
    public Retrofit provideGankRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
                ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Provides
    @Singleton
    @ZhiHu
    public Retrofit providerZhiHuRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/api/4/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat("yyyyMMdd").create()
                ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Provides
    @Singleton
    @Rss
    public Retrofit provideRssRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.31.71:4000/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00").create()
                ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Provides
    @Singleton
    @Github
    public Retrofit provideGithubRetrofit(@Github Retrofit.Builder builder) {
        return builder.baseUrl("https://api.github.com/").build();
    }

    @Provides
    @Singleton
    @Github
    public Retrofit.Builder provideGithubRetrofitBuilder(@Github Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(converterFactory);
    }

    @Provides
    @Singleton
    @Github
    public Converter.Factory provideGithubConverterFactory(@Github Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    @Github
    Gson provideGithubGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setFieldNamingStrategy(new CustomFieldNamingPolicy())
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializeNulls()
                .create();
    }

    private static class CustomFieldNamingPolicy implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            String name = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);
            // Strip out the m_ prefix
            name = name.substring(2, name.length()).toLowerCase();
            return name;
        }
    }
}
