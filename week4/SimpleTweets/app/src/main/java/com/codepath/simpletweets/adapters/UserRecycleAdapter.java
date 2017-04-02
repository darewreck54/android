package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.models.TwitterError;
import com.codepath.simpletweets.models.User;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.ParseRelativeDate;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

public class UserRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<User> mUsers;
    private Context mContext;
    private LayoutInflater inflater;
    private static final String TAG = UserRecycleAdapter.class.getName();
    private static TwitterClient twitterClient;
    private ViewHolder viewHolder;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvUserName;

        @BindView(R.id.tvScreenName)
        TextView tvUserScreenName;

        @BindView(R.id.ivProfileImg)
        ImageView ivProfileIcon;

        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public UserRecycleAdapter(Context context, List<User> users) {
        mUsers = users;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_user, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(view);
        twitterClient = TwitterApplication.getRestClient();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        viewHolder = (ViewHolder) holder;
        viewHolder.tvDescription.setText(user.description);
        viewHolder.tvUserName.setText(user.name);
        viewHolder.tvUserScreenName.setText("@" + user.screenName);
        Glide.with(getContext()).load(user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(viewHolder.ivProfileIcon);
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    private Context getContext() {
        return mContext;
    }

    // Clean all elements of the recycler
    public void clear() {
        TweetDbFlowAdapter.clearTable();
        mUsers.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<User> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }

}
