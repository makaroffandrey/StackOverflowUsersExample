package com.makaroffandrey.stackoverflowusersexample.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.makaroffandrey.stackoverflowusersexample.repo.Resource;
import com.makaroffandrey.stackoverflowusersexample.repo.User;
import com.makaroffandrey.stackoverflowusersexample.repo.UserRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersListViewModelTest {

    private UserRepo repo;

    private LiveData<Resource<List<User>>> liveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        repo = Mockito.mock(UserRepo.class);
        when(repo.getUsers()).thenReturn(liveData);
    }

    @Test
    public void proxyingTest() {
        //checks that this viewModel is a simple proxy of callbacks to th repo
        UsersListViewModel model = new UsersListViewModel(repo);
        assertThat(model.getUsers()).isEqualTo(liveData);
        verify(repo, times(0)).update();
        model.update();
        verify(repo, times(1)).update();
    }
}
