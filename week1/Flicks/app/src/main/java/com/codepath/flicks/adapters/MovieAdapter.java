package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.ContentValues.TAG;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {


    // View lookup cache
    public static class ViewHolder {
        @BindView(R.id.tv_title) TextView title;
        @BindView(R.id.tv_overview) TextView overview;
        @BindView(R.id.iv_posterImage) ImageView poster;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public static class ViewHolder2 {
        @BindView(R.id.iv_posterImage) ImageView poster1;
        public ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    private enum LayoutType{
        NORMAL,
        POPULAR
    }
    private final int POPULAR_STAR_RATING = 5;

    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return LayoutType.values().length;
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {
        return (getItem(position).getVoteAverage() > POPULAR_STAR_RATING)? LayoutType.POPULAR.ordinal(): LayoutType.NORMAL.ordinal();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item type for this position
        int type = getItemViewType(position);


        if(LayoutType.NORMAL.ordinal() == type) {
            Movie movie = getItem(position);

            ViewHolder viewHolder;

            if(convertView == null) {
                convertView = getInflatedLayoutForType(type, parent);
                viewHolder = new ViewHolder(convertView);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object
            viewHolder.title.setText(movie.getTitle());
            viewHolder.overview.setText(movie.getOverview());
            String image = null;
            int orientation = getContext().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                image = movie.getPosterPath();
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                image = movie.getBackdropPath();
            }

            Picasso.with(getContext()).load(image).transform(new RoundedCornersTransformation(10,10)).into(viewHolder.poster);

        }
        else if(LayoutType.POPULAR.ordinal() == type) {
            Movie movie = getItem(position);

            ViewHolder2 viewHolder;

            if(convertView == null) {
                convertView = getInflatedLayoutForType(type, parent);
                viewHolder = new ViewHolder2(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder2) convertView.getTag();
            }

            // Populate the data into the template view using the data object
            String image = movie.getBackdropPath();

            Picasso.with(getContext()).load(image).transform(new RoundedCornersTransformation(10,10)).into(viewHolder.poster1);
        }


        // Return the completed view to render on screen
        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        View selectedView = null;

        if (type == 0) {
            selectedView =  LayoutInflater.from(getContext()).inflate(R.layout.item_box_office_movie, parent, false);
        } else if (type == 1) {
            selectedView =  LayoutInflater.from(getContext()).inflate(R.layout.item_box_office_movie_popular, parent, false);
        }
        else {
            Log.e(TAG, "Layout Type is Invalid");
        }

        return selectedView;
    }
}
