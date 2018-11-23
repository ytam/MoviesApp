
package io.github.ytam.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ytam.BuildConfig;
import io.github.ytam.ui.activity.DetailActivity;
import io.github.ytam.mvp.model.search.ResultsItem;
import io.github.ytam.utils.DateTime;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(io.github.ytam.R.id.img_poster) ImageView img_poster;
    @BindView(io.github.ytam.R.id.tv_title) TextView tv_title;
    @BindView(io.github.ytam.R.id.tv_overview) TextView tv_overview;
    @BindView(io.github.ytam.R.id.tv_release_date) TextView tv_release_date;

    public SearchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final ResultsItem item) {
        tv_title.setText(item.getTitle());
        tv_overview.setText(item.getOverview());
        tv_release_date.setText(DateTime.getLongDate(item.getReleaseDate()));
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + "w45" + item.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(io.github.ytam.R.drawable.placeholder)
                        .centerCrop())
                .into(img_poster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(item));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) itemView.getContext(), img_poster, "poster");
                    itemView.getContext().startActivity(intent, activityOptionsCompat.toBundle());
                } else itemView.getContext().startActivity(intent);
            }
        });
    }
}
