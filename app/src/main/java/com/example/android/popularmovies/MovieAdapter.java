package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private int mNumberItems;
    private List<Movie> mMovieList;

    public MovieAdapter() {
        this.mNumberItems = 0;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.list_item_grid_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        // TODO 2. bind data, example ViewHolder.bind
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public void setMovieData(List<Movie> movieList) {
        mMovieList = movieList;
        mNumberItems = mMovieList.size();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView starsMovieTextView;
        private ImageView movieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title_movie_tv);
            starsMovieTextView = itemView.findViewById(R.id.stars_movie_tv);
            movieImageView = itemView.findViewById(R.id.image_movie_iv);
        }

        // TODO 2. bind method ?
        void bind(int position) {
            Movie movie = mMovieList.get(position);
            titleTextView.setText(movie.getTitle());
            starsMovieTextView.setText(movie.getUserRating());

            Picasso.get().load(movie.getMoviePosterUri()).into(movieImageView);
        }



    }
}
