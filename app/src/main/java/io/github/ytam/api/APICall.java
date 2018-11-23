

package io.github.ytam.api;

import io.github.ytam.mvp.model.detail.DetailModel;
import io.github.ytam.mvp.model.search.SearchModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {

    @GET("movie/popular?")
    Call<SearchModel> getPopularMovie(@Query("page") int page);

    @GET("movie/top_rated?")
    Call<SearchModel> getTopRatedMovie(@Query("page") int page);

    @GET("search/movie")
    Call<SearchModel> getSearchMovie(@Query("page") int page, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<DetailModel> getDetailMovie(@Path("movie_id") String movie_id);


    @GET("tv/popular?")
    Call<SearchModel> getPopularTvSeries(@Query("page") int page);

}
