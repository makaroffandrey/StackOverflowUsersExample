package com.makaroffandrey.stackoverflowusersexample.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import javax.inject.Provider;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRepoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private volatile Resource<List<User>> latestValue;

    private volatile boolean waiting = true;

    private volatile CountDownLatch latestLatch;

    private volatile Thread latestThread;

    private AppExecutor appExecutor;

    private volatile Provider<Response<ApiResponse<User>>> responseToReturn;

    private UsersApi api;

    private Observer<Resource<List<User>>> latestObserver = listResource -> latestValue =
            listResource;

    @Before
    public void setUp() {
        api = Mockito.mock(UsersApi.class);
        when(api.getUsers()).thenReturn(new Call<ApiResponse<User>>() {
            @Override
            public Response<ApiResponse<User>> execute() throws IOException {
                latestLatch.countDown();
                while (waiting) {
                }
                return responseToReturn.get();
            }

            @Override
            public void enqueue(Callback<ApiResponse<User>> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<ApiResponse<User>> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        });
        appExecutor = Mockito.mock(AppExecutor.class);
        Executor executor = command -> {
            Thread thread = new Thread(command);
            latestThread = thread;
            thread.start();
        };
        when(appExecutor.networkIO()).thenReturn(executor);
    }

    @Test(timeout = 1000)
    public void successThenErrorTest() throws InterruptedException {
        ApiResponse<User> response = new ApiResponse<>();
        ArrayList<User> items = new ArrayList<>();
        items.add(new User());
        response.items = items;
        responseToReturn = () -> Response.success(response);
        UserRepo repo = new UserRepo(api, appExecutor);
        repo.getUsers().observeForever(latestObserver);
        latestLatch = new CountDownLatch(1);
        latestLatch.await();
        assertThat(latestValue.status).isEqualTo(Status.LOADING);
        assertThat(latestValue.data).isNull();
        waiting = false;
        latestThread.join();
        assertThat(latestValue.status).isEqualTo(Status.SUCCESS);
        //On successful request we should get the result from response
        assertThat(latestValue.data).isEqualTo(response.items);
        waiting = true;
        latestLatch = new CountDownLatch(1);
        responseToReturn = () -> Response.error(500, ResponseBody.create(null, "Some error"));
        repo.update();
        latestLatch.await();
        //after calling update we should become loading again
        assertThat(latestValue.status == Status.LOADING);
        assertThat(latestValue.data).isEqualTo(items);
        waiting = false;
        latestThread.join();
        assertThat(latestValue.status).isEqualTo(Status.ERROR);
        assertThat(latestValue.data).isEqualTo(items).as("Failed requests must preserve data from" +
                " the previous successful ones");
    }

    @Test(timeout = 1000)
    public void apiErrorTest() throws InterruptedException {
        //Tests that repo treats ApiResponse with error_name as error
        ApiResponse<User> response = new ApiResponse<>();
        response.error_name = "Some error Name";
        responseToReturn = () -> Response.success(response);
        UserRepo repo = new UserRepo(api, appExecutor);
        repo.getUsers().observeForever(latestObserver);
        latestLatch = new CountDownLatch(1);
        latestLatch.await();
        assertThat(latestValue.status).isEqualTo(Status.LOADING);
        assertThat(latestValue.data).isNull();
        waiting = false;
        latestThread.join();
        final Resource<List<User>> errorValue = latestValue;
        assertThat(errorValue.status).isEqualTo(Status.ERROR);
        assertThat(errorValue.data).isNull();
    }

    @Test(timeout = 1000)
    public void emptyResponseTest() throws InterruptedException {
        //Tests that repo treats empty response as error
        responseToReturn = () -> Response.success(null);
        UserRepo repo = new UserRepo(api, appExecutor);
        repo.getUsers().observeForever(latestObserver);
        latestLatch = new CountDownLatch(1);
        latestLatch.await();
        assertThat(latestValue.status).isEqualTo(Status.LOADING);
        assertThat(latestValue.data).isNull();
        waiting = false;
        latestThread.join();
        final Resource<List<User>> errorValue = latestValue;
        assertThat(errorValue.status).isEqualTo(Status.ERROR);
        assertThat(errorValue.data).isNull();
    }

    @Test(timeout = 1000)
    public void executionErrorTest() throws InterruptedException {
        //Tests that repo treats Exception during response execution as error
        responseToReturn = () -> {
            throw new RuntimeException("Test exception");
        };
        UserRepo repo = new UserRepo(api, appExecutor);
        repo.getUsers().observeForever(latestObserver);
        latestLatch = new CountDownLatch(1);
        latestLatch.await();
        assertThat(latestValue.status).isEqualTo(Status.LOADING);
        assertThat(latestValue.data).isNull();
        waiting = false;
        latestThread.join();
        final Resource<List<User>> errorValue = latestValue;
        assertThat(errorValue.status).isEqualTo(Status.ERROR);
        assertThat(errorValue.data).isNull();
    }

    @Test(timeout = 1000)
    public void doubleUpdateTest() throws InterruptedException {
        ApiResponse<User> response = new ApiResponse<>();
        ArrayList<User> items = new ArrayList<>();
        items.add(new User());
        response.items = items;
        responseToReturn = () -> Response.success(response);
        UserRepo repo = new UserRepo(api, appExecutor);
        repo.getUsers().observeForever(latestObserver);
        latestLatch = new CountDownLatch(1);
        latestLatch.await();
        assertThat(latestValue.status).isEqualTo(Status.LOADING);
        assertThat(latestValue.data).isNull();
        repo.update();
        //calling update while we are still loading does not trigger new request
        verify(api, times(1)).getUsers();
        waiting = false;
        latestThread.join();
        final Resource<List<User>> successValue = latestValue;
        assertThat(successValue.status).isEqualTo(Status.SUCCESS);
        //On successful request we should get the result from response
        assertThat(successValue.data).isEqualTo(response.items);
    }
}
