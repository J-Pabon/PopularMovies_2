package popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;
import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Review;

/**
 * Created by JPabon on 2015-11-07.
 */
public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Review> data;

    public ArrayList<Review> getData() {
        return data;
    }

    public void setData(ArrayList<Review> data) {
        this.data = data;
    }

    public ReviewAdapter(Context c) {
        context = c;
        setData(new ArrayList<Review>());
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public Object getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_review, parent, false);

        TextView tvAuthor = (TextView) layout.findViewById(R.id.tvAuthor);
        tvAuthor.setText("By: " + getData().get(position).getAuthor());

        TextView tvUrl = (TextView) layout.findViewById(R.id.tvUrl);
        tvUrl.setText("(" + getData().get(position).getUrl() + ")");

        TextView tvContent = (TextView) layout.findViewById(R.id.tvContent);
        tvContent.setText(getData().get(position).getContent());


        return layout;
    }
}