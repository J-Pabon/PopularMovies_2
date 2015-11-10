package popularmovies.portfolio.jpabon.com.popularmoviesv3.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.fragments.DetailsFragment;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.fragments.PopularMoviesFragment;

import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.favorites;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.movieDB_sort_popularity;
import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants.movieDB_sort_rate;

public class PopularMoviesActivity extends AppCompatActivity {
    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        setContentView(R.layout.activity_popular_movies);

        if (findViewById(R.id.movies_details) != null) {
            mTwoPane = true;
            // Its a tablet, so create a new detail fragment if one does not already exist
            DetailsFragment detailsFragment = (DetailsFragment) fragmentManager.findFragmentById (R.id.movies_details);

            if (detailsFragment == null) {
                // Initialize new detail fragment
                detailsFragment = new DetailsFragment();

                fragmentManager.beginTransaction().replace(R.id.movies_details, detailsFragment, "").commit();
            }
        }

        // Initialize a new golfcourse list fragment, if one does not already exist
        PopularMoviesFragment moviesFragment = (PopularMoviesFragment) fragmentManager.findFragmentById(R.id.movies_grid);

        if ( moviesFragment == null) {
            moviesFragment = new PopularMoviesFragment();

            fragmentManager.beginTransaction().replace(R.id.movies_grid, moviesFragment, "").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.action_popularity) {
            ((PopularMoviesFragment)fragmentManager.findFragmentById(R.id.movies_grid)).updateInfo(movieDB_sort_popularity);
            return true;
        } else if (id == R.id.action_vote) {

            ((PopularMoviesFragment)fragmentManager.findFragmentById(R.id.movies_grid)).updateInfo(movieDB_sort_rate);
            return true;
        } else if (id == R.id.action_favorites) {

            ((PopularMoviesFragment)fragmentManager.findFragmentById(R.id.movies_grid)).updateInfo(favorites);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
