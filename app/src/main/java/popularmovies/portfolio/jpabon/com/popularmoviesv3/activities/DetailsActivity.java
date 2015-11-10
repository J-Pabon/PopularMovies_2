package popularmovies.portfolio.jpabon.com.popularmoviesv3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Movie;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {
    Movie movieDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        movieDetails = intent.getExtras().getParcelable("movie_details");


        // Show the Up button in the action bar.
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie_details", movieDetails);

            DetailsFragment fragment = new DetailsFragment();

            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();
        }
    }

}
