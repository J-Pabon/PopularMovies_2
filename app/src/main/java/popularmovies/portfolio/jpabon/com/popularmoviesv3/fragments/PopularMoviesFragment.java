package popularmovies.portfolio.jpabon.com.popularmoviesv3.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.activities.DetailsActivity;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.activities.PopularMoviesActivity;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters.PosterAdapter;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Movie;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.helpers.HelperMovies;

import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {
    ArrayList<Movie> moviesInfo;
    GridView gvPopMovies;

    TextView tvError;
    Button btRetry;

    PosterAdapter adapterPosters;

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get courses array from argument
        if (getArguments() != null) {
            moviesInfo = getArguments().getParcelableArrayList("movies_info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_movies, container, false);

        gvPopMovies = (GridView) rootView.findViewById(R.id.gvPopMovies);
        gvPopMovies.setAdapter(new PosterAdapter(getActivity()));
        gvPopMovies.setNumColumns(GridView.AUTO_FIT);

        tvError = (TextView) rootView.findViewById(R.id.tvError);
        btRetry = (Button) rootView.findViewById(R.id.btRetry);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQuery(movieDB_discovery, movieDB_filter_date_2015, movieDB_sort_popularity);
            }
        });

        gvPopMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (PopularMoviesActivity.mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
                    Bundle arguments = new Bundle();
                    // Pass the selected Golfcourse object to the DetailFragment
                    arguments.putParcelable("movie_details", (Movie) parent.getAdapter().getItem(position));

                    DetailsFragment fragment = new DetailsFragment();

                    fragment.setArguments(arguments);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.movies_details, fragment)
                            .commit();

                } else {
                    // In single-pane mode, simply start the detail activity
                    // for the selected item ID.
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);

                    intent.putExtra("movie_details", (Movie) parent.getAdapter().getItem(position));

                    startActivity(intent);
                }

            }
        });

        if (moviesInfo == null) {
            executeQuery(movieDB_discovery, movieDB_filter_date_2015, movieDB_sort_popularity);
        }

        return rootView;
    }

    public void executeQuery(String type, String filter, String sorting) {
        if (sorting == favorites) {
            SharedPreferences pref = getActivity().getSharedPreferences(SHARED_PREFERENCE_POPULAR_MOVIES, Activity.MODE_PRIVATE); // 0 - for private mode

            Set movies = new HashSet();
            movies = pref.getStringSet(SHARED_PREFERENCE_FAVORITES_KEY, null);

            if (movies != null) {
                Gson gson = new Gson();
                ArrayList<Movie> savedMovies = new ArrayList<>();

                for (Object movie : movies) {
                    Movie temp = gson.fromJson(movie.toString(), Movie.class);
                    savedMovies.add(temp);
                }

                ((PosterAdapter) gvPopMovies.getAdapter()).setData(savedMovies);
                ((PosterAdapter) gvPopMovies.getAdapter()).notifyDataSetChanged();

                gvPopMovies.setVisibility(View.VISIBLE);

                tvError.setVisibility(View.GONE);
                btRetry.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "You don't have any favorite movies saved", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            boolean onLine = isOnline();

            if (onLine) {
                HelperMovies helper = new HelperMovies();
                int orientation = getResources().getConfiguration().orientation;

                helper.execute(new Object[]{type, filter, sorting, gvPopMovies, tvError, btRetry, getActivity(), orientation});
            } else {
                tvError.setText(getResources().getString(R.string.error_network_unavailable));
            }

            gvPopMovies.setVisibility(onLine ? View.VISIBLE : View.GONE);

            tvError.setVisibility(onLine ? View.GONE : View.VISIBLE);
            btRetry.setVisibility(onLine ? View.GONE : View.VISIBLE);
        }
    }

    /*
    * From: https://developer.android.com/training/basics/network-ops/managing.html
    * On: 2015-09-22
    * */
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void updateInfo (String sorting) {
        executeQuery(movieDB_discovery, movieDB_filter_date_2015, sorting);
    }
}
