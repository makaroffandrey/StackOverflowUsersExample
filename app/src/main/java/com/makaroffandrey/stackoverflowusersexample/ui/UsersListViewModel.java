package com.makaroffandrey.stackoverflowusersexample.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.makaroffandrey.stackoverflowusersexample.repo.Resource;
import com.makaroffandrey.stackoverflowusersexample.repo.User;
import com.makaroffandrey.stackoverflowusersexample.repo.UserRepo;

import java.util.List;

import javax.inject.Inject;

public class UsersListViewModel extends ViewModel {

    private final UserRepo repo;

    @Inject
    UsersListViewModel(UserRepo repo) {
        this.repo = repo;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        return repo.getUsers();
    }

    public void update() {
        repo.update();
    }

}
