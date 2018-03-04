package com.makaroffandrey.stackoverflowusersexample.di;

import com.makaroffandrey.stackoverflowusersexample.StackOverflowExampleApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityModule.class, NetworkModule
        .class, ViewModelModule.class})
public interface AppComponent extends AndroidInjector<StackOverflowExampleApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(StackOverflowExampleApplication application);

        AppComponent build();
    }
}
