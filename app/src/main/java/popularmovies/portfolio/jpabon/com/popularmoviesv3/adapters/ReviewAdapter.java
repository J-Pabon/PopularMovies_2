package popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

        ViewHolderer holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_review, parent, false);
            holder = new ViewHolderer();

            holder.tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.tvUrl = (TextView) convertView.findViewById(R.id.tvUrl);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolderer) convertView.getTag();
        }

        holder.tvAuthor.setText("By: " + getData().get(position).getAuthor());
        holder.tvUrl.setText("(" + getData().get(position).getUrl() + ")");
        holder.tvContent.setText(getData().get(position).getContent());

        return convertView;
    }

    class ViewHolderer {
        TextView tvAuthor;
        TextView tvUrl;
        TextView tvContent;
    }
}