package com.challenge.networkrxretrofit.service;

import com.challenge.networkrxretrofit.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface UserService {

    @GET("todos")
    Single<List<User>> getUsers();
}
