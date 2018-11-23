

package io.github.ytam.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.github.ytam.BuildConfig;
import io.github.ytam.R;
import io.github.ytam.api.APIClient;
import io.github.ytam.mvp.model.detail.DetailModel;
import io.github.ytam.mvp.model.search.ResultsItem;
import io.github.ytam.utils.DateTime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_ITEM = "movie_item";

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.img_backdrop) ImageView img_backdrop;
    @BindView(R.id.img_poster) ImageView img_poster;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote) TextView tv_vote;
    @BindViews({R.id.img_star1, R.id.img_star2, R.id.img_star3, R.id.img_star4, R.id.img_star5}) List<ImageView> img_vote;

    @BindView(R.id.tv_genres) TextView tv_genres;
    @BindView(R.id.tv_overview) TextView tv_overview;

    private Call<DetailModel> apiCall;
    private APIClient apiClient = new APIClient();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsing_toolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        String movie_item = getIntent().getStringExtra(MOVIE_ITEM);
        loadData(movie_item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
    }

    private void loadData(String movie_item) {
        ResultsItem item = gson.fromJson(movie_item, ResultsItem.class);
        loadDataInServer(String.valueOf(item.getId()));

        getSupportActionBar().setTitle(item.getTitle());
        tv_title.setText(item.getTitle());

        Glide.with(DetailActivity.this)
                .load(BuildConfig.BASE_URL_IMG + "w185" + item.getBackdropPath())
                .into(img_backdrop);

        Glide.with(DetailActivity.this)
                .load(BuildConfig.BASE_URL_IMG + "w154" + item.getPosterPath())
                .into(img_poster);

        tv_release_date.setText(DateTime.getLongDate(item.getReleaseDate()));
        tv_vote.setText(String.valueOf(item.getVoteAverage()));
        tv_overview.setText(item.getOverview());

        double userRating = item.getVoteAverage() / 2;
        int integerPart = (int) userRating;

        // Fill stars
        for (int i = 0; i < integerPart; i++) {
            img_vote.get(i).setImageResource(R.drawable.ic_star_black_24dp);
        }

        // Fill half star
        if (Math.round(userRating) > integerPart) {
            img_vote.get(integerPart).setImageResource(R.drawable.ic_star_half_black_24dp);
        }
    }

    private void loadDataInServer(String movie_item) {
        apiCall = apiClient.getService().getDetailMovie(movie_item);
        apiCall.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel item = response.body();

                    int size = 0;

                    String genres = "";
                    size = item.getGenres().size();
                    for (int i = 0; i < size; i++) {
                        genres += "√ " + item.getGenres().get(i).getName() + (i + 1 < size ? "\n" : "");
                    }
                    tv_genres.setText(genres);

                } else loadFailed();
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {

        Toast.makeText(DetailActivity.this, "Cannot fetch detail movie.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
    }
}
