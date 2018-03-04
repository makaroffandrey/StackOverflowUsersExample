package com.makaroffandrey.stackoverflowusersexample.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

@Singleton
public class UserRepo {

    private final MutableLiveData<Resource<List<User>>> liveData = new MutableLiveData<>();

    private final UsersApi api;

    private final Executor executor;

    @Inject
    UserRepo(UsersApi api, AppExecutor appExecutor) {
        this.api = api;
        executor = appExecutor.networkIO();
    }

    public void update() {
        final Resource<List<User>> value = liveData.getValue();
        if (value != null && value.status == Status.LOADING) {
            return;
        }
        executor.execute(() -> {
            try {
                final List<User> previousData = value != null ? value.data : null;
                liveData.postValue(Resource.loading(previousData));
                Response<ApiResponse<User>> response = api.getUsers().execute();
                if (!response.isSuccessful()) {
                    String error = response.errorBody() != null ? response.errorBody().string
                            () : response.message();
                    liveData.postValue(Resource.error(new IOException(error), previousData));
                } else if (response.body() == null) {
                    liveData.postValue(Resource.error(new IOException("Empty response"),
                            previousData));
                } else if (response.body().error_name != null || response.body().error_message !=
                        null) {
                    liveData.postValue(Resource.error(new ApiException(response.body()
                            .error_name, response.body().error_message), previousData));
                } else {
                    liveData.postValue(Resource.success(response.body().items));
                }
            } catch (Exception e) {
                liveData.postValue(Resource.error(e, value != null ? value.data : null));
            }
        });
    }

    public LiveData<Resource<List<User>>> getUsers() {
        if (liveData.getValue() == null) {
            update();
        }
        return liveData;
    }
}
