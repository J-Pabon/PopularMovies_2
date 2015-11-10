package popularmovies.portfolio.jpabon.com.popularmoviesv3.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters.TrailerAdapter;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Video;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.Constants;

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

import static popularmovies.portfolio.jpabon.com.popularmoviesv3.tools.TMDBKey.movieDB_key;

/**
 * Created by JPabon on 2015-11-04.
 */
public class HelperVideos extends AsyncTask {
    ListView lvTrailers;
    TextView tvNoTrailers;

    Context context;

    @Override
    protected ArrayList<Video> doInBackground(Object[] params) {
        String type_query = (String) params [0];
        long id_movie = (Long) params [1];

        lvTrailers = (ListView) params [2];
        tvNoTrailers = (TextView) params [3];

        context = (Context) params [4];

        try {
            return getVideos(type_query, id_movie);
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
            ok = ((ArrayList<Video>)result).size() > 0;
        }

        if (ok) {
            ((TrailerAdapter) lvTrailers.getAdapter()).setData((ArrayList<Video>) result);
            ((TrailerAdapter) lvTrailers.getAdapter()).notifyDataSetChanged();
        } else {
            tvNoTrailers.setText(context.getResources().getString(R.string.no_trailers));
        }

        lvTrailers.setVisibility(ok ? View.VISIBLE : View.GONE);
        tvNoTrailers.setVisibility(ok ? View.GONE : View.VISIBLE);
    };

    public ArrayList<Video> getVideos(String type_query, long id_movie) throws IOException, ParseException {
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
        stringBuilder.append(movieDB_key.replace('&', '?'));

        return new URL(stringBuilder.toString());
    }

    private ArrayList<Video> parseResultVideos(String result) throws ParseException {
        ArrayList<Video> results = new ArrayList<Video>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = (JSONArray) jsonObject.get("results");

            for (int i = 0; i < array.length(); i++) {
                Video video  = new Video(array.getJSONObject(i));
                results.add(video);
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