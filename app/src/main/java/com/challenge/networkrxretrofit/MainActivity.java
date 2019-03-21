package com.challenge.networkrxretrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.challenge.networkrxretrofit.repo.Repository;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button requestButton;
    private ProgressBar progressBar;
    private RecyclerView userRecyclerView;
    private UserAdapter adapter;

    private Repository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new Repository(this);

        initViews();
    }

    private void initViews() {
        requestButton = findViewById(R.id.request_button);
        requestButton.setOnClickListener(this);
        userRecyclerView = findViewById(R.id.recyclerview);
        progressBar  = findViewById(R.id.progress_circular);

        adapter = new UserAdapter();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        userRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        disposable.add(repository.getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(todoList -> progressBar.setVisibility(View.GONE))
                .subscribe(userList -> {
                    adapter.setUserItems(userList);
                    repository.insertUsers(userList);
                }, throwable -> {
                    Log.d("TAG_1", throwable.getMessage());
                    progressBar.setVisibility(View.GONE);
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposable.add(repository.getLocalUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (!users.isEmpty()) {
                        adapter.setUserItems(users);
                    }
                    else {
                        Toast.makeText(this, "Empty Database", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        repository.onDestroy();
    }
}
