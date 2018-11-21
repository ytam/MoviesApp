package io.github.ytam.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ytam.R;
import io.github.ytam.adapter.SearchAdapter;
import io.github.ytam.api.APIClient;
import io.github.ytam.mvp.MainPresenter;
import io.github.ytam.mvp.MainView;
import io.github.ytam.mvp.model.search.ResultsItem;
import io.github.ytam.mvp.model.search.SearchModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class PopulerMoviesFragment extends Fragment  implements MainView,
        MaterialSearchBar.OnSearchActionListener,
        SwipeRefreshLayout.OnRefreshListener,
        PopupMenu.OnMenuItemClickListener  {

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe_refresh;
    @BindView(R.id.search_bar) MaterialSearchBar search_bar;
    @BindView(R.id.rv_movielist) RecyclerView rv_movielist;

    private SearchAdapter adapter;

    private Call<SearchModel> apiCall;
    private APIClient apiClient = new APIClient();

    private String movie_title = "";
    private int currentPage = 1;
    private int totalPages = 1;




    public PopulerMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_populer, container, false);

        ButterKnife.bind(this, view);

        search_bar.setOnSearchActionListener(this);
        swipe_refresh.setOnRefreshListener(this);

        search_bar.inflateMenu(R.menu.main);
        search_bar.getMenu().setOnMenuItemClickListener(this);

        MainPresenter presenter = new MainPresenter(this);

        setupList();
        setupListScrollListener();
        startRefreshing();



        return view;
    }

    @Override
    public void onRefresh() {

        currentPage = 1;
        totalPages = 1;

        stopRefrehing();
        startRefreshing();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

        movie_title = String.valueOf(text);
        onRefresh();
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void setupList() {
        adapter = new SearchAdapter();
        rv_movielist.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        rv_movielist.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_movielist.setAdapter(adapter);
    }

    private void setupListScrollListener() {
        rv_movielist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItems = layoutManager.getItemCount();
                int visibleItems = layoutManager.getChildCount();
                int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                if (pastVisibleItems + visibleItems >= totalItems) {
                    if (currentPage < totalPages) currentPage++;
                    startRefreshing();
                }
            }
        });
    }

    private void loadData(final String movie_title) {

//        getSupportActionBar().setSubtitle("");

        if (movie_title.isEmpty()) apiCall = apiClient.getService().getPopularMovie(currentPage);
        else apiCall = apiClient.getService().getSearchMovie(currentPage, movie_title);

        apiCall.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if (response.isSuccessful()) {
                    totalPages = response.body().getTotalPages();
                    List<ResultsItem> items = response.body().getResults();
                    showResults(response.body().getTotalResults());

                    if (currentPage > 1) adapter.updateData(items);
                    else adapter.replaceAll(items);

                    stopRefrehing();
                } else loadFailed();
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        stopRefrehing();
        Toast.makeText(getActivity(), "Failed to load data.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
    }

    private void startRefreshing() {
        if (swipe_refresh.isRefreshing()) return;
        swipe_refresh.setRefreshing(true);

        loadData(movie_title);
    }

    private void stopRefrehing() {
        if (swipe_refresh.isRefreshing()) swipe_refresh.setRefreshing(false);
    }

    private void showResults(int totalResults) {
        String results;

        String formatResults = NumberFormat.getIntegerInstance().format(totalResults);

        if (totalResults > 0) {
            results = "I found " + formatResults + " movie" + (totalResults > 1 ? "s" : "") + " for you :)";
        } else results = "Sorry! I can't find " + movie_title + " everywhere :(";

//        getSupportActionBar().setSubtitle(results);
    }
}
