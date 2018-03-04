package com.makaroffandrey.stackoverflowusersexample.di;

import com.makaroffandrey.stackoverflowusersexample.repo.UsersApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
class NetworkModule {

    @Provides
    @Singleton
    UsersApi provideUsersApi() {
        return new Retrofit.Builder().baseUrl("https://api.stackexchange.com")
                .addConverterFactory(JacksonConverterFactory.create()).build().create(UsersApi
                        .class);
    }
}
