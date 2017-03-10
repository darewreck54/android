package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
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

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView poster;
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_box_office_movie, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.poster = (ImageView) convertView.findViewById(R.id.ivPosterImage);
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

        Picasso.with(getContext()).load(image).into(viewHolder.poster);

        // Return the completed view to render on screen
        return convertView;
    }

}
