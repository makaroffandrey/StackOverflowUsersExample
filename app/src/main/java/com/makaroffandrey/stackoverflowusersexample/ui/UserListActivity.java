package com.makaroffandrey.stackoverflowusersexample.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.makaroffandrey.stackoverflowusersexample.R;
import com.makaroffandrey.stackoverflowusersexample.repo.User;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * An activity representing a list of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link UserDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class UserListActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UsersListViewModel viewModel;

    private SwipeRefreshLayout refreshLayout;

    private RecyclerView usersList;

    @Nullable
    private Snackbar snackbar;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        refreshLayout = findViewById(R.id.users_refresh);
        usersList = findViewById(R.id.user_list);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UsersListViewModel.class);
        viewModel.getUsers().observe(this, listResource -> {
            if (listResource == null) {
                showError(null);
            } else {
                switch (listResource.status) {
                    case LOADING:
                        showLoading(listResource.data);
                        break;
                    case ERROR:
                        showError(listResource.data);
                        break;
                    case SUCCESS:
                        showList(listResource.data);
                        break;
                }
            }
        });
        refreshLayout.setOnRefreshListener(() -> viewModel.update());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());

        if (findViewById(R.id.user_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @Nullable List<User> users) {
        if (users == null || users.isEmpty()) {
            usersList.setAdapter(new EmptyAdapter());
        } else {
            recyclerView.setAdapter(new UsersRecyclerViewAdapter(this, users, twoPane));
        }
    }

    private void showError(@Nullable List<User> users) {
        refreshLayout.setRefreshing(false);
        snackbar = Snackbar.make(findViewById(R.id.coordinator), R.string.load_error, Snackbar
                .LENGTH_INDEFINITE);
        snackbar.show();
        setupRecyclerView(usersList, users);
    }

    private void showLoading(@Nullable List<User> users) {
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = null;
        }
        refreshLayout.setRefreshing(true);
        setupRecyclerView(usersList, users);
    }

    private void showList(@Nullable List<User> users) {
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = null;
        }
        refreshLayout.setRefreshing(false);
        setupRecyclerView(usersList, users);
    }

    static class EmptyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int
                viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R
                    .layout.users_empty, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

}
