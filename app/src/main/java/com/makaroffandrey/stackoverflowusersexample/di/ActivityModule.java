package com.makaroffandrey.stackoverflowusersexample.di;

import com.makaroffandrey.stackoverflowusersexample.ui.UserDetailActivity;
import com.makaroffandrey.stackoverflowusersexample.ui.UserListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract UserListActivity provideMainActivity();

    @ContributesAndroidInjector
    abstract UserDetailActivity provideDetailsActivity();
}
