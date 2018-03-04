package com.makaroffandrey.stackoverflowusersexample.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.makaroffandrey.stackoverflowusersexample.ui.UsersListViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UsersListViewModel.class)
    abstract ViewModel bindMainActivityViewModel(UsersListViewModel viewModel);

    @Binds
    @Singleton
    abstract ViewModelProvider.Factory bindViewModelFactory(ProviderViewModelFactory factory);
}
