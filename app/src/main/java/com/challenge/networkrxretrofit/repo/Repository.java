package com.challenge.networkrxretrofit.repo;

import com.challenge.networkrxretrofit.model.User;
import com.challenge.networkrxretrofit.service.RetrofitProvider;
import com.challenge.networkrxretrofit.service.UserService;

import java.util.List;

import io.reactivex.Single;

public class Repository {

    RetrofitProvider retrofitProvider;
    private UserService userService;

    public Repository() {
        retrofitProvider = RetrofitProvider.getInstance();
        userService = retrofitProvider.getUserService();
    }

    public Single<List<User>> getUserList() {
        return userService.getUsers();
    }

    public void onDestroy() {
        retrofitProvider.destroy();
    }
}
