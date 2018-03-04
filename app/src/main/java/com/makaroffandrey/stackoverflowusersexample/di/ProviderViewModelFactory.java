package com.makaroffandrey.stackoverflowusersexample.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
class ProviderViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    ProviderViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators
                    .entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            @SuppressWarnings("unchecked") T t = (T) creator.get(); //The map of creators is
            // populated fixed at runtime, therefore it either works correctly every time or
            // crashes every time. Dagger configuration is responsible for correctness.
            if (t == null) {
                throw new IllegalStateException("Creator that was supposed to provide a ViewModel" +
                        " failed to do so");
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

