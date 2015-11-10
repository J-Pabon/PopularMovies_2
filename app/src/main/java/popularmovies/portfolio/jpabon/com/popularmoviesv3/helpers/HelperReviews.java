package popularmovies.portfolio.jpabon.com.popularmoviesv3.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters.ReviewAdapter;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Review;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.TMDBKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;

/**
 * Created by JPabon on 2015-11-04.
 */
public class HelperReviews extends AsyncTask {
    ListView lvReviews;
    TextView tvNoReviews;

    Context context;

    @Override
    protected ArrayList<Review> doInBackground(Object[] params) {
        String type_query = (String) params [0];
        long id_movie = (Long) params [1];

        lvReviews = (ListView) params [2];
        tvNoReviews = (TextView) params [3];

        context = (Context) params [4];

        try {
            return getReviews(type_query, id_movie);
        } catch (IOException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        boolean ok = false;

        if (result != null) {
            ok = ((ArrayList<Review>)result).size() > 0;
        }

        if (ok) {
            ((ReviewAdapter) lvReviews.getAdapter()).setData((ArrayList<Review>) result);
            ((ReviewAdapter) lvReviews.getAdapter()).notifyDataSetChanged();
        } else {
            tvNoReviews.setText(context.getResources().getString(R.string.no_reviews));
        }

        lvReviews.setVisibility(ok ? View.VISIBLE : View.GONE);
        tvNoReviews.setVisibility(ok ? View.GONE : View.VISIBLE);
    };

    public ArrayList<Review> getReviews(String type_query, long id_movie) throws IOException, ParseException {
        URL url = buildURLExtras(type_query, id_movie);

        InputStream stream = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(Constants.READ_TIMEOUT);
            conn.setConnectTimeout(Constants.CONNECT_TIMEOUT);

            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept", "application/json");

            conn.setDoInput(true);

            conn.connect();

            int responseCode = conn.getResponseCode();
            stream = conn.getInputStream();

            return parseResultVideos(StreamToString(stream));
        } catch (Exception ex) {
            return null;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private URL buildURLExtras(String type_query, long id_movie) throws MalformedURLException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Constants.movieDB_base_query);
        stringBuilder.append(String.format(type_query, id_movie));
        stringBuilder.append(TMDBKey.movieDB_key.replace('&', '?'));

        return new URL(stringBuilder.toString());
    }

    private ArrayList<Review> parseResultVideos(String result) throws ParseException {
        ArrayList<Review> results = new ArrayList<Review>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = (JSONArray) jsonObject.get("results");

            for (int i = 0; i < array.length(); i++) {
                Review review  = new Review(array.getJSONObject(i));
                results.add(review);
            }
        } catch (JSONException e) {
            System.err.println(e);
        }
        return results;
    }

    public String StreamToString(InputStream stream) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");

        BufferedReader bufferedReader = new BufferedReader(reader);

        return bufferedReader.readLine();
    }
}