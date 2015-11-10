package popularmovies.portfolio.jpabon.com.popularmoviesv3.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters.ReviewAdapter;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters.TrailerAdapter;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Movie;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Review;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Video;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.helpers.HelperReviews;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.helpers.HelperVideos;

import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.SHARED_PREFERENCE_FAVORITES_KEY;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.SHARED_PREFERENCE_POPULAR_MOVIES;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.movieDB_reviews;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.movieDB_videos;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.movieDB_w342;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Converters.getFormattedDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    Movie movieDetails;
    ArrayList<Video> trailers;
    ArrayList<Review> reviews;

    ListView lvTrailers;
    ListView lvReviews;

    TextView tvNoTrailers;
    TextView tvNoReviews;

    Button btAddFavorite;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle == null) {
            return;
        }

        if (getArguments().containsKey("movie_details")) {
            movieDetails = getArguments().getParcelable("movie_details");
        }
    }
    
    private void isFavorite(){
        SharedPreferences pref = getActivity().getSharedPreferences(SHARED_PREFERENCE_POPULAR_MOVIES, Activity.MODE_PRIVATE); // 0 - for private mode
        Set movies = new HashSet();
        movies = pref.getStringSet(SHARED_PREFERENCE_FAVORITES_KEY, null);

        if (movies != null) {
            Gson gson = new Gson();

            for (Object movie : movies) {
                Movie temp = gson.fromJson(movie.toString(), Movie.class);

                if (temp.getId() == movieDetails.getId()) {
                    doFavorite();
                    break;
                }
            }
        }
    }

    private void doFavorite(){
        btAddFavorite.setText(R.string.button_favorite);
        btAddFavorite.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_details, container, false);

        ImageView ivBackdropPath = (ImageView) rootView.findViewById(R.id.ivBackdrop);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.ivPoster);

        TextView tvOriginalTitle = (TextView) rootView.findViewById(R.id.tvOriginalTitle);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.tvReleaseDate);
        TextView tvUserRating = (TextView) rootView.findViewById(R.id.tvUserRating);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.tvOverview);

        btAddFavorite = (Button) rootView.findViewById(R.id.btFavorites);

        lvTrailers = (ListView) rootView.findViewById(R.id.lvTrailers);
        lvTrailers.setAdapter(new TrailerAdapter(getActivity()));

        lvReviews = (ListView) rootView.findViewById(R.id.lvReviews);
        lvReviews.setAdapter(new ReviewAdapter(getActivity()));

        tvNoTrailers = (TextView) rootView.findViewById(R.id.tvNoTrailers);
        tvNoReviews = (TextView) rootView.findViewById(R.id.tvNoReviews);

        lvTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = ((Video) parent.getAdapter().getItem(position)).getKey();
                Intent intent;

                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                } catch (ActivityNotFoundException ex) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
                }

                startActivity(intent);
            }
        });


        lvReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((Review) parent.getAdapter().getItem(position)).getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                startActivity(intent);
            }
        });

        if (movieDetails != null) {
            Picasso.with(getActivity()).load(movieDetails.getBackdropPathInSize(movieDB_w342)).into(ivBackdropPath);
            Picasso.with(getActivity())
                    .load(movieDetails.getPosterPathInSize(movieDB_w342))
                    .error(R.drawable.abc_ic_clear_mtrl_alpha)
                    .into(ivPoster);

            tvOriginalTitle.setText(movieDetails.getOriginal_title());
            tvReleaseDate.setText(getFormattedDate(movieDetails.getRelease_date()));
            tvUserRating.setText(String.valueOf(movieDetails.getVote_average()));
            tvOverview.setText(movieDetails.getOverview());

            btAddFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getActivity().getSharedPreferences(SHARED_PREFERENCE_POPULAR_MOVIES, Activity.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    Set movies = pref.getStringSet(SHARED_PREFERENCE_FAVORITES_KEY, null);

                    if (movies == null) {
                        movies = new HashSet();
                    } else {
                        movies = new HashSet<String>(pref.getStringSet(SHARED_PREFERENCE_FAVORITES_KEY, null));
                    }

                    movies.add(new Gson().toJson(movieDetails));

                    editor.putStringSet(SHARED_PREFERENCE_FAVORITES_KEY, movies);
                    editor.commit();

                    doFavorite();
                    Toast.makeText(getActivity(), movieDetails.getTitle() + " added to favorites.", Toast.LENGTH_SHORT).show();

                }
            });

            isFavorite();
            executeQueryExtras(movieDetails.getId());
        }

        return rootView;
    }

    public void executeQueryExtras(long id_movie) {
        boolean onLine = isOnline();

        if (onLine) {
            HelperVideos helperTrailers = new HelperVideos();
            helperTrailers.execute(new Object[]{movieDB_videos, id_movie, lvTrailers, tvNoTrailers, getActivity()});

            HelperReviews helperReviews = new HelperReviews();
            helperReviews.execute(new Object[]{movieDB_reviews, id_movie, lvReviews, tvNoReviews, getActivity()});
        }

        lvTrailers.setVisibility(onLine ? View.VISIBLE : View.GONE);
        lvReviews.setVisibility(onLine ? View.VISIBLE : View.GONE);

        tvNoTrailers.setVisibility(onLine ? View.GONE : View.VISIBLE);
        tvNoReviews.setVisibility(onLine ? View.GONE : View.VISIBLE);
    }

    /*
    * From: https://developer.android.com/training/basics/network-ops/managing.html
    * On: 2015-09-22
    * */
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
