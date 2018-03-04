package com.makaroffandrey.stackoverflowusersexample.repo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersApi {
    @GET("/2.2/users?pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    Call<ApiResponse<User>> getUsers();
}
