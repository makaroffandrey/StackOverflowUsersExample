package com.makaroffandrey.stackoverflowusersexample;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.makaroffandrey.stackoverflowusersexample.di.AppComponent;
import com.makaroffandrey.stackoverflowusersexample.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class StackOverflowExampleApplication extends DaggerApplication {
    private final AppComponent component;

    public StackOverflowExampleApplication() {
        super();
        component = DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return component;
    }
}
